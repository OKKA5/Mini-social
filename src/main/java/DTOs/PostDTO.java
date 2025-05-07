package DTOs;

import models.Post;

import java.util.List;

public class PostDTO {
    private int PostID;
    private String Description;
    private String ImageURL;
    private int UserID;
    private List<CommentDTO> comments;
    private List<ReactionDTO> reactions;

    public PostDTO() {}

    public PostDTO(int postID, String description, String imageURL, int userID,
                   List<CommentDTO> comments, List<ReactionDTO> reactions) {
        this.PostID = postID;
        this.Description = description;
        this.ImageURL = imageURL;
        this.UserID = userID;
        this.comments = comments;
        this.reactions = reactions;
    }
    public PostDTO toPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();
        postDTO.setPostID(post.getPostID());
        postDTO.setUserID(post.getUser().getId());
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
    // Getters and setters
    public int getPostID() {
        return PostID;
    }

    public void setPostID(int postID) {
        PostID = postID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public List<CommentDTO> getComments() {
        return comments;
    }

    public void setComments(List<CommentDTO> comments) {
        this.comments = comments;
    }

    public List<ReactionDTO> getReactions() {
        return reactions;
    }

    public void setReactions(List<ReactionDTO> reactions) {
        this.reactions = reactions;
    }
}
