package com.library.app.order.services.impl;

import com.library.app.book.model.Book;
import com.library.app.book.services.BookServices;
import com.library.app.common.exception.UserNotAuthorizedException;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.utils.DateUtils;
import com.library.app.common.utils.ValidationUtils;
import com.library.app.order.exception.OrderNotFoundException;
import com.library.app.order.exception.OrderStatusCannotBeChangedException;
import com.library.app.order.model.Order;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.OrderItem;
import com.library.app.order.model.filter.OrderFilter;
import com.library.app.order.repository.OrderRepository;
import com.library.app.order.services.OrderServices;
import com.library.app.user.model.Customer;
import com.library.app.user.model.User;
import com.library.app.user.model.User.Roles;
import com.library.app.user.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.Queue;
import javax.validation.Validator;

/**
 * The type Order services.
 */
@Stateless
public class OrderServicesImpl implements OrderServices {

    @Inject
    OrderRepository orderRepository;

    @Inject
    UserServices userServices;

    @Inject
    BookServices bookServices;

    @Inject
    Validator validator;

    //Inject Event to be able to send CDI events for synchronous
//    @Inject
//    private Event<Order> orderEventSender;

    //Inject the Queue to be able to use the queue for asynchronous messages
    @Resource(mappedName = "java:/jms/queue/Orders")
    private Queue ordersQueue;
    @Inject
    @JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
    private JMSContext jmsContext;


    @Resource
    SessionContext sessionContext;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Order add(final Order order) {
        checkCustomerAndSetItOnOrder(order);
        checkBooksForItemsAndSetThem(order);

        order.setInitialStatus();
        order.calculateTotal();

        //validate base entity
        ValidationUtils.validateEntityFields(validator, order);

        //publish the event when order was placed
        sendEvent(order);

        return orderRepository.add(order);
    }

    @Override
    public Order findById(final Long id) {
        final Order order = orderRepository.findById(id);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    @Override
    public void updateStatus(final Long id, final OrderStatus newStatus) {
        final Order order = findById(id);

        //check that is an employee who is setting the order to delivered
        if (newStatus == OrderStatus.DELIVERED) {
            if (!sessionContext.isCallerInRole(Roles.EMPLOYEE.name())) {
                throw new UserNotAuthorizedException();
            }
        }

        //check that is a customer cancelling the order, and is order owner
        if (newStatus == OrderStatus.CANCELLED) {
            if (sessionContext.isCallerInRole(Roles.CUSTOMER.name())) {
                if (!order.getCustomer().getEmail().equals(sessionContext.getCallerPrincipal().getName())) {
                    throw new UserNotAuthorizedException();
                }
            }
        }

        try {
            order.addHistoryEntry(newStatus);
        } catch (final IllegalArgumentException e) {
            throw new OrderStatusCannotBeChangedException(e.getMessage());
        }

        //publish the event when order was updated
        sendEvent(order);

        orderRepository.update(order);
    }

    @Override
    public PaginatedData<Order> findByFilter(final OrderFilter orderFilter) {
        return orderRepository.findByFilter(orderFilter);
    }

    @Override
    public void changeStatusOfExpiredOrders(final int daysBeforeOrderExpiration) {
        logger.debug("Finding order to be expired that are reserved with more than {} days", daysBeforeOrderExpiration);

        //create a filter to get orders by specific date
        final OrderFilter orderFilter = new OrderFilter();
        orderFilter.setEndDate(DateUtils.currentDatePlusDays(-daysBeforeOrderExpiration));
        orderFilter.setStatus(OrderStatus.RESERVED);

        //get every order that should be changed to expired status
        final PaginatedData<Order> ordersToBeExpired = findByFilter(orderFilter);
        logger.debug("Found {} orders to be expired", ordersToBeExpired.getNumberOfRows());
        for (final Order order : ordersToBeExpired.getRows()) {
            //update the status of the order
            updateStatus(order.getId(), OrderStatus.RESERVATION_EXPIRED);
        }
        logger.debug("Orders expired!");
    }

    /**
     * Check customer and set it on order.
     *
     * @param order the order
     */
    private void checkCustomerAndSetItOnOrder(final Order order) {
        final User user = userServices.findByEmail(sessionContext.getCallerPrincipal().getName());
        order.setCustomer((Customer) user);
    }

    /**
     * Check books for items and set them.
     *
     * @param order the order
     */
    private void checkBooksForItemsAndSetThem(final Order order) {
        for (final OrderItem item : order.getItems()) {
            if (item.getBook() != null) {
                final Book book = bookServices.findById(item.getBook().getId());
                item.setBook(book);
            }
        }
    }

    /**
     * Send event. This method will publish an event so decoupled objects can use it.
     * This method uses both an event feom CDI or a message from JMS queue
     *
     * @param order the order
     */
    public void sendEvent(final Order order) {

        //send synchronous event
        //orderEventSender.fire(order);

        //send asynchronous message to the queue
        jmsContext.createProducer().send(ordersQueue, order);
    }

}