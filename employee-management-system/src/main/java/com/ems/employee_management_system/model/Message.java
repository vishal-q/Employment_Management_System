package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "messages")
public class Message {

    @Id
    private String id;

    private String fromUserId;
    private String fromName;
    private String fromRole;       // ADMIN or EMPLOYEE

    private String toUserId;       // null means to all admins
    private String toName;

    private String subject;
    private String body;
    private String replyToId;      // null if original message

    private boolean readByRecipient = false;
    private String sentAt;

    public Message() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromUserId() { return fromUserId; }
    public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }

    public String getFromName() { return fromName; }
    public void setFromName(String fromName) { this.fromName = fromName; }

    public String getFromRole() { return fromRole; }
    public void setFromRole(String fromRole) { this.fromRole = fromRole; }

    public String getToUserId() { return toUserId; }
    public void setToUserId(String toUserId) { this.toUserId = toUserId; }

    public String getToName() { return toName; }
    public void setToName(String toName) { this.toName = toName; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }

    public String getReplyToId() { return replyToId; }
    public void setReplyToId(String replyToId) { this.replyToId = replyToId; }

    public boolean isReadByRecipient() { return readByRecipient; }
    public void setReadByRecipient(boolean readByRecipient) { this.readByRecipient = readByRecipient; }

    public String getSentAt() { return sentAt; }
    public void setSentAt(String sentAt) { this.sentAt = sentAt; }
}
