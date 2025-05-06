package ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.Post;
import models.Comment;
import models.Reaction;
import models.User;

import java.util.List;

@Stateless
public class PostBean {
    @PersistenceContext
    private EntityManager em;

    public Post findPost(int postId) {
        Post p = em.find(Post.class, postId);
        if (p == null) {
            throw new IllegalArgumentException("Post not found with id: " + postId);
        } else {
            return p;
        }
    }

    public List<Post> findAllPosts(int userId) {
        User user = em.find(User.class, userId);

        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        } else {
            return em.createQuery("SELECT p FROM Post p WHERE p.UserID = :userId", Post.class)
                    .setParameter("userId", userId)
                    .getResultList();

        }

    }


    public Post createPost(int UserID, Post post) {
        Post p = em.find(Post.class, UserID);
        if (p == null) throw new IllegalArgumentException("User not found");
        if (post.getComments() != null) {
            for (Comment comment : post.getComments()) {
                comment.setPost(post);
            }
        }

        if (post.getReactions() != null) {
            for (Reaction reaction : post.getReactions()) {
                reaction.setPost(post);
            }
        }
        post.setUserID(UserID);
        em.persist(post);
        return post;
    }

    public Post updatePost(int postId, Post post) {
        Post existingPost = em.find(Post.class, postId);
        if (existingPost != null) {
            existingPost.setDescription(post.getDescription());
            existingPost.setImageURL(post.getImageURL());
            em.merge(existingPost);
            return existingPost;
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    public void deletePost(int postId) {
        Post post = em.find(Post.class, postId);
        if (post != null) {
            em.remove(post);
        } else {
            throw new IllegalArgumentException("Post not found");
        }
    }

    public void addCommentToPost(int postId, Comment comment) {
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
        comment.setPost(post);
        post.getComments().add(comment);
        em.persist(comment);
    }

    public void addReactionToPost(int postId, Reaction reaction) {
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
        reaction.setPost(post);
        post.getReactions().add(reaction);
        em.persist(reaction);
    }


}
