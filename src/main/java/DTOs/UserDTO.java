package DTOs;

import java.util.List;


public class UserDTO{
    private int id;
    private String email;
    private String name;
    private String bio;
    private String role;

    private List<FriendDTO> friends;
    private List<PostDTO> posts;


    // Constructors
    public UserDTO() {}

    public UserDTO(int id, String email, String name, String bio, String role) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.bio = bio;
        this.role = role;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public List<FriendDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendDTO> friends) {
        this.friends = friends;
    }
    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }
}