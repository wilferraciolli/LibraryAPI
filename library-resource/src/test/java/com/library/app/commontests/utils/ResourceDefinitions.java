package com.library.app.commontests.utils;

import org.junit.Ignore;

/**
 * Enum to hold the name of resources that will be accessed by Arquillian.
 *
 * @author wilferaciolli
 */
@Ignore
public enum ResourceDefinitions {

    /**
     * Category resource definitions.
     */
    CATEGORY("categories"),
    /**
     * Author resource definitions.
     */
    AUTHOR("authors"),
    /**
     * User resource definitions.
     */
    USER("users"),
    /**
     * Book resource definitions.
     */
    BOOK("books"),
    /**
     * Order resource definitions.
     */
    ORDER("orders");

    private final String resourceName;

    /**
     * Instantiates a new Resource definitions.
     *
     * @param resourceName the resource name
     */
    private ResourceDefinitions(final String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Gets resource name.
     *
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }
}
