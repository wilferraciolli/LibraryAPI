package com.library.app.commontests.user;

/**
 * The type User test utils.
 */
public final class UserTestUtils {

    private UserTestUtils() {
    }

    /**
     * Gets json with password.
     *
     * @param password the password
     * @return the json with password
     */
    public static String getJsonWithPassword(final String password) {
        return String.format("{\"password\":\"%s\"}", password);
    }

}