package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.WorkFromHomeRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface WorkFromHomeRepository extends MongoRepository<WorkFromHomeRequest, String> {
    List<WorkFromHomeRequest> findByEmployeeIdOrderByAppliedOnDesc(String employeeId);
    List<WorkFromHomeRequest> findByStatusOrderByAppliedOnDesc(String status);
    List<WorkFromHomeRequest> findAllByOrderByAppliedOnDesc();
    long countByStatus(String status);
}
