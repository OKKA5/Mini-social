package ejbs;

import DTOs.GroupDTO;

import jakarta.ejb.Stateless;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import models.Group;
import models.GroupJoinRequest;
import models.User;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class GroupBean {

    @PersistenceContext
    private EntityManager em;


    public Group createGroup(int UserId, GroupDTO groupDTO) {
        User creator = em.find(User.class, UserId);
        if (creator == null) {
            throw new IllegalArgumentException("User not found");
        }

        Group group = new Group();
        group.setName(groupDTO.getName());
        group.setDescription(groupDTO.getDescription());
        group.setStatus(groupDTO.getStatus());
        group.setCreator(creator);

        List<User> initialMembers = new ArrayList<>();
        initialMembers.add(creator);
        group.setMembers(initialMembers);

        List<User> admins = new ArrayList<>();
        admins.add(creator);
        group.setAdmins(admins);

        em.persist(group);
        return group;
    }

    public Group findGroupById(int groupId) {
        return em.find(Group.class, groupId);
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
            // Send JMS notification
        }
    }

    public void leaveGroup(int groupId, int userId) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        group.getMembers().remove(user);
        group.getAdmins().remove(user); // also remove from admins if needed
        em.merge(group);
    }

    public void deleteGroup(int groupId) {
        Group group = em.find(Group.class, groupId);
        if (group != null) {
            em.remove(group);
        }
    }

    public void promoteToAdmin(int groupId, int userId) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);
        if (!group.getAdmins().contains(user) && group.getMembers().contains(user)) {
            group.getAdmins().add(user);
            em.merge(group);
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
            group.getMembers().add(user);
            em.merge(group);
            return "Joined group directly (open group)";
        }

        GroupJoinRequest request = new GroupJoinRequest();
        request.setUser(user);
        request.setGroup(group);
        request.setStatus("pending");
        em.persist(request);
        return "Request sent and pending admin approval";
    }

    public String approveRequest(int requestId) {
        GroupJoinRequest request = em.find(GroupJoinRequest.class, requestId);
        if (request == null || !"pending".equals(request.getStatus())) return "Invalid request";

        Group group = request.getGroup();
        User user = request.getUser();

        group.getMembers().add(user);
        request.setStatus("approved");

        em.merge(group);
        em.merge(request);


        return "Request approved";
    }

    public String rejectRequest(int requestId) {
        GroupJoinRequest request = em.find(GroupJoinRequest.class, requestId);
        if (request == null || !"pending".equals(request.getStatus())) return "Invalid request";

        request.setStatus("rejected");
        em.merge(request);
        return "Request rejected";
    }

    public List<GroupJoinRequest> getRequestsForGroup(int groupId) {
        Group group = em.find(Group.class, groupId);
        if (group == null) {
            return List.of(); // or throw exception
        }

        return em.createQuery("SELECT r FROM GroupJoinRequest r WHERE r.group.id = :groupId AND r.status = 'pending'", GroupJoinRequest.class).setParameter("groupId", groupId).getResultList();
    }


}
