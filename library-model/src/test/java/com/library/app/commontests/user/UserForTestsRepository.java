package com.library.app.commontests.user;


import com.library.app.common.utils.DateUtils;
import com.library.app.common.utils.PasswordUtils;
import com.library.app.user.model.Customer;
import com.library.app.user.model.Employee;
import com.library.app.user.model.User;
import com.library.app.user.model.User.Roles;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.List;

/**
 * The type User for tests repository. Helper class to set up some users for testing.
 */
@Ignore
public class UserForTestsRepository {

    /**
     * John doe user. Customer
     *
     * @return the user
     */
    public static User johnDoe() {
        final User user = new Customer();
        user.setName("John doe");
        user.setEmail("john@domain.com");
        user.setPassword("123456");

        return user;
    }

    /**
     * Mary user. Customer.
     *
     * @return the user
     */
    public static User mary() {
        final User user = new Customer();
        user.setName("Mary");
        user.setEmail("mary@domain.com");
        user.setPassword("987789");

        return user;
    }

    /**
     * Admin user. Employee and Admin.
     *
     * @return the user
     */
    public static User admin() {
        final User user = new Employee();
        user.setName("Admin");
        user.setEmail("admin@domain.com");
        user.setPassword("654321");
        user.setRoles(Arrays.asList(Roles.EMPLOYEE, Roles.ADMINISTRATOR));

        return user;
    }

    /**
     * All users list.
     *
     * @return the list
     */
    public static List<User> allUsers() {
        return Arrays.asList(admin(), johnDoe(), mary());
    }

    /**
     * User with id and created at user. Helper method to dynamically create a user and assing an id to it.
     *
     * @param user the user
     * @param id   the id
     * @return the user
     */
    public static User userWithIdAndCreatedAt(final User user, final Long id) {
        user.setId(id);
        user.setCreatedAt(DateUtils.getAsDateTime("2015-01-03T22:35:42Z"));

        return user;
    }

    /**
     * User with encrypted password user.
     *
     * @param user the user
     * @return the user
     */
    public static User userWithEncryptedPassword(final User user) {
        user.setPassword(PasswordUtils.encryptPassword(user.getPassword()));
        return user;
    }

}