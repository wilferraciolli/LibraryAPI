package com.library.app.common.model;

import com.library.app.common.exception.FieldNotValidException;

/**
 * Class to help using resources for HTTP REST error messages. This class will get the error messages code (key) and
 * error messages content (text) and help to pass to Resource Message to be handled.
 *
 * @author wilferaciolli
 */
public class StandardsOperationResults {

    private StandardsOperationResults() {
    }

    /**
     * Gets the Field name where the error message happened. This is normally to identify a duplicated category name.
     *
     * @param resourceMessage The error message.
     * @param fieldsNames     The fields where the error occured.s
     * @return Error message
     */
    public static OperationResult getOperationResultExistent(final ResourceMessage resourceMessage,
                                                             final String fieldsNames) {
        return OperationResult.error(resourceMessage.getKeyOfResourceExistent(),
                resourceMessage.getMessageOfResourceExistent(fieldsNames));
    }

    /**
     * Method to get the field-error-message(violation) and the exception thrown to convert into an error message.
     *
     * @param resourceMessage The error message.
     * @param ex              FieldNotValidException
     * @return Error message
     */
    public static OperationResult getOperationResultInvalidField(final ResourceMessage resourceMessage,
                                                                 final FieldNotValidException ex) {
        return OperationResult.error(resourceMessage.getKeyOfInvalidField(ex.getFieldName()), ex.getMessage());
    }

    /**
     * Method to identify that the category name passed was not found and to convert into an error message.
     *
     * @param resourceMessage The error message.
     * @return Error message
     */
    public static OperationResult getOperationResultNotFound(final ResourceMessage resourceMessage) {
        return OperationResult.error(resourceMessage.getKeyOfResourceNotFound(),
                resourceMessage.getMessageOfResourceNotFound());
    }
}
