package com.library.app.order.services.impl;

import com.library.app.book.exception.BookNotFoundException;
import com.library.app.book.services.BookServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.exception.UserNotAuthorizedException;
import com.library.app.common.model.PaginatedData;
import com.library.app.order.exception.OrderNotFoundException;
import com.library.app.order.exception.OrderStatusCannotBeChangedException;
import com.library.app.order.model.Order;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.filter.OrderFilter;
import com.library.app.order.repository.OrderRepository;
import com.library.app.order.services.OrderServices;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.User.Roles;
import com.library.app.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ejb.SessionContext;
import javax.validation.Validation;
import javax.validation.Validator;
import java.security.Principal;
import java.util.Arrays;

import static com.library.app.commontests.book.BookForTestsRepository.bookWithId;
import static com.library.app.commontests.book.BookForTestsRepository.designPatterns;
import static com.library.app.commontests.book.BookForTestsRepository.refactoring;
import static com.library.app.commontests.order.OrderArgumentMatcher.orderEq;
import static com.library.app.commontests.order.OrderForTestsRepository.orderDelivered;
import static com.library.app.commontests.order.OrderForTestsRepository.orderReserved;
import static com.library.app.commontests.order.OrderForTestsRepository.orderWithId;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type Order services unit test.
 */
public class OrderServicesUtest {

    private Validator validator;
    private OrderServices orderServices;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserServices userServices;

    @Mock
    private BookServices bookServices;

    @Mock
    private SessionContext sessionContext;

    private static final String LOGGED_EMAIL = "anyemail@domain.com";

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        MockitoAnnotations.initMocks(this);

        orderServices = new OrderServicesImpl();

        ((OrderServicesImpl) orderServices).validator = validator;
        ((OrderServicesImpl) orderServices).orderRepository = orderRepository;
        ((OrderServicesImpl) orderServices).userServices = userServices;
        ((OrderServicesImpl) orderServices).bookServices = bookServices;
        ((OrderServicesImpl) orderServices).sessionContext = sessionContext;

