package services;

import ejbs.UserBean;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import models.User;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {
    @EJB
    private UserBean userBean;

    @GET
    @Path("{id}")
    public User getUser(@PathParam("id") int id) {
        return userBean.findUser(id);
    }
    
    @POST
    public User createUser(User user) {
        return userBean.createUser(user);
    }

}
