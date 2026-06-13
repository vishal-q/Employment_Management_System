package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "performance_reviews")
public class PerformanceReview {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String department;
    private String reviewedBy;

    private String reviewPeriod;    // "Q1 2026", "Annual 2026"
    private String reviewType;      // Quarterly, Annual, Probation

    // Ratings (1-5)
    private int technicalSkills;
    private int communication;
    private int teamwork;
    private int punctuality;
    private int productivity;
    private double overallRating;   // Average

    private String strengths;
    private String improvements;
    private String comments;
    private String goal;            // Goal for next period

    private String status = "Draft";  // Draft, Shared
    private String createdOn;

    public PerformanceReview() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(String reviewedBy) { this.reviewedBy = reviewedBy; }
    public String getReviewPeriod() { return reviewPeriod; }
    public void setReviewPeriod(String reviewPeriod) { this.reviewPeriod = reviewPeriod; }
    public String getReviewType() { return reviewType; }
    public void setReviewType(String reviewType) { this.reviewType = reviewType; }
    public int getTechnicalSkills() { return technicalSkills; }
    public void setTechnicalSkills(int technicalSkills) { this.technicalSkills = technicalSkills; }
    public int getCommunication() { return communication; }
    public void setCommunication(int communication) { this.communication = communication; }
    public int getTeamwork() { return teamwork; }
    public void setTeamwork(int teamwork) { this.teamwork = teamwork; }
    public int getPunctuality() { return punctuality; }
    public void setPunctuality(int punctuality) { this.punctuality = punctuality; }
    public int getProductivity() { return productivity; }
    public void setProductivity(int productivity) { this.productivity = productivity; }
    public double getOverallRating() { return overallRating; }
    public void setOverallRating(double overallRating) { this.overallRating = overallRating; }
    public String getStrengths() { return strengths; }
    public void setStrengths(String strengths) { this.strengths = strengths; }
    public String getImprovements() { return improvements; }
    public void setImprovements(String improvements) { this.improvements = improvements; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public String getGoal() { return goal; }
    public void setGoal(String goal) { this.goal = goal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreatedOn() { return createdOn; }
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
}
