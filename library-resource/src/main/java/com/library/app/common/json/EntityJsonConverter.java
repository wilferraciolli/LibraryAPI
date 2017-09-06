package com.library.app.common.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * The interface Entity json converter. It is used to convert from Json to Entity or vice-versa.
 * This interface uses the java 8 default implementation method.
 *
 * @param <T> the type parameter
 */
public interface EntityJsonConverter<T> {
    /**
     * Method to get a Json String, convert it onto a JsonObject then convert it onto a Java Object.
     *
     * @param json the json
     * @return the t
     */
    T convertFrom(String json);

    /**
     * Method to get a java object and convert into Json object.
     *
     * @param entity the entity
     * @return the json element
     */
    JsonElement convertToJsonElement(T entity);

    /**
     * Convert objects to json element. This is the default implementation method that Java 8 accepts.
     *
     * @param entities the entities
     * @return the json element
     */
    default JsonElement convertToJsonElement(final List<T> entities) {
        final JsonArray jsonArray = new JsonArray();

        for (final T entity : entities) {
            jsonArray.add(convertToJsonElement(entity));
        }

        return jsonArray;
    }

}
