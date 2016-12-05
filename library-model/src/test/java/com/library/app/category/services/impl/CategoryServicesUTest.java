package com.library.app.category.services.impl;

import com.library.app.category.exception.CategoryExistentException;
import com.library.app.category.exception.CategoryNotFoundException;
import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import com.library.app.category.services.CategoryServices;
import com.library.app.common.exception.FieldNotValidException;
import org.junit.Before;
import org.junit.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.library.app.commontests.category.CategoryForTestsRepository.categoryWithId;
import static com.library.app.commontests.category.CategoryForTestsRepository.java;
import static com.library.app.commontests.category.CategoryForTestsRepository.networks;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

/**
 * Category Services unit test class.
 *
 * @author wilferaciolli
 */
public class CategoryServicesUTest {

    // instance of the category services
    private CategoryServices categoryServices;

    // declare an instance of Category repository to mock database queries
    private CategoryRepository categoryRepository;

    // instance of the Validator
    private Validator validator;

    /**
     * Before method to instantiate data to each test method.
     */
    @Before
    public void initTestCase() {
        // instantiate the validator to not get NullPointer
        validator = Validation.buildDefaultValidatorFactory().getValidator();

        // mock the category repository to mock DB queries
        categoryRepository = mock(CategoryRepository.class);

        // instantiates a new CategoryServices
        categoryServices = new CategoryServicesImpl();

        // cast the category services to the implementation so we can get access to the validator and assign it to the
        // service to avoid nullPointer.
        ((CategoryServicesImpl) categoryServices).validator = validator;

        // cast the category services to the implementation to get access to the category repository which should be a
        // mock, with this we can mock DB calls
        ((CategoryServicesImpl) categoryServices).categoryRepository = categoryRepository;
    }

    /**
     * Test add a category with null name field. An exception should be thrown.
     */
    @Test
    public void addCategoryWithNullName() {
        addCategoryWithInvalidName(null);
    }

    /**
     * Test add a category with Invalid name field. An exception should be thrown. Less than minimun size.
     */
    @Test
    public void addCategoryWithShortName() {
        addCategoryWithInvalidName("A");
    }

    /**
     * Test add a category with Invalid name field. An exception should be thrown. More than minimum size.
     */
    @Test
    public void addCategoryWithLongName() {
        addCategoryWithInvalidName("this is a long name that will cause one exception to be thrown");
    }

    /**
     * Test add an existing category, the duplication is detected by category name.
     */
    @Test(expected = CategoryExistentException.class)
    public void addCategoryWithExistingName() {
        // mock that there is already a category called Java on the database
        when(categoryRepository.alreadyExists(java())).thenReturn(true);

        // Call the add category method which should throw an error
        categoryServices.add(java());
    }

    /**
     * Test add a valid category.
     */
    @Test
    public void addValidCategory() {
        // mock that there is not a category called Java on the database
        when(categoryRepository.alreadyExists(java())).thenReturn(false);
        when(categoryRepository.add(java())).thenReturn(categoryWithId(java(), 1L));

        // call the method and check if the mock worked
        final Category categoryAdded = categoryServices.add(java());
        assertThat(categoryAdded.getId(), is(equalTo(1L)));
    }

    /**
     * Test if an error is thrown if a category with null name is passed.
     */
    @Test
    public void updateCategoryWithNullName() {
        updateCategoryWithInvalidName(null);
    }

    /**
     * Test if an error is thrown if a category with too short name is passed.
     */
    @Test
    public void updateWithShortName() {
        updateCategoryWithInvalidName("A");
    }

    /**
     * Test if an error is thrown if a category with too long name is passed.
     */
    @Test
    public void updateWitLongName() {
        updateCategoryWithInvalidName("This is a lolong name for a category names");
    }

    /**
     * Check if CategoryExistent Exception is thrown if the category name passed to be updated already exists on the
     * database, avoiding duplication.
     */
    @Test(expected = CategoryExistentException.class)
    public void updateCategoryWithExistingName() {
        // mock category exists query from hte database
        when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(true);

        categoryServices.update(categoryWithId(java(), 1L));
    }

    /**
     * Test to identify that if a category that does not exists is passed , it will thrown CategoryNotFoundException.
     */
    @Test(expected = CategoryNotFoundException.class)
    public void updateCategoryNotFound() {
        // mock category does not exists
        when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
        when(categoryRepository.existsById(1L)).thenReturn(false);

        categoryServices.update(categoryWithId(java(), 1L));
    }

    /**
     * test a category can be successfully Updated.
     */
    @Test
    public void updateValidCategory() {
        // mock values from database.
        when(categoryRepository.alreadyExists(categoryWithId(java(), 1L))).thenReturn(false);
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryServices.update(categoryWithId(java(), 1L));

        // verify that the category was updated
        verify(categoryRepository).update(categoryWithId(java(), 1L));
    }

    /**
     * test find category by id.
     */
    @Test
    public void findCategoryById() {
        // mock find category by id
        when(categoryRepository.findById(1L)).thenReturn(categoryWithId(java(), 1L));

        // call find by id service method
        final Category category = categoryServices.findById(1L);

        // assert that the right category has returned
        assertThat(category, is(notNullValue()));
        assertThat(category.getId(), is(equalTo(1L)));
        assertThat(category.getName(), is(equalTo(java().getName())));
    }

    /**
     * test find category by id that does not exists.
     */
    @Test(expected = CategoryNotFoundException.class)
    public void findCategoryByIdNotFound() {
        // mock return null find by id from database
        when(categoryRepository.findById(1L)).thenReturn(null);

        categoryServices.findById(1L);
    }

    /**
     * Method to test find all categories returning an empty list of categories.
     */
    @Test
    public void findAllNoCategories() {
        // mock return an empty list of categories from database.
        when(categoryRepository.findAll("name")).thenReturn(new ArrayList<>());

        final List<Category> categories = categoryServices.findAll();

        assertThat(categories.isEmpty(), is(equalTo(true)));
    }

    /**
     * Method to test find all categories returning all categories.
     */
    @Test
    public void findAllCategories() {
        // mock return an list of categories from database.
        when(categoryRepository.findAll("name"))
                .thenReturn(Arrays.asList(categoryWithId(java(), 1L), categoryWithId(networks(), 2L)));

        final List<Category> categories = categoryServices.findAll();

        // assertions
        assertThat(categories.size(), is(equalTo(2)));
        assertThat(categories.get(0).getName(), is(equalTo(java().getName())));
        assertThat(categories.get(1).getName(), is(equalTo(networks().getName())));
    }

    /**
     * Helper method to populate a category. It should catch an exception on field name constraint validation.
     *
     * @param name The category name.
     */
    private void addCategoryWithInvalidName(final String name) {
        try {
            categoryServices.add(new Category(name));
            fail("An error should have been thrown");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }

    /**
     * Helper method to update an category. It should catch an exception on field name constraint validation.
     *
     * @param name The category name.
     */
    private void updateCategoryWithInvalidName(final String name) {
        try {
            categoryServices.update(new Category(name));
            fail("An error should have been thrown.");
        } catch (final FieldNotValidException e) {
            assertThat(e.getFieldName(), is(equalTo("name")));
        }
    }
}
