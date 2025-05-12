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
    @Path("/update")
    public String updatePost(@QueryParam("userId") int userId,@QueryParam("postId") int postId, Post post) {
         return postBean.updatePost(userId,postId, post);
    }

    @DELETE
    @Path("/delete")
    public String deletePost(@QueryParam("userId") int userId,@QueryParam("postId") int postId) {
        return postBean.deletePost(userId,postId);
    }


    @POST
    @Path("/comment")
    public String addComment(@QueryParam("userId") int userId,@QueryParam("postId") int postId, Comment comment) {
        return postBean.addCommentToPost(userId,postId, comment);
    }
    @POST
    @Path("/reaction")
    public String addComment(@QueryParam("userId") int userId,@QueryParam("postId") int postId, Reaction reaction) {
        return postBean.addReactionToPost(userId,postId, reaction);
    }

    @GET
    @Path("feed/{id}")
    public List<PostDTO> getAllPosts(@PathParam("id") int userId) {
        return postBean.findAllPosts(userId);
    }

}
