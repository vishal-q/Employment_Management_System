package com.ems.employee_management_system.service;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public Employee saveEmployee(Employee employee) {
        return repository.save(employee);
    }

    public void deleteEmployee(String id) {
        repository.deleteById(id);
    }

    public Employee getEmployeeById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Optional<Employee> findByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    public List<Employee> searchEmployee(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public List<Employee> getByDepartment(String department) {
        return repository.findByDepartment(department);
    }

    public long countActive() {
        return repository.countByStatus("Active");
    }
}
