package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.entity.Department;
import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.User;
import com.ems.employee_management_system.repository.DepartmentRepository;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EmployeeController {

    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // ---- Show all employees ----
    @GetMapping("/employees")
    public String getEmployees(@RequestParam(required = false) String keyword,
                               @RequestParam(required = false) String department,
                               Model model) {
        List<Employee> employees;
        if (keyword != null && !keyword.trim().isEmpty()) {
            employees = employeeRepository.findByNameContainingIgnoreCase(keyword.trim());
        } else if (department != null && !department.trim().isEmpty()) {
            employees = employeeRepository.findByDepartment(department.trim());
        } else {
            employees = employeeRepository.findAll();
        }
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedDept", department);
        model.addAttribute("departments", departmentRepository.findAll());
        return "employees";
    }

    // ---- Add employee page ----
    @GetMapping("/addEmployee")
    public String showAddPage(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("departments", departmentRepository.findAll());
        return "add-employee";
    }

    // ---- Save employee ----
    @PostMapping("/saveEmployee")
    public String saveEmployee(@ModelAttribute Employee employee,
                               @RequestParam(required = false) String createAccount,
                               @RequestParam(required = false) String empUsername,
                               @RequestParam(required = false) String empPassword,
                               RedirectAttributes ra) {

        // Auto-generate employee code if blank
        if (employee.getEmployeeCode() == null || employee.getEmployeeCode().isBlank()) {
            long count = employeeRepository.count() + 1;
            employee.setEmployeeCode("EMP-" + String.format("%03d", count));
        }

        if (employee.getStatus() == null || employee.getStatus().isBlank()) {
            employee.setStatus("Active");
        }

        // Create linked User account if checkbox checked
        if ("on".equals(createAccount) && empUsername != null && !empUsername.isBlank()
                && empPassword != null && !empPassword.isBlank()) {
            User user = new User();
            user.setName(employee.getName());
            user.setEmail(employee.getEmail());
            user.setUsername(empUsername.trim());
            user.setPassword(passwordEncoder.encode(empPassword));
            user.setRole("EMPLOYEE");
            User savedUser = userRepository.save(user);
            employee.setUserId(savedUser.getId());
        }

        employeeRepository.save(employee);
        ra.addFlashAttribute("success", "Employee added successfully!");
        return "redirect:/employees";
    }

    // ---- View employee profile ----
    @GetMapping("/employees/{id}")
    public String viewEmployee(@PathVariable String id, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) return "redirect:/employees";
        model.addAttribute("employee", employee);
        return "employee-profile";
    }

    // ---- Edit employee page ----
    @GetMapping("/editEmployee/{id}")
    public String editEmployee(@PathVariable String id, Model model) {
        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee == null) return "redirect:/employees";
        model.addAttribute("employee", employee);
        model.addAttribute("departments", departmentRepository.findAll());
        return "edit-employee";
    }

    // ---- Update employee ----
    @PostMapping("/employees/update")
    public String updateEmployee(@ModelAttribute Employee employee, RedirectAttributes ra) {
        // Preserve userId if already set
        Employee existing = employeeRepository.findById(employee.getId()).orElse(null);
        if (existing != null && existing.getUserId() != null && employee.getUserId() == null) {
            employee.setUserId(existing.getUserId());
        }
        employeeRepository.save(employee);
        ra.addFlashAttribute("success", "Employee updated successfully!");
        return "redirect:/employees";
    }

    // ---- Delete employee ----
    @PostMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable String id, RedirectAttributes ra) {
        employeeRepository.deleteById(id);
        ra.addFlashAttribute("success", "Employee deleted.");
        return "redirect:/employees";
    }
}
