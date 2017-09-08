package com.library.app.user.resource;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonReader;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.JsonWriter;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.HttpCode;
import com.library.app.common.model.OperationResult;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.model.ResourceMessage;
import com.library.app.user.exception.UserExistentException;
import com.library.app.user.exception.UserNotFoundException;
import com.library.app.user.model.Customer;
import com.library.app.user.model.User;
import com.library.app.user.model.User.Roles;
import com.library.app.user.model.User.UserType;
import com.library.app.user.model.filter.UserFilter;
import com.library.app.user.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
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

    /**
     * The User services.
     */
    @Inject
    UserServices userServices;

    /**
     * The User json converter.
     */
    @Inject
    UserJsonConverter userJsonConverter;

    /**
     * The Security context.
     */
    @Context
    SecurityContext securityContext;

    /**
     * The Uri info.
     */
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
     * Find by id response.
     *
     * @param id the id
     * @return the response
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find user by id: {}", id);
        ResponseBuilder responseBuilder;

        try {
            final User user = userServices.findById(id);
            final OperationResult result = OperationResult.success(userJsonConverter.convertToJsonElement(user));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("User found by id: {}", user);
        } catch (final UserNotFoundException e) {
            logger.error("No user found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    /**
     * Find by email and password response. authenticate method.
     *
     * @param body the body
     * @return the response
     */
    @POST
    @Path("/authenticate")
    public Response findByEmailAndPassword(final String body) {
        logger.debug("Find user by email and password");
        ResponseBuilder responseBuilder;

        try {
            //get usr by email and password
            final User userWithEmailAndPassword = getUserWithEmailAndPasswordFromJson(body);
            final User user = userServices.findByEmailAndPassword(userWithEmailAndPassword.getEmail(),
                    userWithEmailAndPassword.getPassword());
            final OperationResult result = OperationResult.success(userJsonConverter.convertToJsonElement(user));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("User found by email/password: {}", user);
        } catch (final UserNotFoundException e) {
            logger.error("No user found for email/password");
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    /**
     * Find users by filter response.
     *
     * @return the response
     */
    @GET
    public Response findByFilter() {
        final UserFilter userFilter = new UserFilterExtractorFromUrl(uriInfo).getFilter();
        logger.debug("Finding users using filter: {}", userFilter);

        final PaginatedData<User> users = userServices.findByFilter(userFilter);

        logger.debug("Found {} users", users.getNumberOfRows());

        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(users,
                userJsonConverter);
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
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
     * Gets user with email and password from json.
     *
     * @param body the body
     * @return the user with email and password from json
     */
    private User getUserWithEmailAndPasswordFromJson(final String body) {
        final User user = new Customer(); // The implementation does not matter

        final JsonObject jsonObject = JsonReader.readAsJsonObject(body);
        user.setEmail(JsonReader.getStringOrNull(jsonObject, "email"));
        user.setPassword(JsonReader.getStringOrNull(jsonObject, "password"));

        return user;
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