package com.library.app.common.json;

import com.google.gson.Gson;

/**
 * Helper final class to write Json. This class is used in conjunction to OperationResultJsonWriter because of its
 * private methods. It takes a json object and writes it to a string.
 *
 * @author wilferaciolli
 */
public final class JsonWriter {

    /**
     * Private constructor.
     */
    private JsonWriter() {

    }

    /**
     * Method to get a Json content and write it to a String.
     *
     * @param object The json object.
     * @return
     */
    public static String writeToString(final Object object) {
        // check if null and return empty
        if (object == null) {
            return "";
        }
        // return json
        return new Gson().toJson(object);
    }
}
