package com.library.app.common.appproperties;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * The type Application properties test.
 */
public class ApplicationPropertiesTest {

    private ApplicationProperties applicationProperties;

    /**
     * Sets up test.
     */
    @Before
    public void setUpTest() {
        //as this ben isnt managed by the container, then it is required to initialize it and call the postConstruct method on it
        applicationProperties = new ApplicationProperties();
        applicationProperties.init();
    }

    /**
     * Gets days before order expiration.
     */
    @Test
    public void getDaysBeforeOrderExpiration() {
        assertThat(applicationProperties.getDaysBeforeOrderExpiration(), is(equalTo(7)));
    }


}