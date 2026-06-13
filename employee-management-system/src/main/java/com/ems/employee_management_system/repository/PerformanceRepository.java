package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.PerformanceReview;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface PerformanceRepository extends MongoRepository<PerformanceReview, String> {
    List<PerformanceReview> findByEmployeeIdOrderByCreatedOnDesc(String employeeId);
    List<PerformanceReview> findAllByOrderByCreatedOnDesc();
}
