package com.library.app.user.services.impl;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.utils.PasswordUtils;
import com.library.app.common.utils.ValidationUtils;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.User;
import com.library.app.user.model.filter.UserFilter;
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

    @Override
    public User findById(final Long id) {
        final User user = userRepository.findById(id);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public void update(final User user) {
        //find the user
        final User existentUser = findById(user.getId());
        //set his password to whatever it was previously avoiding the to set their own passwords via this method
        user.setPassword(existentUser.getPassword());

        validateUser(user);

        userRepository.update(user);
    }

    @Override
    public void updatePassword(final Long id, final String password) {
        final User user = findById(id);
        user.setPassword(PasswordUtils.encryptPassword(password));

        userRepository.update(user);
    }

    @Override
    public User findByEmail(final String email) throws UserNotFoundException {
        final User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    @Override
    public User findByEmailAndPassword(final String email, final String password) {
        final User user = findByEmail(email);

        if (!user.getPassword().equals(PasswordUtils.encryptPassword(password))) {
            throw new UserNotFoundException();
        }

        return user;
    }

    @Override
    public PaginatedData<User> findByFilter(final UserFilter userFilter) {
        return userRepository.findByFilter(userFilter);
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
