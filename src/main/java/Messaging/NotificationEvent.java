package Messaging;

public class NotificationEvent {
    private String eventType;
    private String sender;
    private String recipient;
    private String content;

    // Constructors, Getters, Setters

    public NotificationEvent() {}

    public NotificationEvent(String eventType, String sender, String recipient, String content) {
        this.eventType = eventType;
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
    }

    // toString for logging or JSON conversion
    @Override
    public String toString() {
        return "{" +
                "\"eventType\": \"" + eventType + "\"," +
                "\"sender\": \"" + sender + "\"," +
                "\"recipient\": \"" + recipient + "\"," +
                "\"content\": \"" + content + "\"," +
                "}";
    }
}
