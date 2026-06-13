package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Attendance;
import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.service.AttendanceService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
public class AttendanceController {

    @Autowired private AttendanceService service;
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private AuthHelper authHelper;

    @GetMapping("/attendance")
    public String attendancePage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);

        List<Attendance> list;
        if (isAdmin) {
            list = service.getAllAttendance();
            model.addAttribute("employees", employeeRepository.findAll());
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                list = service.getByEmployeeId(emp.getId());
                model.addAttribute("employee", emp);
            } else {
                list = List.of();
            }
        }

        model.addAttribute("attendanceList", list);
        model.addAttribute("isAdmin",        isAdmin);
        model.addAttribute("today",          LocalDate.now().toString());
        return "attendance";
    }

    @PostMapping("/saveAttendance")
    public String saveAttendance(@RequestParam String employeeId,
                                 @RequestParam String date,
                                 @RequestParam String status,
                                 @RequestParam(required = false) String remarks,
                                 RedirectAttributes ra) {
        Employee emp = employeeRepository.findById(employeeId).orElse(null);
        if (emp == null) { ra.addFlashAttribute("error", "Employee not found."); return "redirect:/attendance"; }

        Attendance a = new Attendance();
        a.setEmployeeId(emp.getId());
        a.setEmployeeName(emp.getName());
        a.setDepartment(emp.getDepartment());
        a.setDate(date);
        a.setStatus(status);
        a.setRemarks(remarks);
        service.saveAttendance(a);
        ra.addFlashAttribute("success", "Attendance marked for " + emp.getName());
        return "redirect:/attendance";
    }

    @PostMapping("/markMyAttendance")
    public String markMyAttendance(@RequestParam String date,
                                   @RequestParam String status,
                                   Authentication auth,
                                   RedirectAttributes ra) {
        Employee emp = authHelper.getLinkedEmployee(auth);
        if (emp == null) { ra.addFlashAttribute("error", "Profile not linked. Contact Admin."); return "redirect:/attendance"; }

        if (service.alreadyMarked(emp.getId(), date)) {
            ra.addFlashAttribute("error", "Attendance already marked for " + date);
            return "redirect:/attendance";
        }

        Attendance a = new Attendance();
        a.setEmployeeId(emp.getId());
        a.setEmployeeName(emp.getName());
        a.setDepartment(emp.getDepartment());
        a.setDate(date);
        a.setStatus(status);
        service.saveAttendance(a);
        ra.addFlashAttribute("success", "Attendance marked: " + status);
        return "redirect:/attendance";
    }
}
