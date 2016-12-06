package com.library.app.category.resource;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.app.common.json.JsonReader;

/**
 * Class to Convert Json to Java object. Using GSON Google Library. This class is to be called once and dies therefore
 * ApplicationScoped annotation is added to the class. Then this class can be injected to it will have only one instance
 * of the class.
 *
 * @author wilferaciolli
 */
@ApplicationScoped
public class CategoryJsonConverter {

    /**
     * Method to get a Json String, convert it onto a JsonObject then convert it onto a Java Object. It is
     *
     * @param json The Json String.
     * @return The category from the Json string.
     */
    public Category convertFrom(final String json) {
        // get the json string and convert into JSON object
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        // Create a java object to be populated from the JSON
        final Category category = new Category();
        category.setName(JsonReader.getStringOrNull(jsonObject, "name"));

        return category;
    }

//    /**
//     * Method to get a java object and convert into Json object.
//     *
//     * @param category The java catgory object
//     * @return The Json object (serialized)
//     */
//    public JsonElement convertToJsonElement(final Category category) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("id", category.getId());
//        jsonObject.addProperty("name", category.getName());
//
//        return jsonObject;
//    }
//
//    /**
//     * Method to take a list of java categories object and serialise into Json.
//     *
//     * @param categories List of categories
//     * @return list of categories in Json
//     */
//    public JsonElement convertToJsonElement(final List<Category> categories) {
//        JsonArray jsonArray = new JsonArray();
//
//        // conver5t each category into a json andd add to its array
//        for (Category category : categories) {
//            jsonArray.add(convertToJsonElement(category));
//        }
//
//        return jsonArray;
//    }
}
