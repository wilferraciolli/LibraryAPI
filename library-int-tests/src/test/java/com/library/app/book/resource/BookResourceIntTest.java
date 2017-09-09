package com.library.app.book.resource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.library.app.author.model.Author;
import com.library.app.book.model.Book;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;
import com.library.app.common.json.JsonWriter;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.ArquillianTestUtils;
import com.library.app.commontests.utils.IntTestUtils;
import com.library.app.commontests.utils.JsonTestUtils;
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

import static com.library.app.commontests.book.BookForTestsRepository.designPatterns;
import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * The type Book resource integration test.
 */
@RunWith(Arquillian.class)
public class BookResourceIntTest {

    @ArquillianResource
    private URL deploymentUrl;

    private ResourceClient resourceClient;

    private static final String PATH_RESOURCE = ResourceDefinitions.BOOK.getResourceName();

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

        resourceClient.resourcePath("DB/").delete();

        //export all the end points to generate author and category before we can create books
        resourceClient.resourcePath("DB/" + ResourceDefinitions.USER.getResourceName()).postWithContent("");
        resourceClient.resourcePath("DB/" + ResourceDefinitions.CATEGORY.getResourceName()).postWithContent("");
        resourceClient.resourcePath("DB/" + ResourceDefinitions.AUTHOR.getResourceName()).postWithContent("");

        resourceClient.user(admin());
    }

    /**
     * Add valid book and find it.
     */
    @Test
    @RunAsClient
    public void addValidBookAndFindIt() {
        final Long bookId = addBookAndGetId(normalizeDependenciesWithRest(designPatterns()));

        findBookAndAssertResponseWithBook(bookId, designPatterns());
    }

    /**
     * Add book and get id long. Helper method to get the id
     *
     * @param book the book
     * @return the long
     */
    private Long addBookAndGetId(final Book book) {
        return IntTestUtils.addElementWithContentAndGetId(resourceClient, PATH_RESOURCE, getJsonForBook(book));
    }

    /**
     * Gets json for book. Helper method to generate a Json for a book.
     *
     * @param book the book
     * @return the json for book
     */
    private String getJsonForBook(final Book book) {
        final JsonObject bookJson = new JsonObject();
        bookJson.addProperty("title", book.getTitle());
        bookJson.addProperty("description", book.getDescription());
        bookJson.addProperty("categoryId", book.getCategory().getId());

        final JsonArray authorsIds = new JsonArray();
        for (final Author author : book.getAuthors()) {
            authorsIds.add(new JsonPrimitive(author.getId()));
        }
        bookJson.add("authorsIds", authorsIds);
        bookJson.addProperty("price", book.getPrice());
        return JsonWriter.writeToString(bookJson);
    }

    /**
     * Normalize dependencies with rest book. This is to validate the author and category before creating a new book.
     *
     * @param book the book
     * @return the book
     */
    private Book normalizeDependenciesWithRest(final Book book) {
        book.getCategory().setId(loadCategoryFromRest(book.getCategory()).getId());
        for (final Author author : book.getAuthors()) {
            author.setId(loadAuthorFromRest(author).getId());
        }
        return book;
    }

    private Category loadCategoryFromRest(final Category category) {
        final Response response = resourceClient.resourcePath(
                "DB/" + ResourceDefinitions.CATEGORY.getResourceName() + "/" + category.getName()).get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        final String bodyResponse = response.readEntity(String.class);
        return new Category(JsonTestUtils.getIdFromJson(bodyResponse));
    }

    private Author loadAuthorFromRest(final Author author) {
        final Response response = resourceClient.resourcePath(
                "DB/" + ResourceDefinitions.AUTHOR.getResourceName() + "/" + author.getName()).get();
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        final String bodyResponse = response.readEntity(String.class);
        return new Author(JsonTestUtils.getIdFromJson(bodyResponse));
    }

    private void findBookAndAssertResponseWithBook(final Long bookIdToBeFound, final Book expectedBook) {
        final String bodyResponse = IntTestUtils.findById(resourceClient, PATH_RESOURCE, bookIdToBeFound);

        final JsonObject bookJson = JsonReader.readAsJsonObject(bodyResponse);
        assertThat(bookJson.get("id").getAsLong(), is(notNullValue()));
        assertThat(bookJson.get("title").getAsString(), is(equalTo(expectedBook.getTitle())));
        assertThat(bookJson.get("description").getAsString(), is(equalTo(expectedBook.getDescription())));
        assertThat(bookJson.getAsJsonObject("category").get("name").getAsString(), is(equalTo(expectedBook
                .getCategory().getName())));

        final JsonArray authors = bookJson.getAsJsonArray("authors");
        assertThat(authors.size(), is(equalTo(expectedBook.getAuthors().size())));
        for (int i = 0; i < authors.size(); i++) {
            final String actualAuthorName = authors.get(i).getAsJsonObject().get("name").getAsString();
            final String expectedAuthorName = expectedBook.getAuthors().get(i).getName();
            assertThat(actualAuthorName, is(equalTo(expectedAuthorName)));
        }

        assertThat(bookJson.get("price").getAsDouble(), is(equalTo(expectedBook.getPrice())));
    }

}
