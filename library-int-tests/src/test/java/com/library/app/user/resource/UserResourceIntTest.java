package com.library.app.user.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;
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

import java.net.URL;

import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static com.library.app.commontests.user.UserForTestsRepository.johnDoe;
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
}
