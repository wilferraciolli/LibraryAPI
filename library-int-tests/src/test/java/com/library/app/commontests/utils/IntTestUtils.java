package com.library.app.commontests.utils;

import org.junit.Ignore;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ws.rs.core.Response;

import org.junit.Ignore;

import com.library.app.common.model.HttpCode;

/**
 * Integration test utils class. This class contain helper methods for integration tests classes.
 * Created by wilferaciolli on 13/01/2017.
 */
@Ignore
public class IntTestUtils {

    /**
     * Helper method to create a category and get its newly created id.
     * @param resourceClient The resource client.
     * @param pathResource The path to the resource.
     * @param mainFolder The main folder.
     * @param fileName The file name.
     * @return The newly created id.
     */
    public static Long addElementWithFileAndGetId(final ResourceClient resourceClient, final String pathResource,
                                                  final String mainFolder, final String fileName) {
        final Response response = resourceClient.resourcePath(pathResource).postWithFile(
                getPathFileRequest(mainFolder, fileName));
        return assertResponseIsCreatedAndGetId(response);
    }

    /**
     * Helper method to check that the response was OK and return it as String.
     * @param resourceClient The resource client.
     * @param pathResource The path to the resource.
     * @param id the id of the category.
     * @return The response as string.
     */
    public static String findById(final ResourceClient resourceClient, final String pathResource, final Long id) {
        final Response response = resourceClient.resourcePath(pathResource + "/" + id).get();

        //check that response was OK
        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
        return response.readEntity(String.class);
    }

    /**
     * helper method to assert that a category was created and to get its newly created id.
     * @param response The response.
     * @return The id of the newly created category.
     */
    private static Long assertResponseIsCreatedAndGetId(final Response response) {
        //check if the response was OK
        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));

        //check that it has an id
        final Long id = JsonTestUtils.getIdFromJson(response.readEntity(String.class));
        assertThat(id, is(notNullValue()));
        return id;
    }
}
