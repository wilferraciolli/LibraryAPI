package com.library.app.common.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
}
