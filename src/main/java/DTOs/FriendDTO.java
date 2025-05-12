package DTOs;

public class FriendDTO {
    private int Id;
    private String friendName;

    public FriendDTO() {}

    public FriendDTO(int Id, String friendName) {
        this.Id = Id;
        this.friendName = friendName;
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
}
