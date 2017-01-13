package com.library.app.commontests.utils;


import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;

/**
 * Helper class to send requests to the server (as a client) to manage the database.
 * Eg to clean the database when runnning integration tests.
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