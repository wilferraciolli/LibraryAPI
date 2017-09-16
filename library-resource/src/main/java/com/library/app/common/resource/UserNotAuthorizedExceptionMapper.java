package com.library.app.common.resource;

import com.library.app.common.exception.UserNotAuthorizedException;
import com.library.app.common.model.HttpCode;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * The type User not authorized exception mapper. This is a class that intercept <@link UserNotAuthorizedException></@link>
 * and set the response to forbiddenstatus code..
 */
@Provider
public class UserNotAuthorizedExceptionMapper implements ExceptionMapper<UserNotAuthorizedException> {

    @Override
    public Response toResponse(final UserNotAuthorizedException exception) {
        //if UserNotAuthorizedException is thrown then
        return Response.status(HttpCode.FORBIDDEN.getCode()).build();
    }
}
