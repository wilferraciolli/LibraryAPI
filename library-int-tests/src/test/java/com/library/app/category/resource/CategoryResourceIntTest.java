package com.library.app.category.resource;

import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.IntTestUtils;
import com.library.app.commontests.utils.ResourceClient;
import com.library.app.commontests.utils.ResourceDefinitions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URL;

import static com.library.app.commontests.category.CategoryForTestsRepository.cleanCode;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static com.library.app.commontests.category.CategoryForTestsRepository.java;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test class. This class is run by Arquillian plugin for integration test. Arquillian will create a
 * deployable file (WAR) then it will deploy this WAR onto WildFly and run the tests against its deployment.
 * To successfully run this test class. Firstly it has to be run from the command line so WildFly can be downloaded. once it is done once then the class can be run by Run Tests...
 * The steps are:
 * Open up the terminal.
 * Navigate onto to the project Root (LibraryAPI):
 * Eg. cd "C:\Users\wilia\Documents\Udemy\DEV\LibraryAPI"
 * Then run the project:
 * mvn clean install -PintegrationTests-wildfly
 *
 * @author wilferaciolli
 */
@RunWith(Arquillian.class)
public class CategoryResourceIntTest {

    //Use Arquillian to inject the base name for the URL
    @ArquillianResource
    private URL url;

    // Add the API client
    private ResourceClient resourceClient;

    //name of the element we are using when creating the resource definition class
    private static final String PATH_RESOURCE = ResourceDefinitions.CATEGORY.getResourceName();

    /**
     * Define which archive Arquillian will generate and set its properties.This defines that all the classes under
     * com.library.app will be deployed. Then it adds the persistence.xml to configure WildFLy. Also the beans xml file
     * needs to be added for CDI to work plus adding Json and MOCKITO dependencies.
     */
    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addPackages(true, "com.library.app")
                .addAsResource("persistence-integration.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML(new File("src/test/resources/web.xml"))
                .addAsLibraries(
                        Maven.resolver().resolve("com.google.code.gson:gson:2.3.1", "org.mockito:mockito-core:1.9.5")
                                .withTransitivity().asFile());
    }

    /**
     * Method to define a new Client base url. It runs before each test method.
     */
    @Before
    public void initTestCase() {
        this.resourceClient = new ResourceClient(url);
    }

    /**
     * Method to add and find a valid category.
     */
    @Test
    @RunAsClient
    public void addValidCategoryAndFindIt() {
        //add the category and get the id from it after created
        final Long id = addCategoryAndGetId( "category.json");

        //find the category and compare to see if it is the expected value
        findCategoryAndAssertResponseWithCategory(id, java());
    }

    /**
     * Method to add a category with null name.
     */
    @Test
    @RunAsClient
    public void addCategoryWithNullName() {
        // get the content off a file
        final Response response = resourceClient.resourcePath(PATH_RESOURCE)
                .postWithFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json"));
        // assert that the response was an error on validation
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        //assert that the error message was the same on the template file
        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
    }

    /**
     * Method to test that if a duplicated category is added, category already exists exception will be thrown.
     */
    @Test
    @RunAsClient
    public void addExistentCategory() {
        //add a category (Java)
        resourceClient.resourcePath(PATH_RESOURCE).postWithFile(getPathFileRequest(PATH_RESOURCE, "category.json"));

        //Try to add Java as category again
        final Response response = resourceClient.resourcePath(PATH_RESOURCE).postWithFile(
                getPathFileRequest(PATH_RESOURCE, "category.json"));

        //assert that category already exixts validation was triggered
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    /**
     * Method to update a valid category. It creates a category called Java then updates its name to be Clean Code.
     */
    @Test
    @RunAsClient
    public void updateValidCategory() {
        //add a category
        final Long id = addCategoryAndGetId("category.json");
        //get the id of the category created
        findCategoryAndAssertResponseWithCategory(id, java());

        //update this category
        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + id).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "categoryCleanCode.json"));

        //assert that the category was updated
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        //assert that the category name is now Clean Code
        findCategoryAndAssertResponseWithCategory(id, cleanCode());
    }

    /**
     * Method to create two categories, then try to update the name of one of the categories by the name of the other already existent.
     */
    @Test
    @RunAsClient
    public void updateCategoryWithNameBelongingToOtherCategory() {
        //create Java category
        final Long javaCategoryId = addCategoryAndGetId("category.json");
        //create Clean Code category
        addCategoryAndGetId("categoryCleanCode.json");

        //try to update the Java category with the name of Clean Code
        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/" + javaCategoryId).putWithFile(
                getPathFileRequest(PATH_RESOURCE, "categoryCleanCode.json"));

        //assert that a categoryName already exists exception was thrown
        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
    }

    /**
     * Method to try to update a category that does not exists.
     */
    @Test
    @RunAsClient
    public void updateCategoryNotFound() {
        //tries to update a non existing category
        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/999").putWithFile(
                getPathFileRequest(PATH_RESOURCE, "category.json"));

        //assert that category was not found exception was thrown
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Method to try to find a non existing category.
     */
    @Test
    @RunAsClient
    public void findCategoryNotFound() {
        //try to find a non existing category
        final Response response = resourceClient.resourcePath(PATH_RESOURCE + "/999").get();

        //assert that a category was not found
        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
    }

    /**
     * Method to call the integration helper method to instantiate the resource client, make the calls and return the response.
     *
     * @param fileName The file name to read from.
     * @return The response.
     */
    private Long addCategoryAndGetId(final String fileName) {
        return IntTestUtils.addElementWithFileAndGetId(resourceClient, PATH_RESOURCE, PATH_RESOURCE, fileName);
    }

    /**
     * Compare that the client response was the same as the content of the template file.
     *
     * @param response The response.
     * @param fileName the name of the file to compare against.
     */
    private void assertJsonResponseWithFile(Response response, String fileName) {
        assertJsonMatchesFileContent(response.readEntity(String.class), getPathFileResponse(PATH_RESOURCE, fileName));
    }

    /**
     * Method to call the integration helper class to get a category and compare if it is as expected.
     * @param categoryIdToBeFound The category id to get.
     * @param expectedCategory Expected category.
     */
    private void findCategoryAndAssertResponseWithCategory(final Long categoryIdToBeFound,
                                                           final Category expectedCategory) {
        //get category from id
        final String json = IntTestUtils.findById(resourceClient, PATH_RESOURCE, categoryIdToBeFound);

        //build expected category
        final JsonObject categoryAsJson = JsonReader.readAsJsonObject(json);
        assertThat(JsonReader.getStringOrNull(categoryAsJson, "name"), is(equalTo(expectedCategory.getName())));
    }
}































