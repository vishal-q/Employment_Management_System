package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Announcement;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface AnnouncementRepository extends MongoRepository<Announcement, String> {
    List<Announcement> findByActiveTrueOrderByCreatedAtDesc();
    List<Announcement> findAllByOrderByCreatedAtDesc();
}
