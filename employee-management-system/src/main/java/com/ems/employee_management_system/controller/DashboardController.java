package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.repository.*;
import com.ems.employee_management_system.service.AttendanceService;
import com.ems.employee_management_system.service.LeaveService;
import com.ems.employee_management_system.utils.AuthHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired private EmployeeRepository  employeeRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private LeaveRepository     leaveRepository;
    @Autowired private AttendanceRepository attendanceRepository;
    @Autowired private AttendanceService   attendanceService;
    @Autowired private LeaveService        leaveService;
    @Autowired private AuthHelper          authHelper;

    @GetMapping("/")
    public String root() { return "redirect:/dashboard"; }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {

        String username = resolveUsername(auth);
        boolean isAdmin = authHelper.isAdmin(auth);

        model.addAttribute("username", username);
        model.addAttribute("userRole", isAdmin ? "ADMIN" : "EMPLOYEE");

        if (isAdmin) {
            model.addAttribute("totalEmployees",   employeeRepository.count());
            model.addAttribute("totalDepartments", departmentRepository.count());
            model.addAttribute("totalLeaves",      leaveRepository.count());
            model.addAttribute("totalAttendance",  attendanceRepository.count());
            model.addAttribute("pendingLeaves",    leaveService.countPending());
            model.addAttribute("presentToday",     attendanceService.countPresentToday());
            model.addAttribute("activeEmployees",  employeeRepository.countByStatus("Active"));

            List<Employee> all = employeeRepository.findAll();
            int size = Math.min(all.size(), 5);
            model.addAttribute("recentEmployees",
                all.subList(Math.max(0, all.size() - size), all.size()));

        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            model.addAttribute("employee", emp);

            if (emp != null) {
                model.addAttribute("myAttendanceCount",
                    attendanceRepository.findByEmployeeId(emp.getId()).size());
                model.addAttribute("myLeaveCount",
                    leaveRepository.findByEmployeeId(emp.getId()).size());
                model.addAttribute("myPendingLeaves",
                    leaveRepository.findByEmployeeId(emp.getId())
                        .stream().filter(l -> "Pending".equals(l.getStatus())).count());
            }
        }

        return "dashboard";
    }

    private String resolveUsername(Authentication auth) {
        if (auth == null) return "User";
        Object p = auth.getPrincipal();
        if (p instanceof OAuth2User o) {
            String name = o.getAttribute("name");
            return name != null ? name : (o.getAttribute("email") != null ? o.getAttribute("email") : "User");
        }
        return auth.getName();
    }
}
