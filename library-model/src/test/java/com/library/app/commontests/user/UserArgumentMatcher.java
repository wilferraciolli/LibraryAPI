package com.library.app.commontests.user;

import com.library.app.user.model.User;
import org.mockito.ArgumentMatcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.argThat;

/**
 * The type User argument matcher. This is an ovveride of ArgumentMatcher from Mockito.
 */
public class UserArgumentMatcher extends ArgumentMatcher<User> {
    private final User expectedUser;

    /**
     * User equals user. Using userEq from Mockito.
     *
     * @param expectedUser the expected user
     * @return the user
     */
    public static User userEq(final User expectedUser) {
        return argThat(new UserArgumentMatcher(expectedUser));
    }

    /**
     * Instantiates a new User argument matcher.
     *
     * @param expectedUser the expected user
     */
    public UserArgumentMatcher(final User expectedUser) {
        this.expectedUser = expectedUser;
    }

    @Override
    public boolean matches(final Object argument) {
        //ovveride equals method to reinforce the equals check

        final User actualUser = (User) argument;

        assertThat(actualUser.getId(), is(equalTo(expectedUser.getId())));
        assertThat(actualUser.getName(), is(equalTo(expectedUser.getName())));
        assertThat(actualUser.getEmail(), is(equalTo(expectedUser.getEmail())));
        assertThat(actualUser.getPassword(), is(equalTo(expectedUser.getPassword())));

        return true;
    }

}