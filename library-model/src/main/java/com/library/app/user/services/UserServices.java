package com.library.app.user.services;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.model.User;

import javax.ejb.Local;

/**
 * The interface User services.
 */
@Local
public interface UserServices {

    /**
     * Add user.
     *
     * @param user the user
     * @return the user
     * @throws FieldNotValidException the field not valid exception
     * @throws UserExistentException  the user existent exception
     */
    User add(User user) throws FieldNotValidException, UserExistentException;
}
