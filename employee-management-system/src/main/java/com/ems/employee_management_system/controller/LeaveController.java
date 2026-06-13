package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.LeaveRequest;
import com.ems.employee_management_system.service.LeaveService;
import com.ems.employee_management_system.service.NotificationService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class LeaveController {

    @Autowired private LeaveService leaveService;
    @Autowired private NotificationService notificationService;
    @Autowired private AuthHelper authHelper;

    @GetMapping("/leave")
    public String leavePage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);

        if (isAdmin) {
            model.addAttribute("leaveList",    leaveService.getAllLeaves());
            model.addAttribute("pendingCount", leaveService.countPending());
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                model.addAttribute("leaveList", leaveService.getLeavesByEmployee(emp.getId()));
                model.addAttribute("employee",  emp);
            } else {
                model.addAttribute("leaveList",  List.of());
                model.addAttribute("noProfile", true);
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "leave";
    }

    @PostMapping("/applyLeave")
    public String applyLeave(@RequestParam String leaveType,
                             @RequestParam String fromDate,
                             @RequestParam String toDate,
                             @RequestParam(required = false) String reason,
                             Authentication auth,
                             RedirectAttributes ra) {
        Employee emp = authHelper.getLinkedEmployee(auth);
        if (emp == null) {
            ra.addFlashAttribute("error", "Your profile is not linked. Contact Admin.");
            return "redirect:/leave";
        }

        LeaveRequest leave = new LeaveRequest();
        leave.setEmployeeId(emp.getId());
        leave.setEmployeeName(emp.getName());
        leave.setDepartment(emp.getDepartment());
        leave.setLeaveType(leaveType);
        leave.setFromDate(fromDate);
        leave.setToDate(toDate);
        leave.setReason(reason);
        leaveService.applyLeave(leave);

        notificationService.sendToAdmins(emp.getName(),
            "Leave Request: " + emp.getName(),
            emp.getName() + " applied for " + leaveType + " (" + fromDate + " to " + toDate + ")",
            "LEAVE", "/leave");

        ra.addFlashAttribute("success", "Leave request submitted!");
        return "redirect:/leave";
    }

    @PostMapping("/approveLeave/{id}")
    public String approveLeave(@PathVariable String id,
                               @RequestParam(required = false) String adminRemarks,
                               RedirectAttributes ra) {
        leaveService.approveLeave(id, adminRemarks);
        ra.addFlashAttribute("success", "Leave approved.");
        return "redirect:/leave";
    }

    @PostMapping("/rejectLeave/{id}")
    public String rejectLeave(@PathVariable String id,
                              @RequestParam(required = false) String adminRemarks,
                              RedirectAttributes ra) {
        leaveService.rejectLeave(id, adminRemarks);
        ra.addFlashAttribute("success", "Leave rejected.");
        return "redirect:/leave";
    }
}
