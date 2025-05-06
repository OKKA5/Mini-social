package DTOs;

public class FriendDTO {
    private int Id;
    private String friendName;
    private String status;

    public FriendDTO() {}

    public FriendDTO(int Id, String friendName, String status) {
        this.Id = Id;
        this.friendName = friendName;
        this.status = status;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
