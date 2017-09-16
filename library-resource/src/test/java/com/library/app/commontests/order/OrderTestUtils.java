package com.library.app.commontests.order;

import com.library.app.order.model.Order;

/**
 * The type Order test utils.
 */
public class OrderTestUtils {

    /**
     * Gets status as json.
     *
     * @param status the status
     * @return the status as json
     */
    public static String getStatusAsJson(final Order.OrderStatus status) {
        return String.format("{\"status\":\"%s\"}", status);
    }

}