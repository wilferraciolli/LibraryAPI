package com.library.app.order.services.impl;

import com.library.app.book.exception.BookNotFoundException;
import com.library.app.book.services.BookServices;
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

import static com.library.app.commontests.order.OrderForTestsRepository.orderReserved;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
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

    private void setUpLoggedEmail(final String email, final Roles userRole) {
        reset(sessionContext);

        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(email);

        when(sessionContext.getCallerPrincipal()).thenReturn(principal);
        when(sessionContext.isCallerInRole(Roles.EMPLOYEE.name())).thenReturn(userRole == Roles.EMPLOYEE);
        when(sessionContext.isCallerInRole(Roles.CUSTOMER.name())).thenReturn(userRole == Roles.CUSTOMER);
    }

}