package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employees")
public class Employee {

    @Id
    private String id;

    // Basic Info
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String dateOfBirth;
    private String bloodGroup;
    private String address;

    // Job Info
    private String department;
    private String position;
    private String employeeCode;      // EMP-001
    private String dateOfJoining;
    private String employmentType;    // Full-Time, Part-Time, Contract
    private String status;            // Active, Inactive

    // Salary
    private double salary;
    private double basicSalary;
    private double allowances;

    // Leave Balance
    private int sickLeaveBalance    = 12;
    private int casualLeaveBalance  = 12;
    private int earnedLeaveBalance  = 15;

    // Link to User account
    private String userId;   // User.id jo is employee ke liye register hua

    private String photo;

    public Employee() {}

    // ---- Getters & Setters ----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public String getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(String dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getAllowances() { return allowances; }
    public void setAllowances(double allowances) { this.allowances = allowances; }

    public int getSickLeaveBalance() { return sickLeaveBalance; }
    public void setSickLeaveBalance(int sickLeaveBalance) { this.sickLeaveBalance = sickLeaveBalance; }

    public int getCasualLeaveBalance() { return casualLeaveBalance; }
    public void setCasualLeaveBalance(int casualLeaveBalance) { this.casualLeaveBalance = casualLeaveBalance; }

    public int getEarnedLeaveBalance() { return earnedLeaveBalance; }
    public void setEarnedLeaveBalance(int earnedLeaveBalance) { this.earnedLeaveBalance = earnedLeaveBalance; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
}
