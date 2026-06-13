package com.ems.employee_management_system.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "salary_slips")
public class SalarySlip {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String employeeCode;
    private String department;
    private String position;

    private String month;       // e.g. "June 2026"
    private String payPeriod;   // "2026-06"

    private double basicSalary;
    private double hra;               // House Rent Allowance
    private double transportAllowance;
    private double medicalAllowance;
    private double otherAllowances;
    private double grossSalary;

    // Deductions
    private double providentFund;     // PF
    private double professionalTax;
    private double incomeTax;
    private double otherDeductions;
    private double totalDeductions;

    private double netSalary;

    private int workingDays;
    private int presentDays;
    private int leaveDays;

    private String generatedOn;
    private String generatedBy;
    private String status = "Generated";  // Generated, Paid

    public SalarySlip() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }
    public String getPayPeriod() { return payPeriod; }
    public void setPayPeriod(String payPeriod) { this.payPeriod = payPeriod; }
    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }
    public double getHra() { return hra; }
    public void setHra(double hra) { this.hra = hra; }
    public double getTransportAllowance() { return transportAllowance; }
    public void setTransportAllowance(double transportAllowance) { this.transportAllowance = transportAllowance; }
    public double getMedicalAllowance() { return medicalAllowance; }
    public void setMedicalAllowance(double medicalAllowance) { this.medicalAllowance = medicalAllowance; }
    public double getOtherAllowances() { return otherAllowances; }
    public void setOtherAllowances(double otherAllowances) { this.otherAllowances = otherAllowances; }
    public double getGrossSalary() { return grossSalary; }
    public void setGrossSalary(double grossSalary) { this.grossSalary = grossSalary; }
    public double getProvidentFund() { return providentFund; }
    public void setProvidentFund(double providentFund) { this.providentFund = providentFund; }
    public double getProfessionalTax() { return professionalTax; }
    public void setProfessionalTax(double professionalTax) { this.professionalTax = professionalTax; }
    public double getIncomeTax() { return incomeTax; }
    public void setIncomeTax(double incomeTax) { this.incomeTax = incomeTax; }
    public double getOtherDeductions() { return otherDeductions; }
    public void setOtherDeductions(double otherDeductions) { this.otherDeductions = otherDeductions; }
    public double getTotalDeductions() { return totalDeductions; }
    public void setTotalDeductions(double totalDeductions) { this.totalDeductions = totalDeductions; }
    public double getNetSalary() { return netSalary; }
    public void setNetSalary(double netSalary) { this.netSalary = netSalary; }
    public int getWorkingDays() { return workingDays; }
    public void setWorkingDays(int workingDays) { this.workingDays = workingDays; }
    public int getPresentDays() { return presentDays; }
    public void setPresentDays(int presentDays) { this.presentDays = presentDays; }
    public int getLeaveDays() { return leaveDays; }
    public void setLeaveDays(int leaveDays) { this.leaveDays = leaveDays; }
    public String getGeneratedOn() { return generatedOn; }
    public void setGeneratedOn(String generatedOn) { this.generatedOn = generatedOn; }
    public String getGeneratedBy() { return generatedBy; }
    public void setGeneratedBy(String generatedBy) { this.generatedBy = generatedBy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
