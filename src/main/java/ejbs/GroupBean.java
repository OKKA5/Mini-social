package ejbs;

import DTOs.GroupDTO;

import DTOs.PostDTO;
import jakarta.ejb.Stateless;

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

    public String joinGroup(int groupId, int userId) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        if (group == null || user == null) return "Group or user not found";

        if ("open".equalsIgnoreCase(group.getStatus())) {
            group.getMembers().add(user);
            em.merge(group);
            // Trigger JMS notification here if needed
            return "User joined successfully";
        } else {
            // In a real app, insert into a pending table or queue
            return "Join request sent (pending approval)";
        }
    }

    public void approveMember(int groupId, int userId) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
            em.merge(group);
        }
    }

    public void leaveGroup(int groupId, int userId) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        group.getMembers().remove(user);
        group.getAdmins().remove(user);
        em.merge(group);
    }

    public void deleteGroup(int groupId) {
        Group group = em.find(Group.class, groupId);
        if (group != null) {
            em.remove(group);
        }
    }


    public List<Group> findGroupsForUser(int userId) {
        User user = em.find(User.class, userId);
        TypedQuery<Group> query = em.createQuery("SELECT g FROM Group g JOIN g.members m WHERE m.id = :userId", Group.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }


    public String requestToJoinGroup(int userId, int groupId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

        if (user == null || group == null) return "User or Group not found";

        if (group.getStatus().equalsIgnoreCase("open")) {
            if (!group.getMembers().contains(user)) {
                group.getMembers().add(user);
                em.merge(group);
                return "Joined group directly (open group)";
            } else {
                return "User is already a member of the group";
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

        return "Request sent and pending admin approval";
    }

    public String approveRequest(int requestId) {
        GroupJoinRequest request = em.find(GroupJoinRequest.class, requestId);
        if (request == null || !PENDING.equals(request.getStatus())) return "Invalid request";

        Group group = request.getGroup();
        User user = request.getUser();

        group.getMembers().add(user);
        request.setStatus(ACCEPTED);


        em.merge(group);
        em.remove(request);


        return "Request approved";
    }

    public String rejectRequest(int requestId) {
        GroupJoinRequest request = em.find(GroupJoinRequest.class, requestId);
        if (request == null || !PENDING.equals(request.getStatus())) return "Invalid request";
        em.remove(request);
        return "Request rejected";
    }

    public List<GroupJoinRequest> getRequestsForGroup(int groupId) {
        Group group = em.find(Group.class, groupId);
        if (group == null) {
            return List.of(); // or throw exception
        }

        return em.createQuery("SELECT r FROM GroupJoinRequest r WHERE r.group.id = :groupId AND r.status = 'pending'", GroupJoinRequest.class).setParameter("groupId", groupId).getResultList();
    }

    public String addPostToGroup(PostDTO postDTO, int groupId, int userId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);

        if (user == null || group == null) {
            return "User or group not found";
        }

        if ("closed".equalsIgnoreCase(group.getStatus())) {
            if (!group.getMembers().contains(user)) {
                return "User is not a member of the closed group";
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
            return "User or group not found";
        }
        if (post == null) {
            return "Post not found";
        }

        boolean isPostOwner = post.getUser().getId() == userId;
        boolean isGroupAdmin = group.getAdmins().contains(user);
        boolean isGroupMember = group.getMembers().contains(user);

        if ((isGroupAdmin && isGroupMember) || isPostOwner) {
            em.remove(post);
            return "Post removed from group successfully";
        } else {
            return "You are not authorized to remove this post";
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
            return "You are not authorized to delete this group";
        }
    }

    public String DeleteUserFromGroup(int groupId, int adminId, int userId) {
        User user = em.find(User.class, userId);
        User admin = em.find(User.class, adminId);
        Group group = em.find(Group.class, groupId);
        if (user == null || admin == null || group == null) {
            return "User, admin, or group not found";
        }
        boolean isGroupAdmin = group.getAdmins().contains(admin);
        boolean isGroupMember = group.getMembers().contains(user);
        if (!isGroupAdmin) {
            return "You are not authorized to remove this user";
        }
        if (userId == adminId) {
            return "You cannot remove yourself instead you can leave this group";
        }
        if (!isGroupMember) {
            return "User is not a member of the group";
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
            return "User, admin, or group not found";
        }
        boolean isGroupMember = group.getMembers().contains(user);
        boolean isGroupAdmin = group.getAdmins().contains(admin);
        if (!isGroupMember) {
            return "the user with id : " + userId + "is not a member of the group";
        }
        if (userId == adminId) {
            return "you are already an admin";
        }
        if (!isGroupAdmin) {
            return "You are not authorized to promote users";
        } else {
            group.getAdmins().add(user);
            return "user promoted";
        }
    }

    public String LeaveGroup(int groupId, int userId) {
        User user = em.find(User.class, userId);
        Group group = em.find(Group.class, groupId);
        if (user == null || group == null) {
            return "User or group not found";
        }
        boolean isGroupMember = group.getMembers().contains(user);
        boolean isGroupAdmin = group.getAdmins().contains(user);

        if (!isGroupMember) {
            return "user is not a member of that group ";
        }
        if (isGroupAdmin && isGroupMember) {
            group.getAdmins().remove(user);
            group.getMembers().remove(user);
            if (group.getAdmins().size() == 0) {
                if (group.getMembers().size() == 0) {
                    em.remove(group);
                    return "Group removed";
                } else {
                    group.getAdmins().add(group.getMembers().get(0));
                }
            }

            return "Left Group";
        }
        if (isGroupMember) {
            group.getMembers().remove(user);
            return "Left Group";
        }
        em.merge(group);
        return "left Group";
    }

}
