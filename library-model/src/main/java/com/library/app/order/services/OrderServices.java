package com.library.app.order.services;

import com.library.app.book.exception.BookNotFoundException;
import com.library.app.common.exception.UserNotAuthorizedException;
import com.library.app.common.model.PaginatedData;
import com.library.app.order.exception.OrderNotFoundException;
import com.library.app.order.exception.OrderStatusCannotBeChangedException;
import com.library.app.order.model.Order;
import com.library.app.order.model.Order.OrderStatus;
import com.library.app.order.model.filter.OrderFilter;
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

    /**
     * Find by id order.
     *
     * @param id the id
     * @return the order
     * @throws OrderNotFoundException the order not found exception
     */
    Order findById(Long id) throws OrderNotFoundException;

    /**
     * Update status. Some statuses can only be updated by certain user roles.
     *
     * @param id        the id
     * @param newStatus the new status
     * @throws OrderNotFoundException              the order not found exception
     * @throws OrderStatusCannotBeChangedException the order status cannot be changed exception
     * @throws UserNotAuthorizedException          the user not authorized exception
     */
    void updateStatus(Long id, OrderStatus newStatus) throws OrderNotFoundException,
            OrderStatusCannotBeChangedException, UserNotAuthorizedException;

    /**
     * Find by filter paginated data.
     *
     * @param orderFilter the order filter
     * @return the paginated data
     */
    PaginatedData<Order> findByFilter(OrderFilter orderFilter);

}
