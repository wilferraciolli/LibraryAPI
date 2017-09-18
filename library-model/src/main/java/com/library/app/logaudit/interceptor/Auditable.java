package com.library.app.logaudit.interceptor;

import com.library.app.logaudit.model.LogAudit.Action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The interface Auditable. Annotation used to trigger the interceptor to audit the class where it is used.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {

    /**
     * Action action.
     *
     * @return the action
     */
    Action action();

}