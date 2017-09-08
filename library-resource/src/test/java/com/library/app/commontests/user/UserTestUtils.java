package com.library.app.commontests.user;

/**
 * The type User test utils.
 */
public final class UserTestUtils {

    private UserTestUtils() {
    }

    /**
     * Gets user with password JSON.
     *
     * @param password the password
     * @return the json with password
     */
    public static String getJsonWithPassword(final String password) {
        return String.format("{\"password\":\"%s\"}", password);
    }

    /**
     * Gets user with email and password JSON.
     *
     * @param email    the email
     * @param password the password
     * @return the json with email and password
     */
    public static String getJsonWithEmailAndPassword(final String email, final String password) {
        return String.format("{\"email\":\"%s\",\"password\":\"%s\"}", email, password);
    }


}