package com.library.app.commontests.utils;

import org.junit.Ignore;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * The type Test base repository. This class is to have helper methods to initialize repos and entity managers on repositories clases.
 */
@Ignore
public class TestBaseRepository {

    //declare entity manager for repository tests
    private EntityManagerFactory emf;
    protected EntityManager em;

    //add object executor
    protected DBCommandTransactionExecutor dbCommandExecutor;

    /**
     * Initialize test.
     */
    protected void initializeTestDB() {
        //declare the persistence unit
        emf = Persistence.createEntityManagerFactory("libraryPU");
        em = emf.createEntityManager();

        //manage try and catch for entity manager
        dbCommandExecutor = new DBCommandTransactionExecutor(em);
    }

    /**
     * Close entity manager.
     */
    protected void closeEntityManager() {
        //tear down the entity manager
        em.close();
        emf.close();
    }
}
