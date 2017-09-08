package com.library.app.common.model;

/**
 * Enum to hold HTTP response codes.
 *
 * @author wilferaciolli
 */
public enum HttpCode {

    /**
     * Resource created, normally from a POST method.
     */
    CREATED(201),

    /**
     * Resource failed either to validate or bad data, normally POST/PUT method.
     */
    VALIDATION_ERROR(422),

    /**
     * Resource not found. Could be GET/POST/PUT/DELETE.
     */
    NOT_FOUND(404),

    /**
     * Resource success, Normally a GET.
     */
    OK(200),
    /**
     * Forbidden http code.
     */
    FORBIDDEN(403),
    /**
     * Internal error http code.
     */
    INTERNAL_ERROR(500),
    /**
     * Unauthorized http code.
     */
    UNAUTHORIZED(401);

    private final int code;

    private HttpCode(final int code) {
        this.code = code;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }
}
