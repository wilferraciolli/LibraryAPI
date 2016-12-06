package com.library.app.common.model;

/**
 * Class to define error messages used for HTTP Response codes. This will handle the type of messages that the
 * application will handle. Theses keys and messages will handle the formatting of error messages. These keys and
 * messages are the same declared on the json files in src/test/resources/json.
 *
 * @author wilferaciolli
 *
 */
public class ResourceMessage {
    // the name of the resource Eg Category
    private final String resource;

    private static final String KEY_EXISTENT = "%s.existent";
    private static final String MESSAGE_EXISTENT = "There is already a %s for the given %s";
    private static final String MESSAGE_INVALID_FIELD = "%s.invalidField.%s";
    private static final String KEY_NOT_FOUND = "%s.NotFound";
    private static final String MESSAGE_NOT_FOUND = "%s not found";

    // Helper methods to haldle error messages that will be displayed to the client on HTTP Rest calls
    public ResourceMessage(final String resource) {
        this.resource = resource;
    }

    public String getKeyOfResourceExistent() {
        return String.format(KEY_EXISTENT, resource);
    }

    public String getMessageOfResourceExistent(final String fieldsNames) {
        return String.format(MESSAGE_EXISTENT, resource, fieldsNames);
    }

    public String getKeyOfInvalidField(final String invalidField) {
        return String.format(MESSAGE_INVALID_FIELD, resource, invalidField);
    }

    public String getKeyOfResourceNotFound() {
        return String.format(KEY_NOT_FOUND, resource);
    }

    public String getMessageOfResourceNotFound() {
        return String.format(MESSAGE_NOT_FOUND, resource);
    }
}
