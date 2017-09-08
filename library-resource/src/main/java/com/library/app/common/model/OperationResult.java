package com.library.app.common.model;

/**
 * Class to represent Service response, it contains the entity, statuses and error codes.
 *
 * @author wilferaciolli
 */
public class OperationResult {

    // Response codes available from Service methods
    private final boolean success;
    private String errorIdentification;
    private String errorDescription;
    private Object entity;

    /**
     * Private constructor that takes an entity as parameter, therefore it was a success method call.
     *
     * @param entity The entity added.
     */
    private OperationResult(final Object entity) {
        this.success = true;
        this.entity = entity;
    }

    /**
     * Private constructor that takes error message and description as parameters, therefore it was an unsuccessful
     * method call.
     *
     * @param errorIdentification The error identification.
     * @param errorDescription    The error description.
     */
    private OperationResult(final String errorIdentification, final String errorDescription) {
        this.success = false;
        this.errorIdentification = errorIdentification;
        this.errorDescription = errorDescription;
    }

    /**
     * Public method to define that the call was a success and it was passed an category as parameter.
     *
     * @param entity The entity parameter.
     * @return The entity.
     */
    public static OperationResult success(final Object entity) {
        return new OperationResult(entity);
    }

    /**
     * Public method to define that the call was a success but without an entity as parameter.
     *
     * @return success.
     */
    public static OperationResult success() {
        return new OperationResult(null);
    }

    /**
     * Public method to define that the call was an error and it takes the error details as prameters.
     *
     * @param errorIdentification The error identification.
     * @param errorDescription    The error description.
     * @return The Error.
     */
    public static OperationResult error(final String errorIdentification, final String errorDescription) {
        return new OperationResult(errorIdentification, errorDescription);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public Object getEntity() {
        return entity;
    }

    public String getErrorIdentification() {
        return errorIdentification;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public String toString() {
        return "OperationResult [success=" + success + ", errorIdentification=" + errorIdentification
                + ", errorDescription=" + errorDescription + ", entity=" + entity + "]";
    }

}
