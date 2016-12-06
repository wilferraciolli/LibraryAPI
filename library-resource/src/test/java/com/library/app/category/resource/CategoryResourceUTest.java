package com.library.app.category.resource;

import static com.library.app.commontests.category.CategoryForTestsRepository.categoryWithId;
import static com.library.app.commontests.category.CategoryForTestsRepository.java;
import static com.library.app.commontests.category.CategoryForTestsRepository.networks;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileRequest;
import static com.library.app.commontests.utils.FileTestNameUtils.getPathFileResponse;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesExpectedJson;
import static com.library.app.commontests.utils.JsonTestUtils.assertJsonMatchesFileContent;
import static com.library.app.commontests.utils.JsonTestUtils.readJsonFile;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;


/**
 * class to test Category Resource.
 *
 * @author wilferaciolli
 *
 */
public class CategoryResourceUTest {

    private CategoryResource categoryResource;

    // path to the folder where the JSON file is
    private static final String PATH_RESOURCE = "categories";

    @Mock
    private CategoryServices categoryServices;

    /**
     * Method to initialize he data for the tests.
     */
    @Before
    public void initTestCase() {
        //initialise the mocks
        MockitoAnnotations.initMocks(this);
        categoryResource = new CategoryResource();

        // initialize CategoryServices on category resource
        categoryResource.categoryServices = categoryServices;
       // categoryResource.categoryJsonConverter = new CategoryJsonConverter();
    }

    /**
     * test to add valid category.
     */
    @Test
    public void addValidCategory() {
        when(categoryServices.add(java())).thenReturn(categoryWithId(java(), 1L));

        final Response response = categoryResource
                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "newCategory.json")));

        // assertions
        assertThat(response.getStatus(), is(equalTo(201)));
//        assertThat(response.getStatus(), is(equalTo(HttpCode.CREATED.getCode())));
        assertJsonMatchesExpectedJson(response.getEntity().toString(), "{\"id\": 1}");
    }

//    /**
//     * test to add an existing category.
//     */
//    @Test
//    public void addExistingCategory() {
//        // mock the return value to thrown category existing exception
//        when(categoryServices.add(java())).thenThrow(new CategoryExistentException());
//
//        final Response response = categoryResource
//                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "newCategory.json")));
//
//        // assertions
//        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
//    }
//
//    /**
//     * test to add a category with null name.
//     */
//    @Test
//    public void addCategoryWithNullName() {
//        // mock the return value to thrown category existing exception
//        when(categoryServices.add(new Category())).thenThrow(new FieldNotValidException("name", "may not be null"));
//
//        final Response response = categoryResource
//                .add(readJsonFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json")));
//
//        // assertions
//        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
//    }
//
//    /**
//     * Updates a valid category. Uses verify mock.
//     */
//    @Test
//    public void updateValidCategory() {
//        final Response response = categoryResource.update(1L,
//                readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
//        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertThat(response.getEntity().toString(), is(equalTo("")));
//
//        verify(categoryServices).update(categoryWithId(java(), 1L));
//    }
//
//    /**
//     * Try to update a category that belongs to another category's name. Throw Category existent exception. Avoid
//     * duplication.
//     */
//    @Test
//    public void updateCategoryWithNameBelongingToOtherCategory() {
//        doThrow(new CategoryExistentException()).when(categoryServices).update(categoryWithId(java(), 1L));
//
//        final Response response = categoryResource.update(1L,
//                readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
//        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryAlreadyExists.json");
//    }
//
//    /**
//     * Update category with null name. Throw exception. Avoid adding null categories.
//     */
//    @Test
//    public void updateCategoryWithNullName() {
//        doThrow(new FieldNotValidException("name", "may not be null")).when(categoryServices).update(
//                categoryWithId(new Category(), 1L));
//
//        final Response response = categoryResource.update(1L,
//                readJsonFile(getPathFileRequest(PATH_RESOURCE, "categoryWithNullName.json")));
//        assertThat(response.getStatus(), is(equalTo(HttpCode.VALIDATION_ERROR.getCode())));
//        assertJsonResponseWithFile(response, "categoryErrorNullName.json");
//    }
//
//    /**
//     * Update a non existent category.
//     */
//    @Test
//    public void updateCategoryNotFound() {
//        doThrow(new CategoryNotFoundException()).when(categoryServices).update(categoryWithId(java(), 2L));
//
//        final Response response = categoryResource.update(2L,
//                readJsonFile(getPathFileRequest(PATH_RESOURCE, "category.json")));
//        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
//        assertJsonResponseWithFile(response, "categoryNotFound.json");
//    }
//
//    /**
//     * Method to test find category.
//     */
//    @Test
//    public void findCategory() {
//        when(categoryServices.findById(1L)).thenReturn(categoryWithId(java(), 1L));
//
//        final Response response = categoryResource.findById(1L);
//
//        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "categoryFound.json");
//    }
//
//    /**
//     * Method to test find category that does not exists.
//     */
//    @Test
//    public void findCategoryNotFound() {
//        when(categoryServices.findById(1L)).thenThrow(new CategoryNotFoundException());
//
//        final Response response = categoryResource.findById(1L);
//
//        assertThat(response.getStatus(), is(equalTo(HttpCode.NOT_FOUND.getCode())));
//    }
//
//    /**
//     * Method to test find all categories. It will return an empty list of categories.
//     */
//    @Test
//    public void findAllNoCategory() {
//        // mock return empty list of categories
//        when(categoryServices.findAll())
//                .thenReturn(new ArrayList<>());
//
//        final Response response = categoryResource.findAll();
//
//        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "emptyListOfCategories.json");
//    }
//
//    /**
//     * Method to test find all two categories. It will return two categories.
//     */
//    @Test
//    public void findAllTwoCategories() {
//        // mock find java and networks categories
//        when(categoryServices.findAll())
//                .thenReturn(Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));
//
//        final Response response = categoryResource.findAll();
//
//        assertThat(response.getStatus(), is(equalTo(HttpCode.OK.getCode())));
//        assertJsonResponseWithFile(response, "twoCategories.json");
//    }
//
//    /**
//     * Helper method to compare REST responses to Json files content. Files in src/test/resources contains error
//     * messages
//     * that will be compared agains Json responses.
//     */
//    private void assertJsonResponseWithFile(final Response response, final String fileName) {
//        assertJsonMatchesFileContent(response.getEntity().toString(), getPathFileResponse(PATH_RESOURCE, fileName));
//    }
}
