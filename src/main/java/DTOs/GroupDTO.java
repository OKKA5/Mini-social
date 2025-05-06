package DTOs;

import java.util.List;

public class GroupDTO {
    private int id;
    private String name;
    private String description;
    private String creator; // username of the creator
    private String status; // "open" or "closed"
    private List<String> members; // usernames of members
    private List<String> admins;  // usernames of admins

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
