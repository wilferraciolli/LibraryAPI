package com.library.app.user.services.impl;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.model.User;
import com.library.app.user.repository.UserRepository;
import com.library.app.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.validation.Validation;
import javax.validation.Validator;

import static com.library.app.commontests.user.UserArgumentMatcher.userEq;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static com.library.app.commontests.user.UserForTestsRepository.userWithEncryptedPassword;
import static com.library.app.commontests.user.UserForTestsRepository.userWithIdAndCreatedAt;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
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
