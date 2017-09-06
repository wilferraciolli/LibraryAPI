package com.library.app.commontests.author;

import com.library.app.author.services.AuthorServices;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.library.app.commontests.author.AuthorForTestsRepository.allAuthors;

/**
 * Helper class to add every author to the database.
 * It runs as a client.
 */
@Path("/DB/authors")
@Produces(MediaType.APPLICATION_JSON)
public class AuthorResourceDB {

    @Inject
    private AuthorServices authorServices;

    /**
     * Create every author onto the database.
     */
    @POST
    public void addAll() {
        allAuthors().forEach(authorServices::add);
    }

}
