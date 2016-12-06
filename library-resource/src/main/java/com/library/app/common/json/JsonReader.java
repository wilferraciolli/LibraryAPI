package com.library.app.common.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.library.app.common.exception.InvalidJsonException;

/**
 * Class to read Json files(compressed into 1 string) and format into java variables. This class will have helper
 * methods to convert Json.
 *
 * @author wilferaciolli
 */
public class JsonReader {

    /**
     * Method to convert a String into a Json Objt. Once the Json is an object then its attributes(values) can be
     * accessed.
     *
     * @param json The String Json representation.
     * @return The Json object.
     * @throws InvalidJsonException
     */
    public static JsonObject readAsJsonObject(final String json) throws InvalidJsonException {
        return readJsonAs(json, JsonObject.class);
    }

    /**
     * Method to convert a string into a Json Array.
     *
     * @param json The String containng the Json values.
     * @return Array of Json objects.
     * @throws InvalidJsonException
     */
    public static JsonArray readAsJsonArray(final String json) throws InvalidJsonException {
        return readJsonAs(json, JsonArray.class);
    }

    /**
     * Method that uses GSON library to convert a Json String into a java class. It is a generic method to take both
     * Json and the java class.
     *
     * @param json      The String containing the Json.
     * @param jsonClass The clas to convert to.
     * @return Java generic class.
     * @throws InvalidJsonException
     */
    public static <T> T readJsonAs(final String json, final Class<T> jsonClass) throws InvalidJsonException {
        // check if Json is null
        if (json == null || json.trim().isEmpty()) {
            throw new InvalidJsonException("Json String can not be null");
        }

        try {
            return new Gson().fromJson(json, jsonClass);
        } catch (final JsonSyntaxException e) {
            throw new InvalidJsonException(e);
        }
    }

    /**
     * Method to get the Json value of a given propertyName.
     *
     * @param jsonObject   The Json object.
     * @param propertyName The property name.
     * @return The value of the property name passed.
     */
    public static Long getLongOrNull(final JsonObject jsonObject, final String propertyName) {
        final JsonElement property = jsonObject.get(propertyName);
        if (isJsonElementNull(property)) {
            return null;
        }
        return property.getAsLong();
    }

    /**
     * Method to get the Json value of a given propertyName.
     *
     * @param jsonObject   The Json object.
     * @param propertyName The property name.
     * @return The value of the property name passed.
     */
    public static Integer getIntegerOrNull(final JsonObject jsonObject, final String propertyName) {
        final JsonElement property = jsonObject.get(propertyName);
        if (isJsonElementNull(property)) {
            return null;
        }
        return property.getAsInt();
    }

    /**
     * Method to get the Json value of a given propertyName.
     *
     * @param jsonObject   The Json object.
     * @param propertyName The property name.
     * @return The value of the property name passed.
     */
    public static String getStringOrNull(final JsonObject jsonObject, final String propertyName) {
        final JsonElement property = jsonObject.get(propertyName);
        if (isJsonElementNull(property)) {
            return null;
        }
        return property.getAsString();
    }

    /**
     * Method to get the Json value of a given propertyName.
     *
     * @param jsonObject   The Json object.
     * @param propertyName The property name.
     * @return The value of the property name passed.
     */
    public static Double getDoubeOrNull(final JsonObject jsonObject, final String propertyName) {
        final JsonElement property = jsonObject.get(propertyName);
        if (isJsonElementNull(property)) {
            return null;
        }
        return property.getAsDouble();
    }

    /**
     * @param element The element.
     * @return True if null or empty.
     */
    private static boolean isJsonElementNull(final JsonElement element) {
        return element == null || element.isJsonNull();
    }
}
