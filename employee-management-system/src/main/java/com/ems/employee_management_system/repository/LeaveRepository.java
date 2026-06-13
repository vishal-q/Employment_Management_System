package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.LeaveRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LeaveRepository extends MongoRepository<LeaveRequest, String> {

    List<LeaveRequest> findByEmployeeId(String employeeId);
    List<LeaveRequest> findByStatus(String status);
    List<LeaveRequest> findByEmployeeIdOrderByAppliedOnDesc(String employeeId);
    long countByStatus(String status);
}
