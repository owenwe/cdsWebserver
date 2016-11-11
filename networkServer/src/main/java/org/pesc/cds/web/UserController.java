package org.pesc.cds.web;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pesc.cds.model.Credentials;
import org.pesc.cds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu on 11/10/16.
 */
@RestController
@RequestMapping(value="api/v1/users")
public class UserController {

    private static final Log log = LogFactory.getLog(UserController.class);

    //Security is enforced using method level annotations on the service.
    @Autowired
    private UserService userService;

    private void checkOrganizationParameter(Integer organizationId) {
        if (organizationId == null) {
            throw new IllegalArgumentException("The organizationID parameter is mandatory.");
        }
    }

    @RequestMapping(method= RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<Credentials> getUsers( ){

        ArrayList<Credentials> results = new ArrayList<Credentials>();
        Iterable<Credentials> users = userService.findAll();
        for ( Credentials user : users){
            results.add(user);
        }
        return results;

    }

    public List<Credentials> findUser(
            @RequestParam("id") Integer id
    ) {

        checkOrganizationParameter(id);

        ArrayList<Credentials> results = new ArrayList<Credentials>();

        Credentials user = userService.findById(id);

        if (user != null) {
            results.add(user);
        }

        return results;
    }

    /*@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> findUser(
            @QueryParam("id") Integer id,
            @QueryParam("name") String name,
            @QueryParam("organizationId") Integer organizationId
    ) {

        checkOrganizationParameter(organizationId);

        return userService.search(
                id,
                organizationId,
                name);
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Credentials> getUser(@PathParam("id") Integer id) {
        ArrayList<Credentials> results = new ArrayList<Credentials>();

        Credentials user = userService.findById(id);

        if (user != null) {
            results.add(user);
        }

        return results;
    }


    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credentials createUser(Credentials user) {

        return userService.create(user);
    }

    @Path("/{id}")
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Credentials saveUser(@PathParam("id") Integer id, Credentials user) {
        userService.update(user);
        userService.updateSystemAdminEmails();

        return user;
    }

    @Path("/{id}/password")
    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response updatePassword(@PathParam("id") Integer id, String password) {

        try {
            userService.updatePassword(id, password);
        }
        catch (IllegalArgumentException e) {
            //throw new ApiException(e, Response.Status.BAD_REQUEST, "/users/" + id + "/password");
            log.error("Failed to convert to and from dates.", e);
        }

        return Response.ok().build();
    }


    @Path("/{id}")
    @DELETE
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void removeUser(@PathParam("id") Integer id) {
        userService.delete(id);

    }*/


}
