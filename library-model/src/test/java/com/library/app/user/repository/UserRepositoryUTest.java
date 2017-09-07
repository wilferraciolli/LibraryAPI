package com.library.app.user.repository;

import com.library.app.commontests.utils.TestBaseRepository;
import com.library.app.user.model.User;
import com.library.app.user.model.User.UserType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

/**
 * The type User repository u test.
 */
public class UserRepositoryUTest extends TestBaseRepository {

    private UserRepository userRepository;

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        initializeTestDB();

        userRepository = new UserRepository();
        userRepository.em = em;
    }

    /**
     * Sets down test case.
     */
    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    /**
     * Add customer and find it.
     */
    @Test
    public void addCustomerAndFindIt() {
        final Long userAddedId = dbCommandExecutor.executeCommand(() -> {
            return userRepository.add(johnDoe()).getId();
        });
        assertThat(userAddedId, is(notNullValue()));

        final User user = userRepository.findById(userAddedId);
        assertUser(user, johnDoe(), UserType.CUSTOMER);
    }

    /**
     * Find user by id not found.
     */
    @Test
    public void findUserByIdNotFound() {
        final User user = userRepository.findById(999L);
        assertThat(user, is(nullValue()));
    }

    /**
     * Update customer.
     */
    @Test
    public void updateCustomer() {
        final Long userAddedId = dbCommandExecutor.executeCommand(() -> {
            return userRepository.add(johnDoe()).getId();
        });
        assertThat(userAddedId, is(notNullValue()));

        final User user = userRepository.findById(userAddedId);
        assertThat(user.getName(), is(equalTo(johnDoe().getName())));

        user.setName("New name");
        dbCommandExecutor.executeCommand(() -> {
            userRepository.update(user);
            return null;
        });

        final User userAfterUpdate = userRepository.findById(userAddedId);
        assertThat(userAfterUpdate.getName(), is(equalTo("New name")));
    }

    /**
     * Already exists user without id.
     */
    @Test
    public void alreadyExistsUserWithoutId() {
        dbCommandExecutor.executeCommand(() -> {
            return userRepository.add(johnDoe()).getId();
        });

        assertThat(userRepository.alreadyExists(johnDoe()), is(equalTo(true)));
        assertThat(userRepository.alreadyExists(admin()), is(equalTo(false)));
    }

    /**
     * Already exists category with id.
     */
    @Test
    public void alreadyExistsCategoryWithId() {
        final User customer = dbCommandExecutor.executeCommand(() -> {
            userRepository.add(admin());
            return userRepository.add(johnDoe());
        });

        assertFalse(userRepository.alreadyExists(customer));

        customer.setEmail(admin().getEmail());
        assertThat(userRepository.alreadyExists(customer), is(equalTo(true)));

        customer.setEmail("newemail@domain.com");
        assertThat(userRepository.alreadyExists(customer), is(equalTo(false)));
    }

    // @Test
    // public void findUserByEmail() {
    // dbCommandExecutor.executeCommand(() -> {
    // return userRepository.add(johnDoe());
    // });
    //
    // final User user = userRepository.findByEmail(johnDoe().getEmail());
    // assertUser(user, johnDoe(), UserType.CUSTOMER);
    // }
    //
    // @Test
    // public void findUserByEmailNotFound() {
    // final User user = userRepository.findByEmail(johnDoe().getEmail());
    // assertThat(user, is(nullValue()));
    // }

    /**
     * Assert user. This is a helper method to assert users.
     *
     * @param actualUser       the actual user
     * @param expectedUser     the expected user
     * @param expectedUserType the expected user type
     */
    private void assertUser(final User actualUser, final User expectedUser, final UserType expectedUserType) {
        assertThat(actualUser.getName(), is(equalTo(expectedUser.getName())));
        assertThat(actualUser.getEmail(), is(equalTo(expectedUser.getEmail())));
        assertThat(actualUser.getRoles().toArray(), is(equalTo(expectedUser.getRoles().toArray())));
        assertThat(actualUser.getCreatedAt(), is(notNullValue()));
        assertThat(actualUser.getPassword(), is(expectedUser.getPassword()));
        assertThat(actualUser.getUserType(), is(equalTo(expectedUserType)));
    }
}
