package com.library.app.user.services.impl;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.utils.PasswordUtils;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.User;
import com.library.app.user.model.filter.UserFilter;
import com.library.app.user.repository.UserRepository;
import com.library.app.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Arrays;

import static com.library.app.commontests.user.UserArgumentMatcher.userEq;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static com.library.app.commontests.user.UserForTestsRepository.userWithEncryptedPassword;
import static com.library.app.commontests.user.UserForTestsRepository.userWithIdAndCreatedAt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type User services unit test.
 */
public class UserServicesUTest {

    private Validator validator;
    private UserServices userServices;

    @Mock
    private UserRepository userRepository;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        MockitoAnnotations.initMocks(this);

        userServices = new UserServicesImpl();
        ((UserServicesImpl) userServices).userRepository = userRepository;
        ((UserServicesImpl) userServices).validator = validator;
    }

    /**
     * Add user with null name.
     */
    @Test
    public void addUserWithNullName() {
        final User user = johnDoe();
        user.setName(null);
        addUserWithInvalidField(user, "name");
    }

    /**
     * Add user with short name.
     */
    @Test
    public void addUserWithShortName() {
        final User user = johnDoe();
        user.setName("Jo");
        addUserWithInvalidField(user, "name");
    }

    /**
     * Add user with null email.
     */
    @Test
    public void addUserWithNullEmail() {
        final User user = johnDoe();
        user.setEmail(null);
        addUserWithInvalidField(user, "email");
    }

    /**
     * Add user with invalid email.
     */
    @Test
    public void addUserWithInvalidEmail() {
        final User user = johnDoe();
        user.setEmail("invalidemail");
        addUserWithInvalidField(user, "email");
    }

    /**
     * Add user with null password.
     */
    @Test
    public void addUserWithNullPassword() {
        final User user = johnDoe();
        user.setPassword(null);
        addUserWithInvalidField(user, "password");
    }

    /**
     * Add existent user.
     */
    @Test(expected = UserExistentException.class)
    public void addExistentUser() {
        when(userRepository.alreadyExists(johnDoe())).thenThrow(new UserExistentException());

        userServices.add(johnDoe());
    }

    /**
     * Add valid user. Encrypt the password.
     */
    @Test
    public void addValidUser() {
        when(userRepository.alreadyExists(johnDoe())).thenReturn(false);
        when(userRepository.add(userEq(userWithEncryptedPassword(johnDoe()))))
                .thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        final User user = userServices.add(johnDoe());
        assertThat(user.getId(), is(equalTo(1L)));
    }

    /**
     * Find user by id not found.
     */
    @Test(expected = UserNotFoundException.class)
    public void findUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(null);

        userServices.findById(1L);
    }

    /**
     * Find user by id.
     */
    @Test
    public void findUserById() {
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        final User user = userServices.findById(1L);
        assertThat(user, is(notNullValue()));
        assertThat(user.getName(), is(equalTo(johnDoe().getName())));
    }

    /**
     * Update user with null name.
     */
    @Test
    public void updateUserWithNullName() {
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        final User user = userWithIdAndCreatedAt(johnDoe(), 1L);
        user.setName(null);

        try {
            userServices.update(user);
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    /**
     * Update user existent.
     *
     * @throws Exception the exception
     */
    @Test(expected = UserExistentException.class)
    public void updateUserExistent() throws Exception {
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        final User user = userWithIdAndCreatedAt(johnDoe(), 1L);
        when(userRepository.alreadyExists(user)).thenReturn(true);

        userServices.update(user);
    }

    /**
     * Update user not found.
     *
     * @throws Exception the exception
     */
    @Test(expected = UserNotFoundException.class)
    public void updateUserNotFound() throws Exception {
        final User user = userWithIdAndCreatedAt(johnDoe(), 1L);
        when(userRepository.findById(1L)).thenReturn(null);

        userServices.update(user);
    }

    /**
     * Update valid user.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateValidUser() throws Exception {
        final User user = userWithIdAndCreatedAt(johnDoe(), 1L);
        user.setPassword(null);
        when(userRepository.findById(1L)).thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        userServices.update(user);

        final User expectedUser = userWithIdAndCreatedAt(johnDoe(), 1L);

        verify(userRepository).update(userEq(expectedUser));
    }

    /**
     * Update password user not found.
     */
    @Test(expected = UserNotFoundException.class)
    public void updatePasswordUserNotFound() {
        when(userRepository.findById(1L)).thenThrow(new UserNotFoundException());

        userServices.updatePassword(1L, "123456");
    }

    /**
     * Update password.
     *
     * @throws Exception the exception
     */
    @Test
    public void updatePassword() throws Exception {
        final User user = userWithIdAndCreatedAt(johnDoe(), 1L);
        when(userRepository.findById(1L)).thenReturn(user);

        userServices.updatePassword(1L, "654654");

        final User expectedUser = userWithIdAndCreatedAt(johnDoe(), 1L);
        expectedUser.setPassword(PasswordUtils.encryptPassword("654654"));

        verify(userRepository).update(userEq(expectedUser));
    }

    /**
     * Find user by email not found.
     *
     * @throws UserNotFoundException the user not found exception
     */
    @Test(expected = UserNotFoundException.class)
    public void findUserByEmailNotFound() throws UserNotFoundException {
        when(userRepository.findByEmail(johnDoe().getEmail())).thenReturn(null);

        userServices.findByEmail(johnDoe().getEmail());
    }

    /**
     * Find user by email.
     *
     * @throws UserNotFoundException the user not found exception
     */
    @Test
    public void findUserByEmail() throws UserNotFoundException {
        when(userRepository.findByEmail(johnDoe().getEmail())).thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        final User user = userServices.findByEmail(johnDoe().getEmail());
        assertThat(user, is(notNullValue()));
        assertThat(user.getName(), is(equalTo(johnDoe().getName())));
    }

    /**
     * Find user by email and password not found.
     */
    @Test(expected = UserNotFoundException.class)
    public void findUserByEmailAndPasswordNotFound() {
        final User user = johnDoe();
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        userServices.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    /**
     * Find user by and password email with invalid password.
     *
     * @throws UserNotFoundException the user not found exception
     */
    @Test(expected = UserNotFoundException.class)
    public void findUserByAndPasswordEmailWithInvalidPassword() throws UserNotFoundException {
        final User user = johnDoe();
        user.setPassword("1111");

        User userReturned = userWithIdAndCreatedAt(johnDoe(), 1L);
        userReturned = userWithEncryptedPassword(userReturned);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(userReturned);

        userServices.findByEmailAndPassword(user.getEmail(), user.getPassword());
    }

    /**
     * Find user by and password email.
     *
     * @throws UserNotFoundException the user not found exception
     */
    @Test
    public void findUserByAndPasswordEmail() throws UserNotFoundException {
        User user = johnDoe();

        User userReturned = userWithIdAndCreatedAt(johnDoe(), 1L);
        userReturned = userWithEncryptedPassword(userReturned);

        when(userRepository.findByEmail(user.getEmail())).thenReturn(userReturned);

        user = userServices.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(user, is(notNullValue()));
        assertThat(user.getName(), is(equalTo(johnDoe().getName())));
    }

    /**
     * Find user by filter.
     */
    @Test
    public void findUserByFilter() {
        final PaginatedData<User> users = new PaginatedData<>(1,
                Arrays.asList(userWithIdAndCreatedAt(johnDoe(), 1L)));
        when(userRepository.findByFilter((UserFilter) anyObject())).thenReturn(users);

        final PaginatedData<User> usersReturned = userServices.findByFilter(new UserFilter());
        assertThat(usersReturned.getNumberOfRows(), is(equalTo(1)));
        assertThat(usersReturned.getRow(0).getName(), is(equalTo(johnDoe().getName())));
    }


    /**
     * Add user with invalid field helper method.
     *
     * @param user                     the user
     * @param expectedInvalidFieldName the expected invalid field name
     */
    private void addUserWithInvalidField(final User user, final String expectedInvalidFieldName) {
        try {
            userServices.add(user);
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo(expectedInvalidFieldName)));
        }
    }

}
