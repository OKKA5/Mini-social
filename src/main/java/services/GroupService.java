package services;

import DTOs.GroupDTO;
import DTOs.GroupJoinRequestDTO;
import ejbs.GroupBean;
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

    private GroupDTO toGroupDTO(Group group) {
        GroupDTO dto = new GroupDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setCreator(group.getCreator().getName());
        dto.setStatus(group.getStatus());
        return dto;
    }

    @POST
    @Path("/create/{UserId}")
    public GroupDTO createGroup(@PathParam("UserId") int UserId, GroupDTO groupDTO) {
        Group createdGroup = groupBean.createGroup(UserId, groupDTO);
        return toGroupDTO(createdGroup);
    }

    @GET
    @Path("/{id}")
    public GroupDTO getGroup(@PathParam("id") int groupId) {
        Group group = groupBean.findGroupById(groupId);
        if (group == null) {
            throw new NotFoundException("Group not found");
        }
        return toGroupDTO(group);
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
    @Path("/promote/{groupId}/{userId}")
    public String promoteToAdmin(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
        groupBean.promoteToAdmin(groupId, userId);
        return "User promoted to admin";
    }

    @GET
    @Path("/usergroups/{userId}")
    public List<GroupDTO> getGroupsForUser(@PathParam("userId") int userId) {
        List<Group> groups = groupBean.findGroupsForUser(userId);
        return groups.stream().map(this::toGroupDTO).collect(Collectors.toList());
    }

    @POST
    @Path("/{groupId}/join/{userId}")
    public String joinGroup(@PathParam("groupId") int groupId, @PathParam("userId") int userId) {
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

}
