package com.library.app.commontests.utils;

import org.junit.Ignore;

/**
 * Helper class to provide file name and path for testing Json file from src/test/resources.
 *
 * @author wilferaciolli
 */
@Ignore
public class FileTestNameUtils {

    private static final String PATH_REQUEST = "/request/";
    private static final String PATH_RESPONSE = "/response/";

    /**
     * private constructor not allowing this class to be instantiated.
     */
    private FileTestNameUtils() {
    }

    /**
     * Method to get the JSON file path and name
     *
     * @param mainFolder The folder name.
     * @param filenName  The file name.
     * @return The name and path of the JSON file.
     */
    public static String getPathFileRequest(final String mainFolder, final String filenName) {
        return mainFolder + PATH_REQUEST + filenName;
    }

    /**
     * Method to get the JSON file path and name
     *
     * @param mainFolder The folder name.
     * @param filenName  The file name.
     * @return The name and path of the JSON file.
     */
    public static String getPathFileResponse(final String mainFolder, final String filenName) {
        return mainFolder + PATH_RESPONSE + filenName;
    }
}
