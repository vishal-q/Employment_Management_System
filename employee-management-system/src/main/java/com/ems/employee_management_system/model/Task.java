package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tasks")
public class Task {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String department;
    private String assignedBy;     // Admin name
    private String title;
    private String description;
    private String priority;       // High, Medium, Low
    private String dueDate;
    private String status = "Pending";  // Pending, In Progress, Completed, Overdue
    private String employeeNote;   // Employee can add note
    private String assignedOn;
    private String completedOn;

    public Task() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getAssignedBy() { return assignedBy; }
    public void setAssignedBy(String assignedBy) { this.assignedBy = assignedBy; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getEmployeeNote() { return employeeNote; }
    public void setEmployeeNote(String employeeNote) { this.employeeNote = employeeNote; }
    public String getAssignedOn() { return assignedOn; }
    public void setAssignedOn(String assignedOn) { this.assignedOn = assignedOn; }
    public String getCompletedOn() { return completedOn; }
    public void setCompletedOn(String completedOn) { this.completedOn = completedOn; }
}
