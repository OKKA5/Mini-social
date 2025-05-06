package ejbs;

import DTOs.FriendDTO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.Friend;
import models.User;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class FriendBean {

    @PersistenceContext
    private EntityManager em;

    public List<FriendDTO> viewConnections(int userId) {
        List<Friend> friends = em.createQuery(
                        "SELECT f FROM Friend f WHERE f.requester.id = :userId AND f.status = :status", Friend.class)
                .setParameter("userId", userId)
                .setParameter("status", Friend.Status.ACCEPTED)
                .getResultList();

        List<FriendDTO> friendDTOs = new ArrayList<>();
        for (Friend friend : friends) {
            friendDTOs.add(new FriendDTO(
                    friend.getReceiver().getId(),
                    friend.getReceiver().getName(),
                    friend.getStatus().toString()
            ));
        }
        return friendDTOs;
    }

    public List<FriendDTO> getIncomingRequests(int userId) {
        List<Friend> incoming = em.createQuery(
                        "SELECT f FROM Friend f WHERE f.receiver.id = :userId AND f.status = :status", Friend.class)
                .setParameter("userId", userId)
                .setParameter("status", Friend.Status.PENDING)
                .getResultList();

        List<FriendDTO> result = new ArrayList<>();
        for (Friend f : incoming) {
            result.add(new FriendDTO(
                    f.getRequester().getId(),
                    f.getRequester().getName(),
                    f.getStatus().toString()
            ));
        }
        return result;
    }

    public List<FriendDTO> getOutgoingRequests(int userId) {
        List<Friend> outgoing = em.createQuery(
                        "SELECT f FROM Friend f WHERE f.requester.id = :userId AND f.status = :status", Friend.class)
                .setParameter("userId", userId)
                .setParameter("status", Friend.Status.PENDING)
                .getResultList();

        List<FriendDTO> result = new ArrayList<>();
        for (Friend f : outgoing) {
            result.add(new FriendDTO(
                    f.getReceiver().getId(),
                    f.getReceiver().getName(),
                    f.getStatus().toString()
            ));
        }
        return result;
    }

    public String friendRequest(int requesterId, int receiverId) {
        if (requesterId == receiverId) return "You cannot send a friend request to yourself.";

        User requester = em.find(User.class, requesterId);
        User receiver = em.find(User.class, receiverId);

        if (requester == null || receiver == null) return "User not found.";

        Long count = em.createQuery(
                        "SELECT COUNT(f) FROM Friend f WHERE f.requester.id = :requesterId AND f.receiver.id = :receiverId",
                        Long.class)
                .setParameter("requesterId", requesterId)
                .setParameter("receiverId", receiverId)
                .getSingleResult();

        if (count > 0) return "Friend request already exists or users are already connected.";
        Friend requestEntry = new Friend();
        requestEntry.setRequester(requester);
        requestEntry.setReceiver(receiver);
        requestEntry.setStatus(Friend.Status.PENDING);
        em.persist(requestEntry);

        Friend reciprocalEntry = new Friend();
        reciprocalEntry.setRequester(receiver);
        reciprocalEntry.setReceiver(requester);
        reciprocalEntry.setStatus(Friend.Status.PENDING);
        em.persist(reciprocalEntry);

        return "Friend request sent.";
    }

    public String acceptFriendRequest(int requesterId, int receiverId) {
        List<Friend> receiverEntries = em.createQuery(
                        "SELECT f FROM Friend f WHERE f.requester.id = :receiverId AND f.receiver.id = :requesterId AND f.status = :status",
                        Friend.class)
                .setParameter("receiverId", receiverId)
                .setParameter("requesterId", requesterId)
                .setParameter("status", Friend.Status.PENDING)
                .getResultList();

        if (receiverEntries.isEmpty()) return "No pending friend request found.";

        Friend receiverEntry = receiverEntries.get(0);
        receiverEntry.setStatus(Friend.Status.ACCEPTED);
        em.merge(receiverEntry);

        return "Friend request accepted.";
    }

    public String rejectFriendRequest(int requesterId, int receiverId) {
        List<Friend> receiverEntries = em.createQuery(
                        "SELECT f FROM Friend f WHERE f.requester.id = :receiverId AND f.receiver.id = :requesterId AND f.status = :status",
                        Friend.class)
                .setParameter("receiverId", receiverId)
                .setParameter("requesterId", requesterId)
                .setParameter("status", Friend.Status.PENDING)
                .getResultList();

        if (receiverEntries.isEmpty()) return "No pending friend request found.";

        Friend receiverEntry = receiverEntries.get(0);
        receiverEntry.setStatus(Friend.Status.REJECTED);
        em.merge(receiverEntry);

        List<Friend> requesterEntries = em.createQuery(
                        "SELECT f FROM Friend f WHERE f.requester.id = :requesterId AND f.receiver.id = :receiverId",
                        Friend.class)
                .setParameter("requesterId", requesterId)
                .setParameter("receiverId", receiverId)
                .getResultList();

        if (!requesterEntries.isEmpty()) {
            Friend requesterEntry = requesterEntries.get(0);
            requesterEntry.setStatus(Friend.Status.REJECTED);
            em.merge(requesterEntry);
        }

        return "Friend request rejected.";
    }
}
