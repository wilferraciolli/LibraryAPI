package com.library.app.category.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.category.model.Category;
import com.library.app.common.json.EntityJsonConverter;
import com.library.app.common.json.JsonReader;

import javax.enterprise.context.ApplicationScoped;

/**
 * Class to Convert Json to Java object. Using GSON Google Library. This class is to be called once and dies therefore
 * ApplicationScoped annotation is added to the class. Then this class can be injected to it will have only one instance
 * of the class.
 *
 * @author wilferaciolli
 */
@ApplicationScoped
public class CategoryJsonConverter implements EntityJsonConverter<Category> {

    @Override
    public Category convertFrom(final String json) {
        // get the json string and convert into JSON object
        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);

        // Create a java object to be populated from the JSON
        final Category category = new Category();
        category.setName(JsonReader.getStringOrNull(jsonObject, "name"));

        return category;
    }

    @Override
    public JsonElement convertToJsonElement(final Category category) {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", category.getId());
        jsonObject.addProperty("name", category.getName());

        return jsonObject;
    }
}
