package DTOs;

public class GroupJoinRequestDTO {
    private int requestId;
    private int userId;
    private String userName;
    private String status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    // Constructor
    public GroupJoinRequestDTO(int requestId, int userId, String userName, String status) {
        this.requestId = requestId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

}