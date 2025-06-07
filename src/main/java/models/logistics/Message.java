package models.logistics;

import java.time.LocalDateTime;

public class Message {
    private String sender;
    private String recipient; // Module destinataire
    private String content;
    private LocalDateTime timestamp;

    public Message(String sender, String recipient, String content) {
        this.sender = sender;
        this.recipient = recipient;
        this.content = content;
        this.timestamp = LocalDateTime.now();
    }

    public String getSender() { return sender; }
    public String getRecipient() { return recipient; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("[%s] %s à %s: %s", timestamp, sender, recipient, content);
    }
}