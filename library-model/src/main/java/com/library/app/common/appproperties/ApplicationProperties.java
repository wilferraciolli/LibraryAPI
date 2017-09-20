package com.library.app.common.appproperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import java.io.IOException;
import java.util.Properties;


/**
 * The type Application properties. Bean used to inject the application.properties file to the application.
 */
@ApplicationScoped
public class ApplicationProperties {

    private Properties properties;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Init. method invoked after the class is initalized. This loads the application.properties file
     */
    @PostConstruct
    public void init() {
        try {
            properties = new Properties();
            properties.load(ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (final IOException e) {
            logger.error("Error while reading the properties file", e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Gets days before order expiration. it reads from the application.properties file
     *
     * @return the days before order expiration
     */
    public int getDaysBeforeOrderExpiration() {
        return Integer.valueOf(properties.getProperty("days-before-order-expiration"));
    }
}
