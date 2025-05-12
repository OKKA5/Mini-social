package services;

import DTOs.LoginDTO;
import DTOs.UserDTO;
import ejbs.UserBean;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import models.User;

@Stateless
@Path("user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserService {
    @EJB
    private UserBean userBean;

    @GET
    @Path("{id}")
    public UserDTO getUser(@PathParam("id") int id) {
        return userBean.findUser(id);
    }

    @POST
    @Path(("register"))
    public String createUser(User user) {
        return userBean.registerUser(user);
    }
    @PUT
    @Path("{id}")
    public String updateUser(@PathParam("id") int id, User user) {
        return userBean.updateUser(id , user);
    }
    @POST
    @Path("login")
    public String userLogin(LoginDTO loginDTO) {
        return userBean.userLogin(loginDTO.getEmail(), loginDTO.getPassword());
    }

}