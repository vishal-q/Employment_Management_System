package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.entity.Department;
import com.ems.employee_management_system.repository.DepartmentRepository;
import com.ems.employee_management_system.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private EmployeeRepository employeeRepository;

    @GetMapping
    public String listDepartments(Model model) {
        List<Department> departments = departmentRepository.findAll();

        // Employee count per department
        Map<String, Long> empCountMap = new HashMap<>();
        for (Department d : departments) {
            empCountMap.put(d.getName(), employeeRepository.countByDepartment(d.getName()));
        }

        model.addAttribute("departments", departments);
        model.addAttribute("empCountMap", empCountMap);
        return "departments";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        return "add-department";
    }

    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute Department department, RedirectAttributes ra) {
        departmentRepository.save(department);
        ra.addFlashAttribute("success", "Department added successfully!");
        return "redirect:/departments";
    }
}
