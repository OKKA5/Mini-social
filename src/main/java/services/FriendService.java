package services;


import DTOs.FriendDTO;
import ejbs.FriendBean;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import models.Friend;


import java.util.List;

@Stateless
@Path("/friend")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FriendService {

    @EJB
    FriendBean friendBean;

    @GET
    @Path("/connections/{userId}")
    public List<FriendDTO> getConnections(@PathParam("userId") int userId) {
        return friendBean.viewConnections(userId);
    }

    @GET
    @Path("/friend-request/{userId}")
    public List<FriendDTO> getFriendRequests(@PathParam("userId") int userId) {
        return friendBean.getIncomingRequests(userId);
    }
    @POST
    @Path("/request")
    public String sendRequest(
            @QueryParam("requesterId") int requesterId,
            @QueryParam("receiverId") int receiverId) {
        return friendBean.friendRequest(requesterId, receiverId);
    }

    @PUT
    @Path("/accept")
    public String acceptRequest(
            @QueryParam("requesterId") int requesterId,
            @QueryParam("receiverId") int receiverId) {
        return friendBean.acceptFriendRequest(requesterId, receiverId);
    }


    @PUT
    @Path("/reject")
    public String rejectRequest(
            @QueryParam("requesterId") int requesterId,
            @QueryParam("receiverId") int receiverId) {
        return friendBean.rejectFriendRequest(requesterId, receiverId);
    }


}
