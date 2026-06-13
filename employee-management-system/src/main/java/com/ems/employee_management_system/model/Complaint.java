package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "complaints")
public class Complaint {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String department;
    private String category;       // Harassment, Salary Issue, Work Environment, Other
    private String subject;
    private String description;
    private String status = "Open";  // Open, In Progress, Resolved, Closed
    private String adminResponse;
    private String submittedOn;
    private String resolvedOn;
    private boolean anonymous = false;

    public Complaint() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public String getSubmittedOn() { return submittedOn; }
    public void setSubmittedOn(String submittedOn) { this.submittedOn = submittedOn; }

    public String getResolvedOn() { return resolvedOn; }
    public void setResolvedOn(String resolvedOn) { this.resolvedOn = resolvedOn; }

    public boolean isAnonymous() { return anonymous; }
    public void setAnonymous(boolean anonymous) { this.anonymous = anonymous; }
}
