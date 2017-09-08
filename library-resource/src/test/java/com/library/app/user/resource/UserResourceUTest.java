package com.library.app.user.resource;

import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.ResourceDefinitions;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.User;
import com.library.app.user.model.User.Roles;
import com.library.app.user.services.UserServices;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import java.security.Principal;

import static com.library.app.commontests.user.UserArgumentMatcher.userEq;
import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static com.library.app.commontests.user.UserForTestsRepository.mary;
import static com.library.app.commontests.user.UserForTestsRepository.userWithIdAndCreatedAt;
import static com.library.app.commontests.user.UserTestUtils.*;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesExpectedJson;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static com.library.app.commontests.utils.JsonTestUtils.readJsonFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * The type User resource unit test.
 */
public class UserResourceUTest {

    private UserResource userResource;

    @Mock
    private UserServices userServices;

    @Mock
    private UriInfo uriInfo;

    @Mock
    private SecurityContext securityContext;

    private static final String PATH_RESOURCE = ResourceDefinitions.USER.getResourceName();

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        MockitoAnnotations.initMocks(this);

        userResource = new UserResource();

        userResource.userJsonConverter = new UserJsonConverter();
        userResource.userServices = userServices;
        userResource.uriInfo = uriInfo;
        userResource.securityContext = securityContext;
    }

    /**
     * Add valid customer.
     */
    @Test
    public void addValidCustomer() {
        when(userServices.add(userEq(johnDoe()))).thenReturn(userWithIdAndCreatedAt(johnDoe(), 1L));

        final Response response = userResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "customerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

    /**
     * Add valid employee.
     */
    @Test
    public void addValidEmployee() {
        final Response response = userResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "employeeAdmin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

    /**
     * Add existent user.
     */
    @Test
    public void addExistentUser() {
        when(userServices.add(userEq(johnDoe()))).thenThrow(new UserExistentException());

        final Response response = userResource.add(readJsonFile(getPathFileRequest(PATH_RESOURCE,
                "customerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userAlreadyExists.json");
    }

    /**
     * Add user with null name.
     */
    @Test
    public void addUserWithNullName() {
        when(userServices.add((User) anyObject())).thenThrow(new FieldNotValidException("name", "may not be null"));

        final Response response = userResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "customerWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userErrorNullName.json");
    }

    /**
     * Update valid customer.
     */
    @Test
    public void updateValidCustomer() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final User expectedUser = userWithIdAndCreatedAt(johnDoe(), 1L);
        expectedUser.setPassword(null);
        verify(userServices).update(userEq(expectedUser));
    }

    /**
     * Update valid customer logged as customer to be updated.
     */
    @Test
    public void updateValidCustomerLoggedAsCustomerToBeUpdated() {
        setUpPrincipalUser(userWithIdAndCreatedAt(johnDoe(), 1L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final User expectedUser = userWithIdAndCreatedAt(johnDoe(), 1L);
        expectedUser.setPassword(null);
        verify(userServices).update(userEq(expectedUser));
    }

    /**
     * Update valid customer logged as other customer.
     */
    @Test
    public void updateValidCustomerLoggedAsOtherCustomer() {
        setUpPrincipalUser(userWithIdAndCreatedAt(mary(), 2L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

    /**
     * Update valid employee.
     */
    @Test
    public void updateValidEmployee() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateEmployeeAdmin.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        final User expectedUser = userWithIdAndCreatedAt(admin(), 1L);
        expectedUser.setPassword(null);
        verify(userServices).update(userEq(expectedUser));
    }

    /**
     * Update user with email belonging to other user.
     */
    @Test
    public void updateUserWithEmailBelongingToOtherUser() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        doThrow(new UserExistentException()).when(userServices).update(userWithIdAndCreatedAt(johnDoe(), 1L));

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userAlreadyExists.json");
    }

    /**
     * Update user with null name.
     */
    @Test
    public void updateUserWithNullName() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        doThrow(new FieldNotValidException("name", "may not be null")).when(userServices).update(
                userWithIdAndCreatedAt(johnDoe(), 1L));

        final Response response = userResource.update(1L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "customerWithNullName.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "userErrorNullName.json");
    }

    /**
     * Update user not found.
     */
    @Test
    public void updateUserNotFound() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);
        doThrow(new UserNotFoundException()).when(userServices).update(userWithIdAndCreatedAt(johnDoe(), 2L));

        final Response response = userResource.update(2L,
                readJsonFile(getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoe.json")));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Update user password.
     */
    @Test
    public void updateUserPassword() {
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(true);

        final Response response = userResource.updatePassword(1L, getJsonWithPassword("123456"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(userServices).updatePassword(1L, "123456");
    }

    /**
     * Update customer password logged as customer to be updated.
     */
    @Test
    public void updateCustomerPasswordLoggedAsCustomerToBeUpdated() {
        setUpPrincipalUser(userWithIdAndCreatedAt(johnDoe(), 1L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.updatePassword(1L, getJsonWithPassword("123456"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertThat(response.getEntity().toString(), is(equalTo("")));

        verify(userServices).updatePassword(1L, "123456");
    }

    /**
     * Update customer password logged as other customer.
     */
    @Test
    public void updateCustomerPasswordLoggedAsOtherCustomer() {
        setUpPrincipalUser(userWithIdAndCreatedAt(mary(), 2L));
        when(securityContext.isUserInRole(Roles.ADMINISTRATOR.name())).thenReturn(false);

        final Response response = userResource.updatePassword(1L, getJsonWithPassword("123456"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

    /**
     * Sets up principal user.
     *
     * @param user the user
     */
    private void setUpPrincipalUser(final User user) {
        final Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(user.getEmail());

        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(userServices.findByEmail(user.getEmail())).thenReturn(user);
    }

    /**
     * Assert json response with file.
     *
     * @param response the response
     * @param fileName the file name
     */
    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}