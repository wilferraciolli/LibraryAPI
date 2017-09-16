package com.library.app.order.exception;

import javax.ejb.ApplicationException;

/**
 * The type Order status cannot be changed exception.
 */
@ApplicationException
public class OrderStatusCannotBeChangedException extends RuntimeException {
    private static final long serialVersionUID = -37439605945843644L;

    /**
     * Instantiates a new Order status cannot be changed exception.
     *
     * @param message the message
     */
    public OrderStatusCannotBeChangedException(final String message) {
        super(message);
    }

}