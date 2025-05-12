package ejbs;

import DTOs.GroupDTO;

import DTOs.GroupJoinRequestDTO;
import DTOs.PostDTO;
import Messaging.JMSClient;
import Messaging.NotificationEvent;
import jakarta.ejb.Stateless;

import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import models.Group;
import models.GroupJoinRequest;
import models.Post;
import models.User;

import java.util.ArrayList;
import java.util.List;

import static models.GroupJoinRequest.Status.ACCEPTED;
import static models.GroupJoinRequest.Status.PENDING;

@Stateless
public class GroupBean {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private JMSClient jmsClient;

    public GroupDTO createGroup(int UserId, Group group) {
        User creator = em.find(User.class, UserId);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }

        group.setCreator(creator);
        List<User> initialMembers = new ArrayList<>();
        initialMembers.add(creator);
        group.setMembers(initialMembers);

        List<User> admins = new ArrayList<>();
        admins.add(creator);
        group.setAdmins(admins);
        GroupDTO groupDto = new GroupDTO().toGroupDTO(group);


        em.persist(group);
        return groupDto;
    }

    public GroupDTO findGroupById(int groupId) {
        Group group = em.find(Group.class, groupId);
        if (group == null) {
            {
                throw new IllegalArgumentException("Group Doesnt Exist");
            }
        }
        GroupDTO groupDto = new GroupDTO().toGroupDTO(group);
        return groupDto;
    }

    public void deleteGroup(int groupId) {
        Group group = em.find(Group.class, groupId);
        if (group != null) {
            em.remove(group);
        }
    }


    public String requestToJoinGroup(int userId, int groupId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

        if (user == null || group == null) throw new IllegalArgumentException("User or Group not found");

        if (group.getStatus().equalsIgnoreCase("open")) {
            if (!group.getMembers().contains(user)) {
                group.getMembers().add(user);
                em.merge(group);
                jmsClient.sendMessage(new NotificationEvent(
                        "join group",
                        user.getName(),
                        group.getName(),
                        user.getName() + "joined group"));
                return "Joined group directly (open group)";
            } else {
                throw new IllegalArgumentException("User is already a member of the group");
            }
        }

        TypedQuery<GroupJoinRequest> query = em.createQuery(
                "SELECT r FROM GroupJoinRequest r WHERE r.user.id = :userId",
                GroupJoinRequest.class
        );
        query.setParameter("userId", userId);
        if (!query.getResultList().isEmpty()) {
            throw new IllegalArgumentException("Already sent join request");
        }


        GroupJoinRequest request = new GroupJoinRequest();
        request.setUser(user);
        request.setGroup(group);
        request.setStatus(GroupJoinRequest.Status.PENDING);
        em.persist(request);
        // notification sent to jms queue
        jmsClient.sendMessage(new NotificationEvent(
                "group request",
                user.getName(),
                group.getName(),
                user.getName() + "request to join group"));
        return "Request sent and pending admin approval";
    }

    public String approveRequest(int adminId,int requestId) {
        GroupJoinRequest request = em.find(GroupJoinRequest.class, requestId);
        if (request == null || !PENDING.equals(request.getStatus())) throw new IllegalArgumentException("Invalid request");
        User admin = em.find(User.class, adminId);
        Group group = request.getGroup();
        User user = request.getUser();

        if(!group.getAdmins().contains(admin)) {
           throw new IllegalArgumentException ("You do not have permission to approve this request");
        }

        group.getMembers().add(user);
        request.setStatus(ACCEPTED);


        em.merge(group);
        em.remove(request);
        // notification sent to jms queue
        jmsClient.sendMessage(new NotificationEvent(
                "join group",
                user.getName(),
                group.getName(),
                user.getName() + "joined group"));

        return "Request approved";
    }

    public String rejectRequest(int adminId,int requestId) {
        GroupJoinRequest request = em.find(GroupJoinRequest.class, requestId);
        if (request == null || !PENDING.equals(request.getStatus())) throw new IllegalArgumentException("Invalid request");
        User admin = em.find(User.class, adminId);
        Group group = request.getGroup();
        if(!group.getAdmins().contains(admin)) {
            throw new IllegalArgumentException ("You do not have permission to reject this request");
        }
        em.remove(request);
        return "Request rejected";
    }

    public String addPostToGroup(PostDTO postDTO, int groupId, int userId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

        if (user == null || group == null) {
            throw new IllegalArgumentException("User or group not found");
        }

        if ("closed".equalsIgnoreCase(group.getStatus())) {
            if (!group.getMembers().contains(user)) {
                throw new IllegalArgumentException("User is not a member of the closed group");
            }
        }

        Post post = new Post();
        post.setUser(user);
        post.setGroups(group);
        post.setDescription(postDTO.getDescription());
        post.setImageURL(postDTO.getImageURL());

        em.persist(post);
        em.merge(group);
        return "Post added to group successfully";
    }

    public String removePostFromGroup(int postId, int groupId, int userId) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);
        if (user == null || group == null) {
            throw new IllegalArgumentException("User or group not found");
        }
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }

        boolean isPostOwner = post.getUser().getId() == userId;
        boolean isGroupAdmin = group.getAdmins().contains(user);
        boolean isGroupMember = group.getMembers().contains(user);

        if ((isGroupAdmin && isGroupMember) || isPostOwner) {
            em.remove(post);
            return "Post removed from group successfully";
        } else {
            throw new IllegalArgumentException("You are not authorized to remove this post");
        }
    }

    public String DeleteGroup(int groupId, int userId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);
        boolean isGroupAdmin = group.getAdmins().contains(user);

        if (isGroupAdmin) {
            em.remove(group);
            return "group Deleted successfully";
        } else {
            throw new IllegalArgumentException("You are not authorized to delete this group");
        }
    }

    public String DeleteUserFromGroup(int groupId, int adminId, int userId) {
        User user = em.find(User.class, userId);
        User admin = em.find(User.class, adminId);
        Group group = em.find(Group.class, groupId);
        if (user == null || admin == null || group == null) {
            throw new IllegalArgumentException("User, admin, or group not found");
        }
        boolean isGroupAdmin = group.getAdmins().contains(admin);
        boolean isGroupMember = group.getMembers().contains(user);
        if (!isGroupAdmin) {
            throw new IllegalArgumentException("You are not authorized to remove this user");
        }
        if (userId == adminId) {
            throw new IllegalArgumentException("You cannot remove yourself instead you can leave this group");
        }
        if (!isGroupMember) {
            throw new IllegalArgumentException("User is not a member of the group");
        }
        group.getMembers().remove(user);
        em.merge(group);
        return "User removed from group successfully";
    }

    public String promoteToAdmin(int groupId, int userId, int adminId) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        User admin = em.find(User.class, adminId);
        if (user == null || admin == null || group == null) {
            throw new IllegalArgumentException("User, admin, or group not found");
        }
        boolean isGroupMember = group.getMembers().contains(user);
        boolean isGroupAdmin = group.getAdmins().contains(admin);
        if (!isGroupMember) {
            throw new IllegalArgumentException("the user with id : " + userId + " is not a member of the group");
        }
        if(group.getAdmins().contains(user)) {
            throw new IllegalArgumentException("this user is already an admin");
        }
        if (!isGroupAdmin) {
            throw new IllegalArgumentException("You are not authorized to promote users");
        } else {
            group.getAdmins().add(user);

            return "user promoted";
        }
    }

    public String LeaveGroup(int groupId, int userId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);
        if (user == null || group == null) {
            throw new IllegalArgumentException("User or group not found");
        }
        boolean isGroupMember = group.getMembers().contains(user);
        boolean isGroupAdmin = group.getAdmins().contains(user);

        if (!isGroupMember) {
            throw new IllegalArgumentException("user is not a member of that group ");
        }
        if (isGroupAdmin) {
            group.getAdmins().remove(user);
            group.getMembers().remove(user);
            if (group.getAdmins().isEmpty()) {
                if (group.getMembers().isEmpty()) {
                    em.remove(group);
                    return "Group removed";
                } else {
                    group.getAdmins().add(group.getMembers().get(0));
                }
            }
            // notification sent to jms queue
            jmsClient.sendMessage(new NotificationEvent(
                    "leave group",
                    user.getName(),
                    group.getName(),
                    user.getName() + "left group"));
            em.merge(group);
            return "Left Group";
        }
        // notification sent to jms queue
        group.getMembers().remove(user);
        jmsClient.sendMessage(new NotificationEvent(
                "leave group",
                user.getName(),
                group.getName(),
                user.getName() + "left group"));
        em.merge(group);
        return "left Group";
    }

}