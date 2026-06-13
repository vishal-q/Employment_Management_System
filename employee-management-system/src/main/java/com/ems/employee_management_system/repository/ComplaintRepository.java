package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Complaint;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    List<Complaint> findByEmployeeIdOrderBySubmittedOnDesc(String employeeId);
    List<Complaint> findByStatusOrderBySubmittedOnDesc(String status);
    List<Complaint> findAllByOrderBySubmittedOnDesc();
    long countByStatus(String status);
}
