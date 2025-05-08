package DTOs;

import models.GroupJoinRequest;

public class GroupJoinRequestDTO {

    private int requestId;
    private int userId;
    private String userName;
    private GroupJoinRequest.Status status;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GroupJoinRequest.Status getStatus() {
        return status;
    }

    public void setStatus(GroupJoinRequest.Status status) {
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
    public GroupJoinRequestDTO(int requestId, int userId, String userName, GroupJoinRequest.Status status) {
        this.requestId = requestId;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
    }

}