package DTOs;

import models.Group;
import models.User;

import java.util.List;
import java.util.Set;

public class GroupDTO {
    private int id;
    private String name;
    private String description;
    private String creator; // username of the creator
    private String status; // "open" or "closed"
    private List<String> members; // usernames of members
    private List<String> admins;
    private List<GroupJoinRequestDTO> requests;


    private List<PostDTO> posts;
    // usernames of admins

    public GroupDTO toGroupDTO(Group group) {
        GroupDTO dto = new GroupDTO();

        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setDescription(group.getDescription());
        dto.setCreator(group.getCreator().getName());
        dto.setStatus(group.getStatus());

        List<String> memberNames = group.getMembers().stream()
                .map(User::getName)
                .toList();
        dto.setMembers(memberNames);

        // Convert admin users to list of usernames
        List<String> adminNames = group.getAdmins().stream()
                .map(User::getName)
                .toList();
        dto.setAdmins(adminNames);

        if (group.getRequests() != null) {
            dto.setRequests(group.getRequests().stream()
                    .map(request -> new GroupJoinRequestDTO(
                            request.getId(),
                            request.getUser().getId(),
                            request.getUser().getName(),
                            request.getStatus()))
                    .toList());
        } else {
            dto.setRequests(List.of());
        }
        if (group.getPosts() != null) {
            dto.setPosts(group.getPosts().stream()
                    .map(post -> new PostDTO().toPostDTO(post))
                    .toList());
        } else {
            dto.setPosts(List.of());
        }
        return dto;
    }

    // Constructors
    public GroupDTO() {
    }

    public GroupDTO(int id, String name, String description, String creator, String status,
                    List<String> members, List<String> admins) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator = creator;
        this.status = status;
        this.members = members;
        this.admins = admins;
    }

    // Getters and Setters
    public List<PostDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTO> posts) {
        this.posts = posts;
    }

    public List<GroupJoinRequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(List<GroupJoinRequestDTO> requests) {
        this.requests = requests;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    public List<String> getAdmins() {
        return admins;
    }

    public void setAdmins(List<String> admins) {
        this.admins = admins;
    }
}
