package com.library.app.commontests.category;

import com.library.app.category.services.CategoryServices;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.library.app.commontests.category.CategoryForTestsRepository.allCategories;

/**
 * Helper class to add every category to the database.
 * It runs as a client.
 * Created by wilferaciolli on 13/01/2017.
 */
@Path("/DB/categories")
@Produces(MediaType.APPLICATION_JSON)
public class CategoryResourceDB {

    @Inject
    private CategoryServices categoryServices;

    /**
     * Create every category onto the database.
     */
    @POST
    public void addAll() {
        allCategories().forEach(categoryServices::add);
    }

}