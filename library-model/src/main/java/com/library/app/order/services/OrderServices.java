package com.library.app.order.services;

import com.library.app.book.exception.BookNotFoundException;
import com.library.app.order.model.Order;
import com.library.app.user.exception.UserNotFoundException;

import javax.ejb.Local;

/**
 * The interface Order services.
 */
@Local
public interface OrderServices {

    /**
     * Add order.
     *
     * @param order the order
     * @return the order
     * @throws UserNotFoundException the user not found exception
     * @throws BookNotFoundException the book not found exception
     */
    Order add(Order order) throws UserNotFoundException, BookNotFoundException;

}
