package com.library.app.author.repository;

import static com.library.app.commontests.author.AuthorForTestsRepository.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.library.app.author.model.Author;



import com.library.app.author.model.Author;
import com.library.app.commontests.utils.DBCommandTransactionExecutor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static com.library.app.commontests.author.AuthorForTestsRepository.robertMartin;

/**
 * Unit tests for Author Repository.
 * Created by wilferaciolli on 21/02/2017.
 */
public class AuthorRepositoryUTest {

    //Declare entity manager
    private EntityManagerFactory emf;
    private EntityManager em;
    private DBCommandTransactionExecutor dbCommandExecutor;
    private AuthorRepository authorRepository;

    /**
     * Set up entity manager and data.
     */
    @Before
    public void initTestsCase() {
        emf = Persistence.createEntityManagerFactory("libraryPU");
        em = emf.createEntityManager();

        authorRepository = new AuthorRepository();
        authorRepository.em = em;

        dbCommandExecutor = new DBCommandTransactionExecutor(em);
    }

    /**
     * Clear down and close entity manager.
     */
    @After
    public void closeEntityManager() {
        em.close();
        emf.close();
    }

    /**
     * Test add author and find it on the databse.
     */
    @Test
    public void addAuthorAndFindIt() {
        final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
            return authorRepository.add(robertMartin()).getId();
        });
        assertThat(authorAddedId, is(notNullValue()));

        final Author author = authorRepository.findById(authorAddedId);
        assertThat(author, is(notNullValue()));
        assertThat(author.getName(), is(equalTo(robertMartin().getName())));
    }

    /**
     * Test find author with not known id.
     */
    @Test
    public void findAuthorByIdNotFound() {
        final Author author = authorRepository.findById(999L);
        assertThat(author, is(nullValue()));
    }

    /**
     * test update author.
     */
    @Test
    public void updateAuthor() {
        final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
            return authorRepository.add(robertMartin()).getId();
        });
        assertThat(authorAddedId, is(notNullValue()));

        final Author author = authorRepository.findById(authorAddedId);
        assertThat(author.getName(), is(equalTo(robertMartin().getName())));

        author.setName("Uncle Bob");
        dbCommandExecutor.executeCommand(() -> {
            authorRepository.update(author);
            return null;
        });

        final Author authorAfterUpdate = authorRepository.findById(authorAddedId);
        assertThat(authorAfterUpdate.getName(), is(equalTo("Uncle Bob")));
    }

    /**
     * Test does author id exists.
     */
    @Test
    public void existsById() {
        final Long authorAddedId = dbCommandExecutor.executeCommand(() -> {
            return authorRepository.add(robertMartin()).getId();
        });

        assertThat(authorRepository.existsById(authorAddedId), is(equalTo(true)));
        assertThat(authorRepository.existsById(999l), is(equalTo(false)));
    }


}
