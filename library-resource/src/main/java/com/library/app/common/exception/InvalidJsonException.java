package com.library.app.common.exception;

/**
 * Exception to be thrown for invalid JSOn format.
 *
 * @author wilferaciolli
 */
public class InvalidJsonException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor to take a error message;
     *
     * @param message The message.
     */
    public InvalidJsonException(final String message) {
        super(message);
    }

    /**
     * Constructor to take a generic throwable Json InvalidJsonException.
     *
     * @param throwable The class it was thrown.
     */
    public InvalidJsonException(final Throwable throwable) {
        super(throwable);
    }
}
