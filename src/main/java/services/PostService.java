package services;

import DTOs.CommentDTO;
import DTOs.ReactionDTO;
import ejbs.PostBean;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import DTOs.PostDTO;
import models.Comment;
import models.Post;
import models.Reaction;

import java.util.List;

@Stateless
@Path("/post")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostService {
    @EJB
    private PostBean postBean;

    // Convert Post entity to PostDTO


    @GET
    @Path("{id}")
    public PostDTO getPost(@PathParam("id") int postId) {
        return postBean.findPost(postId);
    }

    @POST
    @Path("{id}")
    public String createPost(@PathParam("id")int UserID, Post post) {
        return postBean.createPost(UserID,post);
    }

    @PUT
    @Path("{id}")
    public String updatePost(@PathParam("id") int postId, Post post) {
        Post updatedPost = postBean.updatePost(postId, post);
        return "Post updated successfully";
    }

    @DELETE
    @Path("{id}")
    public String deletePost(@PathParam("id") int postId) {
        postBean.deletePost(postId);
        return "Post deleted successfully";
    }


    @POST
    @Path("/comment")
    public String addComment(@QueryParam("userId") int userId,@QueryParam("postId") int postId, Comment comment) {
        postBean.addCommentToPost(userId,postId, comment);
        return "Comment added successfully";
    }
    @POST
    @Path("/reaction")
    public String addComment(@QueryParam("userId") int userId,@QueryParam("postId") int postId, Reaction reaction) {
        postBean.addReactionToPost(userId,postId, reaction);
        return "Reaction added successfully";
    }

    @GET
    @Path("feed/{id}")
    public List<PostDTO> getAllPosts(@PathParam("id") int userId) {
        return postBean.findAllPosts(userId);
    }

}
