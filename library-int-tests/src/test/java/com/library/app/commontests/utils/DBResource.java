package com.library.app.commontests.utils;


import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

/**
 * Helper class to manage the database. It is used to clean the database and can be called by ta client.
 * Eg to clean the database when running integration tests.
 * Created by wilferaciolli on 13/01/2017.
 */
@Path("/DB")
public class DBResource {

    @Inject
    private TestRepositoryEJB tesRepositoryEJB;

    /**
     * Method to clean up the database.
     */
    @DELETE
    public void deleteAll() {
        tesRepositoryEJB.deleteAll();
    }

}