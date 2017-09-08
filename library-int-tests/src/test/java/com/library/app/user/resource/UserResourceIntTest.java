package com.library.app.user.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.ArquillianTestUtils;
import com.library.app.commontests.utils.IntTestUtils;
import com.library.app.commontests.utils.ResourceClient;
import com.library.app.commontests.utils.ResourceDefinitions;
import com.library.app.user.model.User;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.net.URL;

import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
import static com.library.app.commontests.user.UserForTestsRepository.mary;
import static com.library.app.commontests.user.UserTestUtils.getJsonWithEmailAndPassword;
import static com.library.app.commontests.user.UserTestUtils.getJsonWithPassword;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * The type User resource int test.
 */
@RunWith(Arquillian.class)
public class UserResourceIntTest {

    @ArquillianResource
    private URL deploymentUrl;

    private ResourceClient resourceClient;

    private static final String PATH_RESOURCE = ResourceDefinitions.USER.getResourceName();

    /**
     * Create deployment web archive.
     *
     * @return the web archive
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ArquillianTestUtils.createDeploymentArchive();
    }

    /**
     * Init test case.
     */
    @Before
    public void initTestCase() {
        resourceClient = new ResourceClient(deploymentUrl);

        //add end points
        resourceClient.resourcePath("DB/").delete();
        resourceClient.resourcePath("DB/" + PATH_RESOURCE + "/admin").postWithContent("");
    }

    /**
     * Add valid customer and find it.
     */
    @Test
    @RunAsClient
    public void addValidCustomerAndFindIt() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");

