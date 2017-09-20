package com.library.app.order.services.impl;

import com.library.app.common.appproperties.PropertyValue;
import com.library.app.order.services.OrderServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 * The type Order expirator job. This singleton bean will be used to run as batchlet every hour to mark the orders as expired.
 */
@Singleton
public class OrderExpiratorJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private OrderServices orderServices;

    //get value of property from application.properties file
    @PropertyValue(name = "days-before-order-expiration")
    private Integer daysBeforeOrderExpiration;

    /**
     * Run. the batch job. commented out code triggers it to run every minute.
     */
    //@Schedule(hour = "*", minute = "*", persistent = false)
    @Schedule(hour = "*/1", minute = "0", second = "0", persistent = false)
    public void run() {
        logger.debug("Executing order expirator job");
        orderServices.changeStatusOfExpiredOrders(daysBeforeOrderExpiration);
    }

}