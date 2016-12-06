package com.library.app.commontests.utils;

import java.io.InputStream;
import java.util.Scanner;

import org.json.JSONException;
import org.junit.Ignore;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.google.gson.JsonObject;


/**
 * Helper Class to to test reading and comparing Json file contents. This class will hold two static methods.
 *
 * @author wilferaciolli
 */
@Ignore
public class JsonTestUtils {

    public static final String BASE_JSON_DIR = "json/";

    /**
     * private constructor not allowing this class to be instantiated.
     */
    private JsonTestUtils() {
    }

    /**
     * Method to read JSON file, it takes the file path, reads and formats it.
     *
     * @param relativePath The file path and name.
     * @return Json file content formatted.
     */
    public static String readJsonFile(final String relativePath) {
        final InputStream is = JsonTestUtils.class.getClassLoader().getResourceAsStream(BASE_JSON_DIR + relativePath);

        // use the scanner to go through the content of the file
        try (Scanner s = new Scanner(is)) {
            return s.useDelimiter("\\A").hasNext() ? s.next() : "";
        }
    }

    /**
     * Method to get a JSON file read its content and analyse against a Json response in CategoryresourceUTest.java.
     */
    public static void assertJsonMatchesFileContent(final String actualJson, final String fileNameWithExpectedJson) {
        assertJsonMatchesExpectedJson(actualJson, readJsonFile(fileNameWithExpectedJson));
    }

    /**
     * Method to compare the JSON file content. It compare if both JSON passed are the same, it checks for the content
     * and the number of fields.
     *
     * @param actualJson   The json expected to be.
     * @param expectedJson The Json read from the file. *
     */
    public static void assertJsonMatchesExpectedJson(final String actualJson, final String expectedJson) {
        try {
            JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
        } catch (final JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

//    /**
//     * Method to extract id from an json object.
//     *
//     * @param json The json.
//     * @return The id extracted from the json.
//     */
//    public static Long getIdFromJson(final String json) {
//        final JsonObject jsonObject = JsonReader.readAsJsonObject(json);
//        return JsonReader.getLongOrNull(jsonObject, "id");
//    }
}
