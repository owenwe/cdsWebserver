package org.pesc.cds.web;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pesc.cds.model.Credentials;
import org.pesc.cds.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by prabhu (prajendran@ccctechcenter.org) on 11/10/16.
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

    @RequestMapping(value="/getUserByName", method= RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<Credentials> getUserByName(@RequestParam(value="name", required = false) String name) {
        return userService.findByName(name);
    }

    @RequestMapping(value="/getUserById", method= RequestMethod.GET)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public List<Credentials> getUserById(@RequestParam(value="id", required = false) Integer id) {

        ArrayList<Credentials> results = new ArrayList<Credentials>();

        Credentials user = userService.findById(id);

        if (user != null) {
            results.add(user);
        }

        return results;

    }

    @RequestMapping(value="/saveUser", method= RequestMethod.PUT)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public Credentials saveUser(@RequestBody Credentials user) {
        userService.update(user);

        return user;
    }

    @RequestMapping(value="/createUser", method= RequestMethod.POST)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public Credentials createUser(@RequestBody Credentials user) {

        return userService.create(user);
    }


    @RequestMapping(value="/updatePassword", method= RequestMethod.PUT)
    @Produces(MediaType.APPLICATION_JSON)
    @ResponseBody
    public ResponseEntity<HttpStatus> updatePassword(@RequestBody Credentials user) {

        try {
            userService.updatePassword(user.getId(), user.getPassword());
        }
        catch (Exception e) {
            e.printStackTrace();
            log.error("Failed to update the password.", e);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @RequestMapping(value = "/removeUser", method = RequestMethod.DELETE)
    @Produces(MediaType.APPLICATION_JSON)
    public void removeUser(@PathParam("id") Integer id) {
        userService.delete(id);

    }

}
