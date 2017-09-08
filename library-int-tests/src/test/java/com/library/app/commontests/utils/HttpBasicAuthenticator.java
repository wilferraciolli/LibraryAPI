package com.library.app.commontests.utils;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * The type Http basic authenticator. This class has a request filter to extract the user that is logged on making the calls to the application.
 */
public class HttpBasicAuthenticator implements ClientRequestFilter {

    private final String user;
    private final String password;

    /**
     * Instantiates a new Http basic authenticator.
     *
     * @param user     the user
     * @param password the password
     */
    public HttpBasicAuthenticator(final String user, final String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void filter(final ClientRequestContext requestContext) throws IOException {
        //intercept the headers of the call and add authentication headers to the call
        final MultivaluedMap<String, Object> headers = requestContext.getHeaders();
        headers.add("Authorization", getBasicAuthentication());
    }

    /**
     * Gets basic authentication. This class generates the user and its password.
     *
     * @return the basic authentication
     */
    private String getBasicAuthentication() {
        final String userAndPassword = this.user + ":" + this.password;
        try {
            return "Basic " + Base64.getMimeEncoder().encodeToString(userAndPassword.getBytes("UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            throw new IllegalStateException("Error while converting using UTF-8", e);
        }
    }

}