        setUpLoggedEmail(LOGGED_EMAIL, Roles.ADMINISTRATOR);
    }

    /**
     * Add order with inexistent customer.
     *
     * @throws Exception the exception
     */
    @Test(expected = UserNotFoundException.class)
    public void addOrderWithInexistentCustomer() throws Exception {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenThrow(new UserNotFoundException());

        orderServices.add(orderReserved());
    }

    /**
     * Add order with inexistent book.
     */
    @Test(expected = BookNotFoundException.class)
    public void addOrderWithInexistentBook() {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(johnDoe());
        when(bookServices.findById(anyLong())).thenThrow(new BookNotFoundException());

        orderServices.add(orderReserved());
    }

    /**
     * Add order with null quantity in one item.
     */
    @Test
    public void addOrderWithNullQuantityInOneItem() {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(johnDoe());
        when(bookServices.findById(1L)).thenReturn(bookWithId(designPatterns(), 1L));
        when(bookServices.findById(2L)).thenReturn(bookWithId(refactoring(), 2L));

        final Order order = orderReserved();
        order.getItems().iterator().next().setQuantity(null);

        addOrderWithInvalidField(order, "items[].quantity");
    }

    /**
     * Add order without items.
     *
     * @throws Exception the exception
     */
    @Test
    public void addOrderWithoutItems() throws Exception {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(johnDoe());

        final Order order = orderReserved();
        order.setItems(null);
        addOrderWithInvalidField(order, "items");
    }

    /**
     * Add order with null book in one item.
     *
     * @throws Exception the exception
     */
    @Test
    public void addOrderWithNullBookInOneItem() throws Exception {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(johnDoe());

        final Order order = orderReserved();
        order.getItems().iterator().next().setBook(null);

        addOrderWithInvalidField(order, "items[].book");
    }

    /**
     * Add valid order.
     */
    @Test
    public void addValidOrder() {
        when(userServices.findByEmail(LOGGED_EMAIL)).thenReturn(johnDoe());
        when(bookServices.findById(1L)).thenReturn(bookWithId(designPatterns(), 1L));
        when(bookServices.findById(2L)).thenReturn(bookWithId(refactoring(), 2L));
        when(orderRepository.add(orderEq(orderReserved()))).thenReturn(orderWithId(orderReserved(), 1L));

        final Order order = new Order();
        order.setItems(orderReserved().getItems());

        final Long id = orderServices.add(order).getId();
        assertThat(id, is(notNullValue()));
    }

    /**
     * Find order by id not found.
     */
    @Test(expected = OrderNotFoundException.class)
    public void findOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(null);

        orderServices.findById(1L);
    }

    /**
     * Find order by id.
     */
    @Test
    public void findOrderById() {
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderReserved(), 1L));

        final Order order = orderServices.findById(1L);
        assertThat(order, is(notNullValue()));
    }

    /**
     * Update status order not found.
     */
    @Test(expected = OrderNotFoundException.class)
    public void updateStatusOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(null);

        orderServices.updateStatus(1L, OrderStatus.DELIVERED);
    }

    /**
     * Update status for same status.
     */
    @Test(expected = OrderStatusCannotBeChangedException.class)
    public void updateStatusForSameStatus() {
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderReserved(), 1L));

        orderServices.updateStatus(1L, OrderStatus.RESERVED);
    }

    /**
     * Update status for invalid status.
     *
     * @throws Exception the exception
     */
    @Test(expected = OrderStatusCannotBeChangedException.class)
    public void updateStatusForInvalidStatus() throws Exception {
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderDelivered(), 1L));

        orderServices.updateStatus(1L, OrderStatus.RESERVED);
    }

    /**
     * Update status delivered as not employee. Only employee can update to delivered status.
     *
     * @throws Exception the exception
     */
    @Test(expected = UserNotAuthorizedException.class)
    public void updateStatusDeliveredAsNotEmployee() throws Exception {
        setUpLoggedEmail(LOGGED_EMAIL, Roles.CUSTOMER);
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderReserved(), 1L));

        orderServices.updateStatus(1L, OrderStatus.DELIVERED);
    }

    /**
     * Update status delivered as employee.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateStatusDeliveredAsEmployee() throws Exception {
        setUpLoggedEmail(LOGGED_EMAIL, Roles.EMPLOYEE);
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderReserved(), 1L));

        orderServices.updateStatus(1L, OrderStatus.DELIVERED);

        final Order expectedOrder = orderWithId(orderReserved(), 1L);
        expectedOrder.addHistoryEntry(OrderStatus.DELIVERED);
        verify(orderRepository).update(orderEq(expectedOrder));
    }

    /**
     * Update status cancelled as customer not the order customer.
     *
     * @throws Exception the exception
     */
    @Test(expected = UserNotAuthorizedException.class)
    public void updateStatusCancelledAsCustomerNotTheOrderCustomer() throws Exception {
        setUpLoggedEmail(LOGGED_EMAIL, Roles.CUSTOMER);
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderReserved(), 1L));

        orderServices.updateStatus(1L, OrderStatus.CANCELLED);
    }

    /**
     * Update status cancelled as customer the order customer.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateStatusCancelledAsCustomerTheOrderCustomer() throws Exception {
        setUpLoggedEmail(orderReserved().getCustomer().getEmail(), Roles.CUSTOMER);
        when(orderRepository.findById(1L)).thenReturn(orderWithId(orderReserved(), 1L));

        orderServices.updateStatus(1L, OrderStatus.CANCELLED);

        final Order expectedOrder = orderWithId(orderReserved(), 1L);
        expectedOrder.addHistoryEntry(OrderStatus.CANCELLED);
        verify(orderRepository).update(orderEq(expectedOrder));
    }

    /**
     * Find by filter.
     */
    @Test
    public void findByFilter() {
        final PaginatedData<Order> orders = new PaginatedData<Order>(2,
                Arrays.asList(orderReserved(), orderDelivered()));
        when(orderRepository.findByFilter((OrderFilter) anyObject())).thenReturn(orders);

        final PaginatedData<Order> ordersReturned = orderServices.findByFilter(new OrderFilter());
        assertThat(ordersReturned.getNumberOfRows(), is(equalTo(2)));
        assertThat(ordersReturned.getRows().size(), is(equalTo(2)));
    }

    /**
     * Add order with invalid field.
     *
     * @param order        the order
     * @param invalidField the invalid field
     */
    private void addOrderWithInvalidField(final Order order, final String invalidField) {
        try {
            orderServices.add(order);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(invalidField)));
        }
    }


    /**
     * Sets up logged email.
     *
     * @param email    the email
     * @param userRole the user role
     */
    private void setUpLoggedEmail(final String email, final Roles userRole) {
        //reset the mock
        reset(sessionContext);

        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        when(sessionContext.getCallerPrincipal()).thenReturn(principal);
        when(sessionContext.isCallerInRole(Roles.EMPLOYEE.name())).thenReturn(userRole == Roles.EMPLOYEE);
        when(sessionContext.isCallerInRole(Roles.CUSTOMER.name())).thenReturn(userRole == Roles.CUSTOMER);
    }

}