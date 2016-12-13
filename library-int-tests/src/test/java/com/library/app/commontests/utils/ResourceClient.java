package com.library.app.commontests.utils;


import java.net.URISyntaxException;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.library.app.commontests.utils.JsonTestUtils.readJsonFile;

/**
 * Helper class use on tests. This will be the client responsible to access the category API.
 *
 * @author wilferaciolli
 */
public class ResourceClient {

    private URL urlBase;
    private String resourcePath;

    // This class will receive a base URL to act as a client for test eg http:localhost/test
    public ResourceClient(final URL urlBase) {
        this.urlBase = urlBase;
    }

    // The path to access such as '/categories/{id}'
    public ResourceClient resourcePath(final String resourcePath) {
        this.resourcePath = resourcePath;
        return this;
    }

    // Post a file by passing its name- create  category from a file
    public Response postWithFile(final String fileName) {
        return postWithContent(getRequestFromFileOrEmptyIfNullFile(fileName));
    }

    // Post a file by passing its content - create a category from content
    public Response postWithContent(final String content) {
        return buildClient().post(Entity.entity(content, MediaType.APPLICATION_JSON));
    }

    //get method
    public Response get() {
        return buildClient().get();
    }

    /**
     *   Using jax.rs.client to create requests for testing.
     * @return The Client.
     */
    private Builder buildClient() {
        final Client resourceClient = ClientBuilder.newClient();
        return resourceClient.target(getFullURL(resourcePath)).request();
    }

    private String getFullURL(final String resourcePath) {
        try {
            return this.urlBase.toURI() + "api/" + resourcePath;
        } catch (final URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private String getRequestFromFileOrEmptyIfNullFile(final String fileName) {
        if (fileName == null) {
            return "";
        }
        return readJsonFile(fileName);
    }
}