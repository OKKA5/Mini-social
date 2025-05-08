package models;

import jakarta.persistence.*;

@Entity
@Table(name = "group_join_requests")
public class GroupJoinRequest {
    public enum Status {ACCEPTED, REJECTED, PENDING}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;

    @Column(nullable = false)
    private Status status;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


}

