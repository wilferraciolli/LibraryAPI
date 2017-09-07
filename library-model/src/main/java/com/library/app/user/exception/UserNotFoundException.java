package com.library.app.user.exception;

import javax.ejb.ApplicationException;

/**
 * The type User not found exception.
 */
@ApplicationException
public class UserNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -60289524691210822L;

}