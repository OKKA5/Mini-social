package models;

import jakarta.persistence.*;

@Entity
@Table(name = "friends", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "friend_id"})
})
public class Friend {

    public enum Status {PENDING, ACCEPTED, REJECTED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User requester;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }


    public User getRequester() {
        return requester;
    }

    public void setRequester(User requester) {
        this.requester = requester;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
