package com.library.app.commontests.utils;

import org.junit.Ignore;

/**
 * Enum to hold the name of resources that will be accessed by Arquilian.
 *
 * @author wilferaciolli
 *
 */
@Ignore
public enum ResourceDefinitions {
    CATEGORY("categories");

    private String resourceName;

    private ResourceDefinitions(final String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceName() {
        return resourceName;
    }
}
