package com.library.app.common.appproperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Property value. this annotation is to be used to get properties values from the application.properties file.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyValue {

    /**
     * Name string.
     *
     * @return the string
     */
    String name();
}
