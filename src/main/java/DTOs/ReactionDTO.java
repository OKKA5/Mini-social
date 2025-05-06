package DTOs;

public class ReactionDTO {
    private int reactionId;
    private String reaction;
    private String author;
    private String date;

    public ReactionDTO(int reactionId, String reaction, String author, String date) {
        this.reactionId = reactionId;
        this.reaction = reaction;
        this.author = author;
        this.date = date;
    }
    public ReactionDTO(){

    }

    // Getters and setters
    public int getReactionId() {
        return reactionId;
    }

    public void setReactionId(int reactionId) {
        this.reactionId = reactionId;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
