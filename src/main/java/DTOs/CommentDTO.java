package DTOs;

public class CommentDTO {
    private int commentId;
    private String comment;
    private String author;
    private String date;

    public CommentDTO(int commentId, String comment, String author, String date) {
        this.commentId = commentId;
        this.comment = comment;
        this.author = author;
        this.date = date;
    }
    public CommentDTO(){

    }

    // Getters and setters
    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
