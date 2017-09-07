package com.library.app.user.services;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.User;
import com.library.app.user.model.filter.UserFilter;

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

    /**
     * Find user by id user.
     *
     * @param id the id
     * @return the user
     * @throws UserNotFoundException the user not found exception
     */
    User findById(Long id) throws UserNotFoundException;

    /**
     * Update user. This method does not update the user's password, but their details.
     *
     * @param user the user
     * @throws FieldNotValidException the field not valid exception
     * @throws UserExistentException  the user existent exception
     * @throws UserNotFoundException  the user not found exception
     */
    void update(User user) throws FieldNotValidException, UserExistentException, UserNotFoundException;

    /**
     * Update password.
     *
     * @param id       the id
     * @param password the password
     * @throws UserNotFoundException the user not found exception
     */
    void updatePassword(Long id, String password) throws UserNotFoundException;

    /**
     * Find user by email user.
     *
     * @param email the email
     * @return the user
     * @throws UserNotFoundException the user not found exception
     */
    User findByEmail(String email) throws UserNotFoundException;

    /**
     * Find by email and password user.
     *
     * @param email    the email
     * @param password the password
     * @return the user
     * @throws UserNotFoundException the user not found exception
     */
    User findByEmailAndPassword(String email, String password) throws UserNotFoundException;

    /**
     * Find by filter paginated data.
     *
     * @param userFilter the user filter
     * @return the paginated data
     */
    PaginatedData<User> findByFilter(UserFilter userFilter);
}
