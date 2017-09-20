package com.library.app.order.services.impl;

import com.library.app.order.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * The type Order notification receiver jms. This class will be responsible for handling the messages on the queue.
 */
@MessageDriven(name = "OrderNotificationReceiverJMS", activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/Orders"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")})
public class OrderNotificationReceiverJMS implements MessageListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(final Message message) {
        try {
            logger.debug("Order notification received for order: {}", message.getBody(Order.class));
        } catch (final JMSException e) {
            e.printStackTrace();
        }
    }
}
