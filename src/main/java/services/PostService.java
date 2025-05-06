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
    private PostDTO toPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostID(post.getPostID());
        postDTO.setUserID(post.getUserID());
        postDTO.setDescription(post.getDescription());
        postDTO.setImageURL(post.getImageURL());

        postDTO.setComments(post.getComments().stream()
                .map(comment -> new CommentDTO(comment.getCommentId(), comment.getComment(), comment.getAuthor(), comment.getDate()))
                .toList());

        postDTO.setReactions(post.getReactions().stream()
                .map(reaction -> new ReactionDTO(reaction.getReactionId(), reaction.getReaction(), reaction.getAuthor(), reaction.getDate()))
                .toList());

        return postDTO;
    }

    @GET
    @Path("{id}")
    public PostDTO getPost(@PathParam("id") int postId) {
        Post post = postBean.findPost(postId);
        return post != null ? toPostDTO(post) : null;
    }

    @POST
    @Path("{id}")
    public PostDTO createPost(@PathParam("id")int UserID, Post post) {
        Post createdPost = postBean.createPost(UserID,post);
        return toPostDTO(createdPost);
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
    @Path("/comment/{id}")
    public String addComment(@PathParam("id") int postId, CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setAuthor(commentDTO.getAuthor());
        comment.setComment(commentDTO.getComment());
        comment.setDate(commentDTO.getDate());
        postBean.addCommentToPost(postId, comment);
        return "Comment added successfully";
    }
    @POST
    @Path("/reaction/{id}")
    public String addComment(@PathParam("id") int postId, ReactionDTO reactionDTO) {
        // Create a Comment entity from the DTO
        Reaction reaction = new Reaction();
        reaction.setAuthor(reactionDTO.getAuthor());
        reaction.setReaction(reactionDTO.getReaction());
        reaction.setDate(reactionDTO.getDate());
        postBean.addReactionToPost(postId, reaction);
        return "Reaction added successfully";
    }

    @GET
    @Path("allposts/{id}")
    public List<PostDTO> getAllPosts(@PathParam("id") int userId) {
        List<Post> posts = postBean.findAllPosts(userId);
        return posts.stream()
                .map(this::toPostDTO)
                .toList();
    }

}
