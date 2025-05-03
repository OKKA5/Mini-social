package ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.User;

@Stateless
public class UserBean {
    @PersistenceContext
    EntityManager em;
    public User createUser(User user) {
        em.persist(user);
        return user;
    }
    public User findUser(int id) {
        return em.find(User.class, id);
    }
    public User updateUser(User user) {
        return em.merge(user);
    }
    public void deleteUser(int id) {
        User u = em.find(User.class, id);
        if (u != null) {
            em.remove(u);
        }
    }
}
