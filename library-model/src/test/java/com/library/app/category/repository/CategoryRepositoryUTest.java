package com.library.app.category.repository;

import com.library.app.category.model.Category;
import com.library.app.commontests.utils.TestBaseRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.library.app.commontests.category.CategoryForTestsRepository.allCategories;
import static com.library.app.commontests.category.CategoryForTestsRepository.architecture;
import static com.library.app.commontests.category.CategoryForTestsRepository.cleanCode;
import static com.library.app.commontests.category.CategoryForTestsRepository.java;
import static com.library.app.commontests.category.CategoryForTestsRepository.networks;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Unit test for Repository.
 * Created by wilferaciolli on 25/11/2016.
 */
public class CategoryRepositoryUTest extends TestBaseRepository {

    //declare entity manager for repository tests
    private CategoryRepository categoryRepository;

    /**
     * Set up tests. This method will create a entity manager for running tests. The persisntence unit is declare in src/test/resources/META-INF/persistence.xml
     */
    @Before
    public void iniTestCase() {
        //declare the persistence unit
        initializeTestDB();

        //instantiate the entity manager
        categoryRepository = new CategoryRepository();
        categoryRepository.em = em;
    }

    /**
     * Tear down method.
     */
    @After
    public void setDownTestCase() {
        closeEntityManager();
    }

    /**
     * test create and find a new category. This method initiates a transaction with the database, commit and clears it.
     * If exceptions are caught the transaction will rollback.
     */
    @Test
    public void addCategoryAndFindIt() {
        //Call DBCommandTransaction instead of creating the transaction with a try and catch
        final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
            return categoryRepository.add(java()).getId();
        });

        assertThat(categoryAddedId, is(notNullValue()));

        //Old code before refactor above
//        try {
//            //get the entity manager and add a category
//            em.getTransaction().begin();
//            categoryAddedId = categoryRepository.add(java()).getId();
//            assertThat(categoryAddedId, is(notNullValue()));
//
//            em.getTransaction().commit();
//            em.clear();
//        } catch (final Exception e) {
//            fail("This exception should not have been thrown");
//            e.printStackTrace();
//            em.getTransaction().rollback();
//        }

        //now do a find on the database to see if the newly added category is on the database
        final Category category = categoryRepository.findById(categoryAddedId);
        assertThat(category, is(notNullValue()));
        assertThat(category.getName(), is(equalTo(java().getName())));
    }

    /**
     * test find a category that does not exists, it should return/is expected a null value.
     * If exceptions are caught the transaction will rollback.
     */
    @Test
    public void findCategoryByIdNotFound() {
        // Check that the category is not found/return null
        final Category category = categoryRepository.findById(999L);
        assertThat(category, is(nullValue()));
    }

    /**
     * test find a category by passing a null id, it should return/is expected a null value.
     * If exceptions are caught the transaction will rollback.
     */
    @Test
    public void findCategoryByIdWithNullId() {
        // Check that the category is not found/return null
        final Category category = categoryRepository.findById(null);
        assertThat(category, is(nullValue()));
    }

    /**
     * test to update a category to the database. It will add a category, fetch it, update and persist back to the
     * database.
     * If exceptions are caught the transaction will rollback.
     */
    @Test
    public void updateCategory() {
        // add a category
        final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
            return categoryRepository.add(java()).getId();
        });

        // fetch it from the database
        final Category categoryAfterAdd = categoryRepository.findById(categoryAddedId);
        assertThat(categoryAfterAdd.getName(), is(equalTo(java().getName())));

        // update its values
        categoryAfterAdd.setName(cleanCode().getName());
        dbCommandExecutor.executeCommand(() -> {
            categoryRepository.update(categoryAfterAdd);
            return null;
        });

        // fetch the data from the database to check if the name was updated
        final Category categoryAfterUpdate = categoryRepository.findById(categoryAddedId);
        assertThat(categoryAfterUpdate.getName(), is(equalTo(cleanCode().getName())));
    }

    /**
     * Test to get all the categories from CategoryforTest class
     */
    @Test
    public void findAllCategories() {
        // add every element from the allCategories list to the database
        dbCommandExecutor.executeCommand(() -> {
            // Code in Java 7
            // for (final Category category : allCategories()) {
            // categoryRepository.add(category);
            // }

            // Code in Java 8 -- this method loops through the list of categories and call categoryRepository.add for
            // each of them
            allCategories().forEach(categoryRepository::add);

            return null;
        });

        // Populate a list of categories by find all categories from the database
        final List<Category> categories = categoryRepository.findAll("name");

        // assertions
        assertThat(categories.size(), is(equalTo(4)));
        assertThat(categories.get(0).getName(), is(architecture().getName()));
        assertThat(categories.get(1).getName(), is(cleanCode().getName()));
        assertThat(categories.get(2).getName(), is(java().getName()));
        assertThat(categories.get(3).getName(), is(networks().getName()));
    }

    /**
     * Test to identify if a category name is not already defined on the database. Avoiding duplicated categories.
     */
    @Test
    public void alreadyExistsForAdd() {

        // Add a category to the database.
        dbCommandExecutor.executeCommand(() -> {
            categoryRepository.add(java());
            return null;
        });

        // check if a category already exists
        assertThat(categoryRepository.alreadyExists(java()), is(equalTo(true)));
        assertThat(categoryRepository.alreadyExists(cleanCode()), is(equalTo(false)));
    }

    /**
     * Test to identify if a category name is not already defined on the database by querying its id.
     */
    @Test
    public void alreadyExistsCategoryWithId() {

        // Add two categories to the database (cleanCode and java) and assign java category
        final Category java = dbCommandExecutor.executeCommand(() -> {
            categoryRepository.add(cleanCode());
            return categoryRepository.add(java());
        });

        // check if the category java is updated, it would not flag as alreadyExists as it is an update of Java category
        // because it already has its ID
        assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));

        // set the category to have clean code name
        java.setName(cleanCode().getName());
        assertThat(categoryRepository.alreadyExists(java), is(equalTo(true)));

        // set the category to have networks name
        java.setName(networks().getName());
        assertThat(categoryRepository.alreadyExists(java), is(equalTo(false)));
    }

    /**
     * Test to identify if a category name is not already defined on the database by querying its id.
     */
    @Test
    public void existsById() {
        // Add a category to the database and get its ID
        final Long categoryAddedId = dbCommandExecutor.executeCommand(() -> {
            return categoryRepository.add(java()).getId();
        });

        // check if the category Id added above exists and that 999L does not exists
        assertThat(categoryRepository.existsById(categoryAddedId), is(equalTo(true)));
        assertThat(categoryRepository.existsById(999L), is(equalTo(false)));
    }
}

