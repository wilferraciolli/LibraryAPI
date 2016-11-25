package com.library.app.category.model.repository;

import com.library.app.category.model.Category;
import com.library.app.category.repository.CategoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import static com.library.app.commontests.category.CategoryForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit test for Repository.
 * Created by wilferaciolli on 25/11/2016.
 */
public class CategoryRepositoryUTest {

    //declare entity manager for repository tests
    private EntityManagerFactory emf;
    private EntityManager em;
    private CategoryRepository categoryRepository;

    /**
     * Set up tests. This method will create a entity manager for running tests. The persisntence unit is declare in src/test/resources/META-INF/persistence.xml
     */
    @Before
    public void iniTestCase() {
        //declare the persistence unit
        emf = Persistence.createEntityManagerFactory("libraryPU");
        em = emf.createEntityManager();

        //instantiate the entity manager
        categoryRepository = new CategoryRepository();
        categoryRepository.em = em;
    }

    /**
     * Tear down method.
     */
    @After
    public void closeEntityManager() {
        //tear down the entimy manager
        em.close();
        emf.close();
    }

    /**
     * test add category
     */
    @Test
    public void addCategoryAndFindIt() {
        Long categoryAddedId = null;
        try {
            //get the entity manager and add acategory
            em.getTransaction().begin();
            categoryAddedId = categoryRepository.add(java()).getId();
            assertThat(categoryAddedId, is(notNullValue()));

            em.getTransaction().commit();
            em.clear();
        } catch (final Exception e) {
            fail("This exception should not have been thrown");
            e.printStackTrace();
            em.getTransaction().rollback();
        }

        //now do a find on the database to see if the newly added category is on the database
        final Category category = categoryRepository.findById(categoryAddedId);
        assertThat(category, is(notNullValue()));
        assertThat(category.getName(), is(equalTo(java().getName())));
    }
}

