package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByToUserIdOrderByCreatedAtDesc(String toUserId);
    List<Notification> findByToRoleOrderByCreatedAtDesc(String toRole);
    List<Notification> findByToUserIdAndReadFalse(String toUserId);
    long countByToUserIdAndReadFalse(String toUserId);
    long countByToRoleAndReadFalse(String toRole);
}
