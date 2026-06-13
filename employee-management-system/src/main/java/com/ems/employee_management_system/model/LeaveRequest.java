package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "leave_requests")
public class LeaveRequest {

    @Id
    private String id;

    private String employeeId;    // Employee.id reference
    private String employeeName;  // Denormalized
    private String department;
    private String leaveType;     // Sick Leave, Casual Leave, Earned Leave, etc.
    private String fromDate;
    private String toDate;
    private int    totalDays;
    private String reason;
    private String status = "Pending";   // Pending, Approved, Rejected
    private String adminRemarks;         // Admin comment on approve/reject
    private String appliedOn;            // Date of application

    public LeaveRequest() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }

    public String getFromDate() { return fromDate; }
    public void setFromDate(String fromDate) { this.fromDate = fromDate; }

    public String getToDate() { return toDate; }
    public void setToDate(String toDate) { this.toDate = toDate; }

    public int getTotalDays() { return totalDays; }
    public void setTotalDays(int totalDays) { this.totalDays = totalDays; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    // Keep old field for compatibility
    public String getReasonDisplay() {
        return leaveType != null ? leaveType : reason;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminRemarks() { return adminRemarks; }
    public void setAdminRemarks(String adminRemarks) { this.adminRemarks = adminRemarks; }

    public String getAppliedOn() { return appliedOn; }
    public void setAppliedOn(String appliedOn) { this.appliedOn = appliedOn; }
}
