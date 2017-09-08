package com.library.app.user.resource;

import com.google.gson.JsonObject;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonReader;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.HttpCode;
import com.library.app.common.model.OperationResult;
import com.library.app.common.model.ResourceMessage;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.User;
import com.library.app.user.model.User.Roles;
import com.library.app.user.model.User.UserType;
import com.library.app.user.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import static com.library.app.common.model.StandardsOperationResults.getOperationResultExistent;
import static com.library.app.common.model.StandardsOperationResults.getOperationResultInvalidField;
import static com.library.app.common.model.StandardsOperationResults.getOperationResultNotFound;

/**
 * The type User resource. This class provides end point for CRUD operation on users.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("user");

    @Inject
    UserServices userServices;

    @Inject
    UserJsonConverter userJsonConverter;

    @Context
    SecurityContext securityContext;

    @Context
    UriInfo uriInfo;

    /**
     * Add user response.
     *
     * @param body the body
     * @return the response
     */
    @POST
    public Response add(final String body) {
        logger.debug("Adding a new user with body {}", body);

        User user = userJsonConverter.convertFrom(body);
        //user cannot be the type of employee
        if (user.getUserType().equals(UserType.EMPLOYEE)) {
            return Response.status(HttpCode.FORBIDDEN.getCode()).build();
        }

        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result;
        try {
            user = userServices.add(user);
            result = OperationResult.success(JsonUtils.getJsonElementWithId(user.getId()));
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the user is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final UserExistentException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("There is already an user for the given email", e);
            result = getOperationResultExistent(RESOURCE_MESSAGE, "email");
        }

        logger.debug("Returning the operation result after adding user: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    /**
     * Update response.
     *
     * @param id   the id
     * @param body the body
     * @return the response
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the user {} with body {}", id, body);

        //only administrators or owners can update a user
        if (!securityContext.isUserInRole(Roles.ADMINISTRATOR.name())) {
            if (!isLoggedUser(id)) {
                return Response.status(HttpCode.FORBIDDEN.getCode()).build();
            }
        }

        final User user = userJsonConverter.convertFrom(body);
        user.setId(id);

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            userServices.update(user);
            result = OperationResult.success();
        } catch (final FieldNotValidException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("One of the fields of the user is not valid", e);
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final UserExistentException e) {
            httpCode = HttpCode.VALIDATION_ERROR;
            logger.error("There is already an user for the given email", e);
            result = getOperationResultExistent(RESOURCE_MESSAGE, "email");
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No user found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating user: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    /**
     * Update password response.
     *
     * @param id   the id
     * @param body the body
     * @return the response
     */
    @PUT
    @Path("/{id}/password")
    public Response updatePassword(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the password for user {}", id);

        //chec that only admin or owner can update passwords
        if (!securityContext.isUserInRole(Roles.ADMINISTRATOR.name())) {
            if (!isLoggedUser(id)) {
                return Response.status(HttpCode.FORBIDDEN.getCode()).build();
            }
        }

        HttpCode httpCode = HttpCode.OK;
        OperationResult result;
        try {
            userServices.updatePassword(id, getPasswordFromJson(body));
            result = OperationResult.success();
        } catch (final UserNotFoundException e) {
            httpCode = HttpCode.NOT_FOUND;
            logger.error("No user found for the given id", e);
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        logger.debug("Returning the operation result after updating user password: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    /**
     * Is logged user boolean.
     *
     * @param id the id
     * @return the boolean
     */
    private boolean isLoggedUser(final Long id) {
        try {
            final User loggerUser = userServices.findByEmail(securityContext.getUserPrincipal().getName());
            if (loggerUser.getId().equals(id)) {
                return true;
            }
        } catch (final UserNotFoundException e) {
            logger.error("User was not found.");
        }
        return false;
    }

    /**
     * Gets password from json.
     *
     * @param body the body
     * @return the password from json
     */
    private String getPasswordFromJson(final String body) {
        final JsonObject jsonObject = JsonReader.readAsJsonObject(body);
        return JsonReader.getStringOrNull(jsonObject, "password");
    }

}