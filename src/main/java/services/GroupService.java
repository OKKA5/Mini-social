package services;

import DTOs.GroupDTO;
import DTOs.GroupJoinRequestDTO;
import DTOs.PostDTO;
import ejbs.GroupBean;
import jakarta.data.repository.Delete;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import models.Group;
import models.GroupJoinRequest;

import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Path("/group")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupService {

    @EJB
    private GroupBean groupBean;


    @POST
    @Path("/create/{UserId}")
    public GroupDTO createGroup(@PathParam("UserId") int UserId, Group group) {
        return groupBean.createGroup(UserId, group);

    }

    @GET
    @Path("/{id}")
    public GroupDTO getGroup(@PathParam("id") int groupId) {
        return groupBean.findGroupById(groupId);
    }

    @POST
    @Path("/join/{groupId}/{userId}")
    public String requestToJoinGroup(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
        return groupBean.joinGroup(groupId, userId);
    }

    @POST
    @Path("/approve/{groupId}/{userId}")
    public String approveMember(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
        groupBean.approveMember(groupId, userId);
        return "User approved successfully";
    }

    @POST
    @Path("/leave/{groupId}/{userId}")
    public String leaveGroup(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
        groupBean.leaveGroup(groupId, userId);
        return "User left the group";
    }

    @DELETE
    @Path("/delete/{groupId}")
    public String deleteGroup(@PathParam("groupId") int groupId) {
        groupBean.deleteGroup(groupId);
        return "Group deleted successfully";
    }

    @POST
    @Path("/promote")
    public String promoteToAdmin(@QueryParam("groupId") int groupId, @QueryParam("userId") int userId, @QueryParam("adminId") int adminId) {
        return groupBean.promoteToAdmin(groupId, userId, adminId);

    }

    @DELETE
    @Path("/remove")
    public String removePostFromGroup(@QueryParam("postId") int postId, @QueryParam("groupId") int groupId, @QueryParam("userId") int userId) {
        return groupBean.removePostFromGroup(postId, groupId, userId);
    }


    @POST
    @Path("/join")
    public String joinGroup(@QueryParam("groupId") int groupId, @QueryParam("userId") int userId) {
        return groupBean.requestToJoinGroup(userId, groupId);
    }

    @POST
    @Path("/approve/{requestId}")
    public String approve(@PathParam("requestId") int requestId) {
        return groupBean.approveRequest(requestId);
    }

    @POST
    @Path("/reject/{requestId}")
    public String reject(@PathParam("requestId") int requestId) {
        return groupBean.rejectRequest(requestId);
    }

    @GET
    @Path("/{groupId}/requests")
    public List<GroupJoinRequestDTO> getJoinRequests(@PathParam("groupId") int groupId) {
        List<GroupJoinRequest> requests = groupBean.getRequestsForGroup(groupId);

        return requests.stream()
                .map(r -> new GroupJoinRequestDTO(
                        r.getId(),
                        r.getUser().getId(),
                        r.getUser().getName(),
                        r.getStatus()))
                .toList();
    }

    @POST
    @Path("/add-post")
    public String addPostToGroup(@QueryParam("groupId") int groupId, PostDTO postDTO, @QueryParam("UserId") int UserId) {
        return groupBean.addPostToGroup(postDTO, groupId, UserId);
    }

    @DELETE
    @Path("/Delete")
    @Produces(MediaType.TEXT_PLAIN)
    public String DeleteGroup(@QueryParam("groupId") int groupId, @QueryParam("AdminId") int UserId) {
        return groupBean.DeleteGroup(groupId, UserId);
    }

    @DELETE
    @Path("/DeleteGroupUser")
    @Produces(MediaType.TEXT_PLAIN)
    public String DeleteUserFromGroup(@QueryParam("groupId") int groupId, @QueryParam("AdminId") int AdminId, @QueryParam("UserId") int UserId) {
        return groupBean.DeleteUserFromGroup(groupId, AdminId, UserId);
    }

    @DELETE
    @Path("/Leave")
    @Produces(MediaType.TEXT_PLAIN)
    public String LeaveGroup(@QueryParam("groupId") int groupId, @QueryParam("UserId") int UserId) {
        return groupBean.LeaveGroup(groupId, UserId);
    }
}
