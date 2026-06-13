package com.ems.employee_management_system.service;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.LeaveRequest;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.repository.LeaveRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private com.ems.employee_management_system.service.NotificationService notificationService;

    public LeaveService(LeaveRepository leaveRepository, EmployeeRepository employeeRepository) {
        this.leaveRepository    = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<LeaveRequest> getAllLeaves() {
        return leaveRepository.findAll();
    }

    public List<LeaveRequest> getLeavesByEmployee(String employeeId) {
        return leaveRepository.findByEmployeeIdOrderByAppliedOnDesc(employeeId);
    }

    public List<LeaveRequest> getPendingLeaves() {
        return leaveRepository.findByStatus("Pending");
    }

    public long countPending() {
        return leaveRepository.countByStatus("Pending");
    }

    public LeaveRequest applyLeave(LeaveRequest leave) {
        // Calculate total days
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate from = LocalDate.parse(leave.getFromDate(), fmt);
            LocalDate to   = LocalDate.parse(leave.getToDate(), fmt);
            int days = (int) ChronoUnit.DAYS.between(from, to) + 1;
            leave.setTotalDays(days);
        } catch (Exception e) {
            leave.setTotalDays(1);
        }
        leave.setStatus("Pending");
        leave.setAppliedOn(LocalDate.now().toString());
        return leaveRepository.save(leave);
    }

    public void approveLeave(String id, String adminRemarks) {
        Optional<LeaveRequest> opt = leaveRepository.findById(id);
        if (opt.isPresent()) {
            LeaveRequest leave = opt.get();
            leave.setStatus("Approved");
            leave.setAdminRemarks(adminRemarks);
            leaveRepository.save(leave);

            // Notify employee
            if (leave.getEmployeeId() != null) {
                notificationService.sendToUser(leave.getEmployeeId(), "Admin",
                    "Leave Approved!",
                    "Your " + leave.getLeaveType() + " request (" + leave.getFromDate() + " to " + leave.getToDate() + ") has been approved.",
                    "SUCCESS", "/leave");
            }

            // Deduct leave balance
            if (leave.getEmployeeId() != null) {
                Optional<Employee> empOpt = employeeRepository.findById(leave.getEmployeeId());
                empOpt.ifPresent(emp -> {
                    int days = leave.getTotalDays() > 0 ? leave.getTotalDays() : 1;
                    String type = leave.getLeaveType();
                    if (type != null) {
                        if (type.contains("Sick"))     emp.setSickLeaveBalance(Math.max(0, emp.getSickLeaveBalance() - days));
                        else if (type.contains("Casual")) emp.setCasualLeaveBalance(Math.max(0, emp.getCasualLeaveBalance() - days));
                        else if (type.contains("Earned"))  emp.setEarnedLeaveBalance(Math.max(0, emp.getEarnedLeaveBalance() - days));
                    }
                    employeeRepository.save(emp);
                });
            }
        }
    }

    public void rejectLeave(String id, String adminRemarks) {
        Optional<LeaveRequest> opt = leaveRepository.findById(id);
        if (opt.isPresent()) {
            LeaveRequest leave = opt.get();
            leave.setStatus("Rejected");
            leave.setAdminRemarks(adminRemarks);
            leaveRepository.save(leave);

            // Notify employee
            if (leave.getEmployeeId() != null) {
                notificationService.sendToUser(leave.getEmployeeId(), "Admin",
                    "Leave Request Rejected",
                    "Your " + leave.getLeaveType() + " request has been rejected." +
                    (adminRemarks != null ? " Reason: " + adminRemarks : ""),
                    "WARNING", "/leave");
            }
        }
    }
}
