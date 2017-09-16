package com.library.app.common.exception;

import javax.ejb.ApplicationException;

/**
 * The type User not authorized exception. This exception is to beused to a user that is trying to request a resource
 * that their user role does not allow to.
 */
@ApplicationException
public class UserNotAuthorizedException extends RuntimeException {
    private static final long serialVersionUID = -1449722059595947793L;
}
