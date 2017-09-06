package com.library.app.category.resource;

import com.google.gson.JsonElement;
import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import com.library.app.common.json.JsonUtils;
import com.library.app.common.json.JsonWriter;
import com.library.app.common.json.OperationResultJsonWriter;
import com.library.app.common.model.HttpCode;
import com.library.app.common.model.OperationResult;
import com.library.app.common.model.PaginatedData;
import com.library.app.common.model.ResourceMessage;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.util.List;

import static com.library.app.common.model.StandardsOperationResults.getOperationResultExistent;
import static com.library.app.common.model.StandardsOperationResults.getOperationResultInvalidField;
import static com.library.app.common.model.StandardsOperationResults.getOperationResultNotFound;

/**
 * Class to implement category resource RESTful service.
 *
 * @author wilferaciolli
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    // Add a logger
    private final Logger logger = LoggerFactory.getLogger(getClass());

    // Add the resource messages to catch and handle error messages
    private static final ResourceMessage RESOURCE_MESSAGE = new ResourceMessage("category");

    // Add dependency category services
    @Inject
    CategoryServices categoryServices;

    // Declare Json Converter
    @Inject
    CategoryJsonConverter categoryJsonConverter;

    /**
     * Method to add a category.
     *
     * @param body The JSON file
     * @return JSON value.
     */
    @POST
    public Response add(final String body) {
        logger.debug("Adding a new category with body{},", body);

        // Convert JSOn to Category Java object
        Category category = categoryJsonConverter.convertFrom(body);

        // define the httpCode, if success then it is created otherwise error on the catch blick
        HttpCode httpCode = HttpCode.CREATED;
        OperationResult result = null;
        try {
            // persist the category to the database
            category = categoryServices.add(category);
            // get the operation result to check if it was a success
            result = OperationResult.success(JsonUtils.getJsonElementWithId(category.getId()));
        } catch (final FieldNotValidException e) {
            // field null/invalid exception
            logger.error("One of the fields of the category is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);
        } catch (final CategoryExistentException e) {
            // duplicated category exception
            logger.error("There is already category for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultExistent(RESOURCE_MESSAGE, "name");
        }

        logger.debug("Returning the operation result after adding category : {}", result);
        // return the response from JaxRS
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    /**
     * Method to update a category. It takes a category id and the body (Json). Then it validates it by parsing from
     * Json into Java object. If any exception get thrown it will be handled..
     *
     * @param id   The category id
     * @param body The JSON body
     * @return JSON response.
     */
    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") final Long id, final String body) {
        logger.debug("Updating the category {} with body {}", id, body);

        // create the category object.
        final Category category = categoryJsonConverter.convertFrom(body);
        category.setId(id);

        // define http code as success
        HttpCode httpCode = HttpCode.OK;
        OperationResult result;

        try {
            categoryServices.update(category);
            result = OperationResult.success();

        } catch (final FieldNotValidException e) {
            logger.error("One of the field of the category is not valid", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultInvalidField(RESOURCE_MESSAGE, e);

        } catch (final CategoryExistentException e) {
            logger.error("There is already a category for the given name", e);
            httpCode = HttpCode.VALIDATION_ERROR;
            result = getOperationResultExistent(RESOURCE_MESSAGE, "name");

        } catch (final CategoryNotFoundException e) {
            logger.error("No category found for the given id", e);
            httpCode = HttpCode.NOT_FOUND;
            result = getOperationResultNotFound(RESOURCE_MESSAGE);
        }

        // return the Operation result to the client
        logger.debug("Returning the operation after updating category: {}", result);
        return Response.status(httpCode.getCode()).entity(OperationResultJsonWriter.toJson(result)).build();
    }

    /**
     * Method to find a category by id.
     *
     * @param id The category id
     * @return The category object.
     */
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") final Long id) {
        logger.debug("Find category: {}", id);

        // builds the response
        ResponseBuilder responseBuilder;

        try {
            // get the category object from its id
            final Category category = categoryServices.findById(id);

            // convert to Json
            final OperationResult result = OperationResult.success(categoryJsonConverter.convertToJsonElement(category));
            responseBuilder = Response.status(HttpCode.OK.getCode()).entity(OperationResultJsonWriter.toJson(result));
            logger.debug("Category found: {}", category);

        } catch (final CategoryNotFoundException e) {
            logger.error("No category found for id", id);
            responseBuilder = Response.status(HttpCode.NOT_FOUND.getCode());
        }

        return responseBuilder.build();
    }

    /**
     * Method to get all categories.
     *
     * @return All categories or empty list.
     */
    @GET
    public Response findAll() {
        logger.debug("Find all categories");

        // get all categories
        final List<Category> categories = categoryServices.findAll();
        logger.debug("Found {} categories", categories.size());

        // get all categories and convert into json
        final JsonElement jsonWithPagingAndEntries = JsonUtils.getJsonElementWithPagingAndEntries(new PaginatedData<Category>(categories.size(), categories),
                categoryJsonConverter);

        // return the converted json
        return Response.status(HttpCode.OK.getCode()).entity(JsonWriter.writeToString(jsonWithPagingAndEntries))
                .build();
    }
}
