package models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PostID;
    @Column(nullable = false)
    private String Description;
    @Column(nullable = true)
    private String ImageURL;

    public void setUserID(int userID) {
        UserID = userID;
    }

    @Column(nullable = false)
    private int UserID;
    public int getUserID() {
        return UserID;
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> Comments = new ArrayList<Comment>();


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reaction> reactions = new ArrayList<Reaction>();


    public List<Reaction> getReactions() {
        return reactions;
    }

    public void setReactions(List<Reaction> reactions) {
        this.reactions = reactions;
    }


    public int getPostID() {
        return PostID;
    }

    public void setPostID(int postID) {
        PostID = postID;
    }

    public List<Comment> getComments() {
        return Comments;
    }

    public void setComments(List<Comment> comments) {
        Comments = comments;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
