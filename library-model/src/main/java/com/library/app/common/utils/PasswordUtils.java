package com.library.app.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * The Password encrypting utils.
 */
public final class PasswordUtils {

    private PasswordUtils() {
    }

    /**
     * Encrypt password using SHA256.
     *
     * @param password the password
     * @return the string
     */
    public static String encryptPassword(final String password) {
        MessageDigest md = null;

        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (final NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        md.update(password.getBytes());
        return Base64.getMimeEncoder().encodeToString(md.digest());
    }

    //Use this method to pass a pasword and get the encrypted values off it
//    public static void main(final String[] args){
//        System.out.println(PasswordUtils.encryptPassword("123456"));
//    }

}