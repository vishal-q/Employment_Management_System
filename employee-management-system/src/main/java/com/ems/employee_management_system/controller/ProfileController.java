package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Attendance;
import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.LeaveRequest;
import com.ems.employee_management_system.repository.AttendanceRepository;
import com.ems.employee_management_system.repository.LeaveRepository;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ProfileController {

    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private LeaveRepository leaveRepository;
    @Autowired private AuthHelper authHelper;

    @GetMapping("/my-profile")
    public String myProfile(Model model, Authentication auth) {
        Employee emp = authHelper.getLinkedEmployee(auth);
        if (emp == null) {
            model.addAttribute("noProfile", true);
            return "my-profile";
        }

        List<Attendance>   attendance = attendanceRepository.findByEmployeeId(emp.getId());
        List<LeaveRequest> leaves     = leaveRepository.findByEmployeeId(emp.getId());

        long presentCount   = attendance.stream().filter(a -> "Present".equals(a.getStatus())).count();
        long absentCount    = attendance.stream().filter(a -> "Absent".equals(a.getStatus())).count();
        long approvedLeaves = leaves.stream().filter(l -> "Approved".equals(l.getStatus())).count();
        long pendingLeaves  = leaves.stream().filter(l -> "Pending".equals(l.getStatus())).count();

        model.addAttribute("employee",       emp);
        model.addAttribute("attendanceList", attendance);
        model.addAttribute("leaveList",      leaves);
        model.addAttribute("presentCount",   presentCount);
        model.addAttribute("absentCount",    absentCount);
        model.addAttribute("approvedLeaves", approvedLeaves);
        model.addAttribute("pendingLeaves",  pendingLeaves);
        return "my-profile";
    }
}
