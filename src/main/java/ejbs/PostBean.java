package ejbs;

import DTOs.CommentDTO;
import DTOs.PostDTO;
import DTOs.ReactionDTO;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import models.*;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class PostBean {
    @PersistenceContext
    private EntityManager em;


    public PostDTO findPost(int postId) {
        Post p = em.find(Post.class, postId);
        if (p == null) {
            throw new IllegalArgumentException("Post not found with id: " + postId);
        } else {
            return new PostDTO().toPostDTO(p);
        }
    }

    public List<PostDTO> findAllPosts(int userId) {
        User user = em.find(User.class, userId);

        if (user == null) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        } else {
            List <Post> userPosts = em.createQuery("SELECT p FROM Post p WHERE p.user.id = :userId", Post.class)
                    .setParameter("userId", userId)
                    .getResultList();
            List<Post> requesterFriendsPosts = em.createQuery("""
            SELECT p FROM Post p
            WHERE p.user.id IN (
            SELECT f.receiver.id FROM Friend f 
            WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
                            )
            """, Post.class).setParameter("userId", userId).getResultList();

            List<Post> receiverFriendsPosts = em.createQuery("""
            SELECT p FROM Post p
            WHERE p.user.id IN (
            SELECT f.requester.id FROM Friend f 
            WHERE f.receiver.id = :userId AND f.status = 'ACCEPTED'
                            )
            """, Post.class).setParameter("userId", userId).getResultList();

            List<Post> userFriendsPosts = new ArrayList<>();
            userFriendsPosts.addAll(requesterFriendsPosts);
            userFriendsPosts.addAll(receiverFriendsPosts);
            List <Post> feed = new ArrayList<>();
            feed.addAll(userPosts);
            feed.addAll(userFriendsPosts);


            List <PostDTO> postDTOs = new ArrayList<>();
            for (Post p : feed) {
                PostDTO dto = new PostDTO().toPostDTO(p);
                postDTOs.add(dto.toPostDTO(p));
            }
            return postDTOs;
        }

    }


    public String createPost(int UserID, Post post) {
        User u = em.find(User.class, UserID);
        if (u == null) throw new IllegalArgumentException("User not found");
        post.setUser(u);
        em.persist(post);

        return "Post created successfully";
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

    public void addCommentToPost(int userId,int postId, Comment comment) {
        Post post = em.find(Post.class, postId);
        User user = em.find(User.class, userId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
        comment.setCommenterId(user.getId());
        comment.setAuthor(user.getName());
        comment.setPost(post);
        post.getComments().add(comment);
        em.persist(comment);
    }

    public void addReactionToPost(int userId,int postId, Reaction reaction) {
        User user = em.find(User.class, userId);
        Post post = em.find(Post.class, postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found with ID: " + postId);
        }
        reaction.setReactorId(user.getId());
        reaction.setAuthor(user.getName());
        reaction.setPost(post);
        post.getReactions().add(reaction);
        em.persist(reaction);
    }

    public String addPostToGroup(int userId, int groupId, String description, String imageUrl) {
        Group group = em.find(Group.class, groupId);
        User user = em.find(User.class, userId);

        if (group == null || user == null) {
            return "Group or User not found";
        }

        if (!group.getMembers().contains(user)) {
            return "User is not a member of the group";
        }

        Post post = new Post();
        post.setDescription(description);
        post.setImageURL(imageUrl);
        post.setUser(user);
        post.setGroups(group);

        em.persist(post);
        return "Post added successfully";
    }




}
