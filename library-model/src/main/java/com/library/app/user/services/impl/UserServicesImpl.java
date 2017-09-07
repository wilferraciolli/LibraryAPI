package com.library.app.user.services.impl;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.utils.PasswordUtils;
import com.library.app.common.utils.ValidationUtils;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.model.User;
import com.library.app.user.repository.UserRepository;
import com.library.app.user.services.UserServices;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Validator;

/**
 * The type User services business logic.
 */
@Stateless
public class UserServicesImpl implements UserServices {

    @Inject
    UserRepository userRepository;

    @Inject
    Validator validator;

    @Override
    public User add(final User user) {

        //validate and encrypt user password
        validateUser(user);
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));

        return userRepository.add(user);
    }

    /**
     * Validate user.
     *
     * @param user the user
     * @throws FieldNotValidException the field not valid exception
     * @throws UserExistentException  the user existent exception
     */
    private void validateUser(final User user) {
        if (userRepository.alreadyExists(user)) {
            throw new UserExistentException();
        }

        ValidationUtils.validateEntityFields(validator, user);
    }
}
