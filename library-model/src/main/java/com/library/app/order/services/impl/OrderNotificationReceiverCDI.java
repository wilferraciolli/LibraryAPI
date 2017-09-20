package com.library.app.order.services.impl;

import com.library.app.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

/**
 * The type Order notification receiver cdi.
 */
@ApplicationScoped
public class OrderNotificationReceiverCDI {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Receive event. This method can do anything with the order.
     *
     * @param order the order
     */
    public void receiveEvent(@Observes final Order order) {
        logger.debug("Order notification received for order: {}", order);
    }
}
