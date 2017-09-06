package com.library.app.commontests.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.library.app.common.json.JsonReader;
import com.library.app.common.model.HttpCode;
import org.junit.Ignore;

import javax.ws.rs.core.Response;

import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Integration test utils class. This class contain helper methods for integration tests classes.
 * Created by wilferaciolli on 13/01/2017.
 */
@Ignore
public class IntTestUtils {

    /**
     * Helper method to create a category and get its newly created id.
     *
     * @param resourceClient The resource client.
     * @param pathResource   The path to the resource.
     * @param mainFolder     The main folder.
     * @param fileName       The file name.
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
     *
     * @param resourceClient The resource client.
     * @param pathResource   The path to the resource.
     * @param id             the id of the category.
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

    /**
     * Assert json has the number of elements and return the entries json array.
     *
     * @param response                   the response
     * @param expectedTotalRecords       the expected total records
     * @param expectedEntriesForThisPage the expected entries for this page
     * @return the json array
     */
    public static JsonArray assertJsonHasTheNumberOfElementsAndReturnTheEntries(final Response response,
                                                                                final int expectedTotalRecords, final int expectedEntriesForThisPage) {
        final JsonObject result = JsonReader.readAsJsonObject(response.readEntity(String.class));

        final int totalRecords = result.getAsJsonObject("paging").get("totalRecords").getAsInt();
        assertThat(totalRecords, is(equalTo(expectedTotalRecords)));

        final JsonArray entries = result.getAsJsonArray("entries");
        assertThat(entries.size(), is(equalTo(expectedEntriesForThisPage)));

        return entries;
    }
}
