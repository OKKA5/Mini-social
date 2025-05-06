package models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;


import java.util.Set;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique=true)
    private String email;
    @Column(nullable=false)
    private String password;
    @Column(nullable=false)
    private String name;
    @Column(nullable=true)
    @Size(max=100)
    private String bio;
    @Column(nullable=false)
    private String role;


    @OneToMany(mappedBy = "requester", fetch = FetchType.LAZY)
    private Set<Friend> outgoingFriendRequests;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private Set<Friend> incomingFriendRequests;

    @OneToMany(mappedBy = "user" , fetch = FetchType.LAZY)
    private Set<Post> posts;


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public Set<Friend> getOutgoingFriendRequests() {
        return outgoingFriendRequests;
    }

    public void setOutgoingFriendRequests(Set<Friend> outgoingFriendRequests) {
        this.outgoingFriendRequests = outgoingFriendRequests;
    }

    public Set<Friend> getIncomingFriendRequests() {
        return incomingFriendRequests;
    }

    public void setIncomingFriendRequests(Set<Friend> incomingFriendRequests) {
        this.incomingFriendRequests = incomingFriendRequests;
    }
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

}