package com.library.app.category.resource;

import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;
import com.library.app.common.model.HttpCode;
import com.library.app.commontests.utils.JsonTestUtils;
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

import static com.library.app.commontests.category.CategoryForTestsRepository.java;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration test class. This class is run by Arquillian plugin for integration test. Arquillian will create a
 * deployable file (WAR) then it will deploy this WAR onto WildFly and run the tests against its deployment.
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
        // get the content off a file
        final Response response = resourceClient.resourcePath(PATH_RESOURCE)
                .postWithFile(getPathFileRequest(PATH_RESOURCE, "category.json"));
        // assert that it was created
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));

        // get the id of the json object
        final Long id = JsonTestUtils.getIdFromJson(response.readEntity(String.class));

        // do a GET
        final Response responseGet = resourceClient.resourcePath(PATH_RESOURCE + "/" + id).get();
        // assert that the category could be retrieved by doing a GET
        assertThat(responseGet.getStatus(), is(equalTo(HttpCode.OK.getCode())));

        // read the json
        final JsonObject categoryAsJson = JsonReader.readAsJsonObject(responseGet.readEntity(String.class));
        // assert that the name of the category defined on the file is Java
        assertThat(JsonReader.getStringOrNull(categoryAsJson, "name"), is(equalTo(java().getName())));
    }
}
