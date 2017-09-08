package com.library.app.commontests.user;

import com.library.app.user.services.UserServices;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.library.app.commontests.user.UserForTestsRepository.admin;
import static com.library.app.commontests.user.UserForTestsRepository.allUsers;

/**
 * The type User resource db.
 */
@Path("/DB/users")
@Produces(MediaType.APPLICATION_JSON)
public class UserResourceDB {

    @Inject
    private UserServices userServices;

    /**
     * Add all.
     */
    @POST
    public void addAll() {
        allUsers().forEach(userServices::add);
    }

    /**
     * Add admin user.
     */
    @POST
    @Path("/admin")
    public void addAdmin() {
        userServices.add(admin());
    }

}
