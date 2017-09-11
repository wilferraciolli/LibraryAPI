package com.library.app.order.services.impl;

import com.library.app.book.model.Book;
import com.library.app.book.services.BookServices;
import com.library.app.order.model.Order;
import com.library.app.order.model.OrderItem;
import com.library.app.order.repository.OrderRepository;
import com.library.app.order.services.OrderServices;
import com.library.app.user.model.Customer;
import com.library.app.user.model.User;
import com.library.app.user.services.UserServices;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

/**
 * The type Order services.
 */
@Stateless
public class OrderServicesImpl implements OrderServices {

    /**
     * The Order repository.
     */
    @Inject
    OrderRepository orderRepository;

    /**
     * The User services.
     */
    @Inject
    UserServices userServices;

    /**
     * The Book services.
     */
    @Inject
    BookServices bookServices;

    /**
     * The Validator.
     */
    @Inject
    Validator validator;

    /**
     * The Session context.
     */
    @Resource
    SessionContext sessionContext;

    @Override
    public Order add(final Order order) {
        checkCustomerAndSetItOnOrder(order);
        checkBooksForItemsAndSetThem(order);

        return null;
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

}