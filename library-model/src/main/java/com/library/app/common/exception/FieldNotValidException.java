package com.library.app.common.exception;

/**
 * Validation messages handler. This class will handle the error messages assigned to a field, it will take the field
 * where the
 * validation occurred and load the message related to that.
 *
 * @author wilferaciolli
 *
 */
public class FieldNotValidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // exception field name
    private final String fieldName;

    /**
     * Constructor to take the field name where the exception occurred and the message associated with it.
     *
     * @param fieldName
     *            The filed name the exception occurred.
     * @param message
     *            The message associated with the error.
     */
    public FieldNotValidException(final String fieldName, final String message) {
        super(message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String toString() {
        return "FieldNotValidException [fieldName=" + fieldName + "]";
    }

}
