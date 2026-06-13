package com.ems.employee_management_system.service;

import com.ems.employee_management_system.model.Notification;
import com.ems.employee_management_system.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Send to specific user
    public void sendToUser(String toUserId, String fromName,
                           String title, String message, String type, String link) {
        Notification n = new Notification();
        n.setToUserId(toUserId);
        n.setFromName(fromName);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setLink(link);
        n.setCreatedAt(LocalDateTime.now().format(FMT));
        repo.save(n);
    }

    // Send to all admins (by role)
    public void sendToAdmins(String fromName, String title, String message, String type, String link) {
        Notification n = new Notification();
        n.setToRole("ADMIN");
        n.setFromName(fromName);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setLink(link);
        n.setCreatedAt(LocalDateTime.now().format(FMT));
        repo.save(n);
    }

    // Send to all employees (by role)
    public void sendToAllEmployees(String fromName, String title, String message, String type, String link) {
        Notification n = new Notification();
        n.setToRole("EMPLOYEE");
        n.setFromName(fromName);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setLink(link);
        n.setCreatedAt(LocalDateTime.now().format(FMT));
        repo.save(n);
    }

    // Get notifications for a user (personal + role-based)
    public List<Notification> getForUser(String userId, String role) {
        List<Notification> list = new ArrayList<>();
        list.addAll(repo.findByToUserIdOrderByCreatedAtDesc(userId));
        list.addAll(repo.findByToRoleOrderByCreatedAtDesc(role));
        list.sort((a, b) -> {
            if (a.getCreatedAt() == null) return 1;
            if (b.getCreatedAt() == null) return -1;
            return b.getCreatedAt().compareTo(a.getCreatedAt());
        });
        return list;
    }

    public long countUnread(String userId, String role) {
        return repo.countByToUserIdAndReadFalse(userId)
             + repo.countByToRoleAndReadFalse(role);
    }

    public void markAllRead(String userId) {
        List<Notification> unread = repo.findByToUserIdAndReadFalse(userId);
        unread.forEach(n -> { n.setRead(true); repo.save(n); });
    }
}
