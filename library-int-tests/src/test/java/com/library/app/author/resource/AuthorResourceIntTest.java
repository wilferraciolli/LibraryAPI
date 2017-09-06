package com.library.app.author.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.app.author.model.Author;
import com.library.app.common.json.JsonReader;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.ArquillianTestUtils;
import com.library.app.commontests.utils.IntTestUtils;
import com.library.app.commontests.utils.ResourceClient;
import com.library.app.commontests.utils.ResourceDefinitions;
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

import static com.library.app.commontests.author.AuthorForTestsRepository.donRoberts;
import static com.library.app.commontests.author.AuthorForTestsRepository.erichGamma;
import static com.library.app.commontests.author.AuthorForTestsRepository.jamesGosling;
import static com.library.app.commontests.author.AuthorForTestsRepository.johnBrant;
import static com.library.app.commontests.author.AuthorForTestsRepository.johnVlissides;
import static com.library.app.commontests.author.AuthorForTestsRepository.joshuaBloch;
import static com.library.app.commontests.author.AuthorForTestsRepository.kentBeck;
import static com.library.app.commontests.author.AuthorForTestsRepository.martinFowler;
import static com.library.app.commontests.author.AuthorForTestsRepository.ralphJohnson;
import static com.library.app.commontests.author.AuthorForTestsRepository.richardHelm;
import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;
import static com.library.app.commontests.author.AuthorForTestsRepository.williamOpdyke;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * The type Author resource int test.
 */
@RunWith(Arquillian.class)
public class AuthorResourceIntTest {

    @ArquillianResource
    private URL url;

    private ResourceClient resourceClient;

    //get the authors path
    private static final String PATH_RESOURCE = ResourceDefinitions.AUTHOR.getResourceName();

    /**
     * Create deployment web archive. This creates a war file with for integration tests.
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
        this.resourceClient = new ResourceClient(url);

        resourceClient.resourcePath("/DB").delete();
    }

    /**
     * Add valid author and find it.
     */
    @Test
    @RunAsClient
    public void addValidAuthorAndFindIt() {
        final Long authorId = addAuthorAndGetId("robertMartin.json");
        findAuthorAndAssertResponseWithAuthor(authorId, robertMartin());
    }

    /**
     * Add author with null name.
     */
    @Test
    @RunAsClient
    public void addAuthorWithNullName() {
        final Response response = resourceClient.resourcePath(PATH_RESOURCE).postWithFile(
                getPathFileRequest(PATH_RESOURCE, "authorWithNullName.json"));

        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "authorErrorNullName.json");
    }

    /**
     * Update valid author.
     */
    @Test
    @RunAsClient
    public void updateValidAuthor() {
        final Long authorId = addAuthorAndGetId("robertMartin.json");
        findAuthorAndAssertResponseWithAuthor(authorId, robertMartin());

        final Response responseUpdate = resourceClient.resourcePath(PATH_RESOURCE + "/" + authorId).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "uncleBob.json"));
        assertThat(responseUpdate.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        final Author uncleBob = new Author();
        uncleBob.setName("Uncle Bob");
        findAuthorAndAssertResponseWithAuthor(authorId, uncleBob);
    }

    /**
     * Update author not found.
     */
    @Test
    @RunAsClient
    public void updateAuthorNotFound() {
        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + 999).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "robertMartin.json"));
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Find author not found.
     */
    @Test
    @RunAsClient
    public void findAuthorNotFound() {
        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + 999).get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Find by filter paginating and ordering descending by name.
     */
    @Test
    @RunAsClient
    public void findByFilterPaginatingAndOrderingDescendingByName() {
        //generat the authors test path DB/authors
        resourceClient.resourcePath("DB/" + PATH_RESOURCE).postWithContent("");

        // first page - add filters
        Response response = resourceClient.resourcePath(PATH_RESOURCE + "?page=0&per_page=10&sort=-name").get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertResponseContainsTheAuthors(response, 12, williamOpdyke(), robertMartin(), richardHelm(), ralphJohnson(),
                martinFowler(), kentBeck(), joshuaBloch(), johnVlissides(), johnBrant(), jamesGosling());

        // second page
        response = resourceClient.resourcePath(PATH_RESOURCE + "?page=1&per_page=10&sort=-name").get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        assertResponseContainsTheAuthors(response, 12, erichGamma(), donRoberts());
    }

    /**
     * Add author and get id long.
     *
     * @param fileName the file name
     * @return the long
     */
    private Long addAuthorAndGetId(final String fileName) {
        return IntTestUtils.addElementWithFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, fileName);
    }

    /**
     * Find and assert.
     *
     * @param authorIdToBeFound the author id to be found
     * @param expectedAuthor    the expected author
     */
    private void findAuthorAndAssertResponseWithAuthor(final Long authorIdToBeFound, final Author expectedAuthor) {
        final String json = IntTestUtils.findById(resourceClient, PATH_RESOURCE, authorIdToBeFound);

        final JsonObject categoryAsJson = JsonReader.readAsJsonObject(json);
        assertThat(JsonReader.getStringOrNull(categoryAsJson, "name"), is(equalTo(expectedAuthor.getName())));
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

    /**
     * Assert response contains the authors.
     *
     * @param response             the response
     * @param expectedTotalRecords the expected total records
     * @param expectedAuthors      the expected authors
     */
    public void assertResponseContainsTheAuthors(final Response response, final int expectedTotalRecords,
                                                 final Author... expectedAuthors) {
        //assert response
        final JsonArray authorsList = IntTestUtils.assertJsonHasTheNumberOfElementsAndReturnTheEntries(response,
                expectedTotalRecords, expectedAuthors.length);

        //check that the response contains the author
        for (int i = 0; i < expectedAuthors.length; i++) {
            final Author expectedAuthor = expectedAuthors[i];
            assertThat(authorsList.get(i).getAsJsonObject().get("name").getAsString(),
                    is(equalTo(expectedAuthor.getName())));
        }
    }
}
