package com.library.app.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.common.model.PaginatedData;

/**
 * Class responsible for writing to the Java class.
 *
 * @author wilferaciolli
 */
public class JsonUtils {

    /**
     * Private constructor to not allow the class to be instantiated.
     */
    private JsonUtils() {
    }

    /**
     * Method to get the json property value by passing its id.
     *
     * @param id The id.
     * @return The value of the property id.
     */
    public static JsonElement getJsonElementWithId(final Long id) {
        final JsonObject idJson = new JsonObject();
        idJson.addProperty("id", id);

        return idJson;
    }

    /**
     * Gets json element with paging and entries. Method to wrap the json into a root,This is the outer wrap of the json response.
     * It will count how many records there are and display it on the response.
     *
     * @param <T>                 the type parameter
     * @param paginatedData       the paginated data
     * @param entityJsonConverter the entity json converter
     * @return the json element with paging and entries
     */
    public static <T> JsonElement getJsonElementWithPagingAndEntries(final PaginatedData<T> paginatedData, final EntityJsonConverter<T> entityJsonConverter) {
        final JsonObject jsonWithEntriesAndPaging = new JsonObject();

        final JsonObject jsonPaging = new JsonObject();
        jsonPaging.addProperty("totalRecords", paginatedData.getNumberOfRows());

        jsonWithEntriesAndPaging.add("paging", jsonPaging);
        jsonWithEntriesAndPaging.add("entries", entityJsonConverter.convertToJsonElement(paginatedData.getRows()));

        return jsonWithEntriesAndPaging;

        /**
         //         * Example
         //         * {
         //         * "paging": {
         //         * "totalRecords": 2
         //         * },
         //         * "entries": [
         //         * {
         //         * "id": 1,
         //         * "name": "Java"
         //         * },
         //         * {
         //         * "id": 2,
         //         * "name": "Networks"
         //         * }
         //         * ]
         //         * }
         //         */
    }

}
