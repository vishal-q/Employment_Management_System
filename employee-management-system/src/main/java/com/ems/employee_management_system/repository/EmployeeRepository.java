package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {

    List<Employee> findByNameContainingIgnoreCase(String name);
    List<Employee> findByDepartment(String department);
    List<Employee> findByStatus(String status);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByUserId(String userId);
    long countByDepartment(String department);
    long countByStatus(String status);
}
