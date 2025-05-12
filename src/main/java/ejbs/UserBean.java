package ejbs;

import DTOs.FriendDTO;
import DTOs.UserDTO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import models.Friend;
import models.User;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class UserBean {
    @PersistenceContext
    EntityManager em;

    public String registerUser(User user) {
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", user.getEmail());
        List<User> existingUsers = query.getResultList();
        if (!existingUsers.isEmpty()) {
            return "Email is already registered";
        }
        em.persist(user);
        return "User registered successfully!";
    }

    public UserDTO findUser(int userId) {
        User user = em.find(User.class, userId);
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getBio(),
                user.getRole()
        );

        List<Friend> accepted = em.createQuery(
                        "SELECT f FROM Friend f " +
                                " WHERE (f.requester.id = :uid OR f.receiver.id = :uid) " +
                                "   AND f.status = :accepted",
                        Friend.class)
                .setParameter("uid", userId)
                .setParameter("accepted", Friend.Status.ACCEPTED)
                .getResultList();

        // 4) map them into FriendDTOs
        List<FriendDTO> friendsDTOs = new ArrayList<>(accepted.size());
        for (Friend f : accepted) {
            // figure out which side is "me" and which is "the other"
            User friendUser = (f.getRequester().getId() == userId)
                    ? f.getReceiver()
                    : f.getRequester();

            friendsDTOs.add(new FriendDTO(
                    friendUser.getId(),
                    friendUser.getName()
            ));
        }
        userDTO.setFriends(friendsDTOs);

        return userDTO;
    }


    public String updateUser(int id, User user) {
        User u = em.find(User.class, id);
        if(u != null) {
            if(user.getName() != null) {
                u.setName(user.getName());
            }
            if(user.getPassword() != null) {
                u.setPassword(user.getPassword());
            }
            if(user.getBio() != null) {
                u.setBio(user.getBio());
            }
            if(user.getEmail() != null) {
                u.setEmail(user.getEmail());
            }
            em.merge(u);
            return "User updated successfully";
        }else{
            return "nothing to be updated";
        }

    }
    public String userLogin(String email, String password) {
        List<User> users = em.createQuery(
                        "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();

        if (users.isEmpty()) {
            throw new IllegalArgumentException("Wrong email");
        }

        User user = users.get(0);
        if (user.getPassword().equals(password)) {
            return "User logged in successfully";
        } else {
            throw new IllegalArgumentException("Incorrect password");
        }
    }
}



