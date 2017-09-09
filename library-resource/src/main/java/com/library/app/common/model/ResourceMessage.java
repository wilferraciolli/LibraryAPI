package com.library.app.common.model;

/**
 * Class to define error messages used for HTTP Response codes. This will handle the type of messages that the
 * application will handle. Theses keys and messages will handle the formatting of error messages. These keys and
 * messages are the same declared on the json files in src/test/resources/json.
 *
 * @author wilferaciolli
 */
public class ResourceMessage {
    // the name of the resource Eg Category
    private final String resource;

    private static final String KEY_EXISTENT = "%s.existent";
    private static final String MESSAGE_EXISTENT = "There is already a %s for the given %s";
    private static final String MESSAGE_INVALID_FIELD = "%s.invalidField.%s";
    private static final String KEY_NOT_FOUND = "%s.NotFound";
    private static final String MESSAGE_NOT_FOUND = "%s not found";
    private static final String NOT_FOUND = "Not found";

    /**
     * Instantiates a new Resource message. Helper methods to handle error messages that will be displayed to the client on HTTP Rest calls
     *
     * @param resource the resource
     */
    public ResourceMessage(final String resource) {
        this.resource = resource;
    }

    /**
     * Gets key of resource existent.
     *
     * @return the key of resource existent
     */
    public String getKeyOfResourceExistent() {
        return String.format(KEY_EXISTENT, resource);
    }

    /**
     * Gets message of resource existent.
     *
     * @param fieldsNames the fields names
     * @return the message of resource existent
     */
    public String getMessageOfResourceExistent(final String fieldsNames) {
        return String.format(MESSAGE_EXISTENT, resource, fieldsNames);
    }

    /**
     * Gets key of invalid field.
     *
     * @param invalidField the invalid field
     * @return the key of invalid field
     */
    public String getKeyOfInvalidField(final String invalidField) {
        return String.format(MESSAGE_INVALID_FIELD, resource, invalidField);
    }

    /**
     * Gets key of resource not found.
     *
     * @return the key of resource not found
     */
    public String getKeyOfResourceNotFound() {
        return String.format(KEY_NOT_FOUND, resource);
    }

    /**
     * Gets message of resource not found.
     *
     * @return the message of resource not found
     */
    public String getMessageOfResourceNotFound() {
        return String.format(MESSAGE_NOT_FOUND, resource);
    }

    /**
     * Gets message not found.
     *
     * @return the message not found
     */
    public String getMessageNotFound() {
        return NOT_FOUND;
    }
}
