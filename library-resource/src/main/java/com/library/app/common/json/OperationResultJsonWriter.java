package com.library.app.common.json;

import com.google.gson.JsonObject;
import com.library.app.common.model.OperationResult;

/**
 * Class to write the operation result.
 *
 * @author wilferaciolli
 */
public class OperationResultJsonWriter {

    /**
     * Private constructor to not allow the class to be instantiated.
     */
    private OperationResultJsonWriter() {
    }

    /**
     * Method to get the Json Operation result. Success or error. If success it return the Json string otherwise returns
     * empty string.
     *
     * @param operationResult The operation result.
     * @return The operation result.
     */
    public static String toJson(final OperationResult operationResult) {

        // check if the operation result was empty by calling JsonWriter.writeToString
        return JsonWriter.writeToString(getJsonObject(operationResult));
    }

    /**
     * Method to determine if the Json was successful or an error ocuured. Then it passes the error messages to the
     * Error message handler class.
     */
    private static Object getJsonObject(final OperationResult operationResult) {
        // checks whether the operation succeeded
        if (operationResult.isSuccess()) {
            return getJsonSuccess(operationResult);
        }
        return getJsonError(operationResult);
    }

    /**
     * Get operation entity result if the JSON conversion was successful.
     *
     * @param operationResult
     * @return Json conversion success.
     */
    private static Object getJsonSuccess(final OperationResult operationResult) {
        return operationResult.getEntity();
    }

    /**
     * Get operation entity result if the JSON conversion was errored. Get the error message code and description.
     *
     * @param operationResult
     * @return Json conversion errored
     */
    private static JsonObject getJsonError(final OperationResult operationResult) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("errorIdentification", operationResult.getErrorIdentification());
        jsonObject.addProperty("errorDescription", operationResult.getErrorDescription());

        return jsonObject;
    }
}
