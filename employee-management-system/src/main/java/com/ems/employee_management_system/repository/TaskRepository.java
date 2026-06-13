package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByEmployeeIdOrderByAssignedOnDesc(String employeeId);
    List<Task> findByStatusOrderByAssignedOnDesc(String status);
    List<Task> findAllByOrderByAssignedOnDesc();
    long countByStatus(String status);
    long countByEmployeeIdAndStatus(String employeeId, String status);
}