        findUserAndAssertResponseWithUser(userId, johnDoe());
    }

    /**
     * Add user with null name.
     */
    @Test
    @RunAsClient
    public void addUserWithNullName() {
        addUserWithValidationError("customerWithNullName.json", "userErrorNullName.json");
    }

    /**
     * Add existent user.
     */
    @Test
    @RunAsClient
    public void addExistentUser() {
        addUserAndGetId("customerJohnDoe.json");
        addUserWithValidationError("customerJohnDoe.json", "userAlreadyExists.json");
    }

    /**
     * Update valid customer as admin.
     */
    @Test
    @RunAsClient
    public void updateValidCustomerAsAdmin() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        findUserAndAssertResponseWithUser(userId, johnDoe());

        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + userId).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoeWithNewName.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        final User expectedUser = johnDoe();
        expectedUser.setName("New name");
        findUserAndAssertResponseWithUser(userId, expectedUser);
    }

    /**
     * Update valid logged customer as customer.
     */
    @Test
    @RunAsClient
    public void updateValidLoggedCustomerAsCustomer() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        findUserAndAssertResponseWithUser(userId, johnDoe());

        final Response response = resourceClient.user(johnDoe()).resourcePath(PATH_RESOURCE + "/" + userId)
                .putWithFile(
                        getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoeWithNewName.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        resourceClient.user(admin());
        final User expectedUser = johnDoe();
        expectedUser.setName("New name");
        findUserAndAssertResponseWithUser(userId, expectedUser);
    }

    /**
     * Update customer but not the logged customer.
     */
    @Test
    @RunAsClient
    public void updateCustomerButNotTheLoggedCustomer() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        findUserAndAssertResponseWithUser(userId, johnDoe());
        addUserAndGetId("customerMary.json");

        final Response response = resourceClient.user(mary()).resourcePath(PATH_RESOURCE + "/" + userId).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoeWithNewName.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

    /**
     * Update valid customer trying to change type.
     */
    @Test
    @RunAsClient
    public void updateValidCustomerTryingToChangeType() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        findUserAndAssertResponseWithUser(userId, johnDoe());

        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + userId).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoeWithNewType.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.INTERNAL_ERROR.getCode())));
    }

    /**
     * Update valid customer trying to change password.
     */
    @Test
    @RunAsClient
    public void updateValidCustomerTryingToChangePassword() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        findUserAndAssertResponseWithUser(userId, johnDoe());

        assertThat(authenticate(johnDoe().getEmail(), johnDoe().getPassword()), is(equalTo(true)));
        assertThat(authenticate(johnDoe().getEmail(), "111111"), is(equalTo(false)));

        final Response response = resourceClient.user(johnDoe()).resourcePath(PATH_RESOURCE + "/" + userId)
                .putWithFile(
                        getPathFileRequest(PATH_RESOURCE, "updateCustomerJohnDoeWithNewPassword.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        assertThat(authenticate(johnDoe().getEmail(), johnDoe().getPassword()), is(equalTo(true)));
        assertThat(authenticate(johnDoe().getEmail(), "111111"), is(equalTo(false)));
    }

    /**
     * Update user with email belonging to other user.
     */
    @Test
    @RunAsClient
    public void updateUserWithEmailBelongingToOtherUser() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        addUserAndGetId("customerMary.json");

        final Response responseUpdate = resourceClient.user(admin()).resourcePath(PATH_RESOURCE + "/" + userId)
                .putWithFile(
                        getPathFileRequest(PATH_RESOURCE, "customerMary.json"));
        assertThat(responseUpdate.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(responseUpdate, "userAlreadyExists.json");
    }

    /**
     * Update user not found.
     */
    @Test
    @RunAsClient
    public void updateUserNotFound() {
        final Response response = resourceClient.user(admin()).resourcePath(PATH_RESOURCE + "/" + 999).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "customerJohnDoe.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Update password as admin.
     */
    @Test
    @RunAsClient
    public void updatePasswordAsAdmin() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");

        assertThat(authenticate(johnDoe().getEmail(), johnDoe().getPassword()), is(equalTo(true)));
        assertThat(authenticate(johnDoe().getEmail(), "111111"), is(equalTo(false)));

        final Response response = resourceClient.user(admin()).resourcePath(PATH_RESOURCE + "/" + userId + "/password")
                .putWithContent(getJsonWithPassword("111111"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        assertThat(authenticate(johnDoe().getEmail(), johnDoe().getPassword()), is(equalTo(false)));
        assertThat(authenticate(johnDoe().getEmail(), "111111"), is(equalTo(true)));
    }

    /**
     * Update password logged customer as customer.
     */
    @Test
    @RunAsClient
    public void updatePasswordLoggedCustomerAsCustomer() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");

        assertThat(authenticate(johnDoe().getEmail(), johnDoe().getPassword()), is(equalTo(true)));
        assertThat(authenticate(johnDoe().getEmail(), "111111"), is(equalTo(false)));

        final Response response = resourceClient.user(johnDoe())
                .resourcePath(PATH_RESOURCE + "/" + userId + "/password")
                .putWithContent(getJsonWithPassword("111111"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        assertThat(authenticate(johnDoe().getEmail(), johnDoe().getPassword()), is(equalTo(false)));
        assertThat(authenticate(johnDoe().getEmail(), "111111"), is(equalTo(true)));
    }

    /**
     * Update password but not the logged customer.
     */
    @Test
    @RunAsClient
    public void updatePasswordButNotTheLoggedCustomer() {
        final Long userId = addUserAndGetId("customerJohnDoe.json");
        addUserAndGetId("customerMary.json");

        final Response response = resourceClient.user(mary()).resourcePath(PATH_RESOURCE + "/" + userId + "/password")
                .putWithContent(getJsonWithPassword("111111"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.FORBIDDEN.getCode())));
    }

    /**
     * Find user by id not found.
     */
    @Test
    @RunAsClient
    public void findUserByIdNotFound() {
        final Response response = resourceClient.user(admin()).resourcePath(PATH_RESOURCE + "/" + 999).get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Find by filter paginating and ordering descending by name.
     */
    @Test
    @RunAsClient
    public void findByFilterPaginatingAndOrderingDescendingByName() {
        resourceClient.resourcePath("DB/").delete();
        resourceClient.resourcePath("DB/" + PATH_RESOURCE).postWithContent("");
        resourceClient.user(admin());

        // first page
        Response response = resourceClient.resourcePath(PATH_RESOURCE + "?page=0&per_page=2&sort=-name").get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertResponseContainsTheUsers(response, 3, mary(), johnDoe());

        // second page
        response = resourceClient.resourcePath(PATH_RESOURCE + "?page=1&per_page=2&sort=-name").get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertResponseContainsTheUsers(response, 3, admin());
    }

    /**
     * Add user with validation error.
     *
     * @param requestFileName  the request file name
     * @param responseFileName the response file name
     */
    private void addUserWithValidationError(final String requestFileName, final String responseFileName) {
        final Response response = resourceClient.user(null).resourcePath(PATH_RESOURCE)
                .postWithFile(getPathFileRequest(PATH_RESOURCE, requestFileName));
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, responseFileName);
    }

    /**
     * Assert response contains the users.
     *
     * @param response             the response
     * @param expectedTotalRecords the expected total records
     * @param expectedUsers        the expected users
     */
    private void assertResponseContainsTheUsers(final Response response, final int expectedTotalRecords,
                                                final User... expectedUsers) {

        final JsonArray usersList = IntTestUtils.assertJsonHasTheNumberOfElementsAndReturnTheEntries(response,
                expectedTotalRecords, expectedUsers.length);

        for (int i = 0; i < expectedUsers.length; i++) {
            final User expectedUser = expectedUsers[i];
            assertThat(usersList.get(i).getAsJsonObject().get("name").getAsString(),
                    is(equalTo(expectedUser.getName())));
        }
    }

    /**
     * Authenticate boolean.
     *
     * @param email    the email
     * @param password the password
     * @return the boolean
     */
    private boolean authenticate(final String email, final String password) {
        final Response response = resourceClient.user(null).resourcePath(PATH_RESOURCE + "/authenticate")
                .postWithContent(getJsonWithEmailAndPassword(email, password));
        return response.getStatus() == HttpCode.OK.getCode();
    }

    /**
     * Add user and get id long.
     *
     * @param fileName the file name
     * @return the long
     */
    private Long addUserAndGetId(final String fileName) {
        resourceClient.user(null);
        return IntTestUtils.addElementWithFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, fileName);
    }

    /**
     * Find user and assert response with user.
     *
     * @param userIdToBeFound the user id to be found
     * @param expectedUser    the expected user
     */
    private void findUserAndAssertResponseWithUser(final Long userIdToBeFound, final User expectedUser) {
        resourceClient.user(admin());

        final String bodyResponse = IntTestUtils.findById(resourceClient, PATH_RESOURCE, userIdToBeFound);
        assertResponseWithUser(bodyResponse, expectedUser);
    }

    /**
     * Assert response with user.
     *
     * @param bodyResponse the body response
     * @param expectedUser the expected user
     */
    private void assertResponseWithUser(final String bodyResponse, final User expectedUser) {
        final JsonObject userJson = JsonReader.readAsJsonObject(bodyResponse);
        assertThat(userJson.get("id").getAsLong(), is(notNullValue()));
        assertThat(userJson.get("name").getAsString(), is(equalTo(expectedUser.getName())));
        assertThat(userJson.get("email").getAsString(), is(equalTo(expectedUser.getEmail())));
        assertThat(userJson.get("type").getAsString(), is(equalTo(expectedUser.getUserType().toString())));
        assertThat(userJson.get("createdAt").getAsString(), is(notNullValue()));

        final JsonArray roles = userJson.getAsJsonArray("roles");
        assertThat(roles.size(), is(equalTo(expectedUser.getRoles().size())));
        for (int i = 0; i < roles.size(); i++) {
            final String actualRole = roles.get(i).getAsJsonPrimitive().getAsString();
            final String expectedRole = expectedUser.getRoles().get(i).toString();
            assertThat(actualRole, is(equalTo(expectedRole)));
        }
    }

    /**
     * Assert json response with file.
     *
     * @param response the response
     * @param fileName the file name
     */
    private void assertJsonResponseWithFile(final Response response, final String fileName) {
        assertJsonMatchesFileContent(response.readEntity(String.class), getPathFileResponse(PATH_RESOURCE, fileName));
    }

}
