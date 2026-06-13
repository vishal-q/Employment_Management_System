package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String toUserId;       // recipient user id
    private String toRole;         // "ADMIN" or "EMPLOYEE" or "ALL"
    private String fromName;       // sender display name
    private String title;
    private String message;
    private String type;           // INFO, SUCCESS, WARNING, LEAVE, TASK, MESSAGE, WFH, COMPLAINT
    private String link;           // optional redirect link
    private boolean read = false;
    private String createdAt;

    public Notification() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getToUserId() { return toUserId; }
    public void setToUserId(String toUserId) { this.toUserId = toUserId; }

    public String getToRole() { return toRole; }
    public void setToRole(String toRole) { this.toRole = toRole; }

    public String getFromName() { return fromName; }
    public void setFromName(String fromName) { this.fromName = fromName; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}
