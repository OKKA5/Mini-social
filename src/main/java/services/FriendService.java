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
@Path("/Friend")
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

    @POST
    @Path("/request")
    public String sendRequest(
            @QueryParam("requesterId") int requesterId,
            @QueryParam("receiverId") int receiverId) {
        return friendBean.friendRequest(requesterId, receiverId);
    }

    /**
     * Accept a friend request
     */
    @PUT
    @Path("/accept")
    public String acceptRequest(
            @QueryParam("requesterId") int requesterId,
            @QueryParam("receiverId") int receiverId) {
        return friendBean.acceptFriendRequest(requesterId, receiverId);
    }

    /**
     * Reject a friend request
     */
    @PUT  // Changed from GET to PUT since this is modifying data
    @Path("/reject")
    public String rejectRequest(
            @QueryParam("requesterId") int requesterId,
            @QueryParam("receiverId") int receiverId) {
        return friendBean.rejectFriendRequest(requesterId, receiverId);
    }

}
