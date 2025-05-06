package DTOs;

import java.util.List;

public class PostDTO {
    private int PostID;
    private String Description;
    private String ImageURL;
    private int UserID;
    private List<CommentDTO> comments;
    private List<ReactionDTO> reactions;

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
