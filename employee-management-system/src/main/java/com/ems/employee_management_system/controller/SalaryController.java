package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.model.SalarySlip;
import com.ems.employee_management_system.repository.AttendanceRepository;
import com.ems.employee_management_system.repository.EmployeeRepository;
import com.ems.employee_management_system.repository.SalarySlipRepository;
import com.ems.employee_management_system.service.NotificationService;
import com.ems.employee_management_system.utils.AuthHelper;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/salary")
public class SalaryController {

    @Autowired private SalarySlipRepository salaryRepo;
    @Autowired private EmployeeRepository   empRepo;
    @Autowired private AttendanceRepository attendanceRepo;
    @Autowired private NotificationService  notificationService;
    @Autowired private AuthHelper           authHelper;

    // Admin: list all salary slips
    @GetMapping
    public String salaryPage(Model model, Authentication auth) {
        boolean isAdmin = authHelper.isAdmin(auth);
        if (isAdmin) {
            model.addAttribute("slips",     salaryRepo.findAllByOrderByPayPeriodDesc());
            model.addAttribute("employees", empRepo.findAll());
        } else {
            Employee emp = authHelper.getLinkedEmployee(auth);
            if (emp != null) {
                model.addAttribute("slips",    salaryRepo.findByEmployeeIdOrderByPayPeriodDesc(emp.getId()));
                model.addAttribute("employee", emp);
            } else {
                model.addAttribute("noProfile", true);
                model.addAttribute("slips", List.of());
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "salary";
    }

    // Admin: Generate salary slip
    @PostMapping("/generate")
    public String generate(@RequestParam String employeeId,
                           @RequestParam String payPeriod,   // "2026-06"
                           @RequestParam double basicSalary,
                           @RequestParam(defaultValue = "0") double hra,
                           @RequestParam(defaultValue = "0") double transportAllowance,
                           @RequestParam(defaultValue = "0") double medicalAllowance,
                           @RequestParam(defaultValue = "0") double otherAllowances,
                           @RequestParam(defaultValue = "0") double providentFund,
                           @RequestParam(defaultValue = "0") double professionalTax,
                           @RequestParam(defaultValue = "0") double incomeTax,
                           @RequestParam(defaultValue = "0") double otherDeductions,
                           @RequestParam(defaultValue = "26") int workingDays,
                           Authentication auth,
                           RedirectAttributes ra) {

        Employee emp = empRepo.findById(employeeId).orElse(null);
        if (emp == null) { ra.addFlashAttribute("error", "Employee not found."); return "redirect:/salary"; }

        // Get attendance for pay period
        List<com.ems.employee_management_system.model.Attendance> attendance =
            attendanceRepo.findByEmployeeId(employeeId);
        long presentDays = attendance.stream()
            .filter(a -> a.getDate() != null && a.getDate().startsWith(payPeriod))
            .filter(a -> "Present".equals(a.getStatus()) || "Half Day".equals(a.getStatus()))
            .count();

        double grossSalary   = basicSalary + hra + transportAllowance + medicalAllowance + otherAllowances;
        double totalDeductions = providentFund + professionalTax + incomeTax + otherDeductions;
        double netSalary     = grossSalary - totalDeductions;

        // Parse month name
        String monthName = payPeriod;
        try {
            java.time.YearMonth ym = java.time.YearMonth.parse(payPeriod);
            monthName = ym.getMonth().getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH)
                + " " + ym.getYear();
        } catch (Exception ignored) {}

        SalarySlip slip = new SalarySlip();
        slip.setEmployeeId(employeeId);
        slip.setEmployeeName(emp.getName());
        slip.setEmployeeCode(emp.getEmployeeCode());
        slip.setDepartment(emp.getDepartment());
        slip.setPosition(emp.getPosition());
        slip.setMonth(monthName);
        slip.setPayPeriod(payPeriod);
        slip.setBasicSalary(basicSalary);
        slip.setHra(hra);
        slip.setTransportAllowance(transportAllowance);
        slip.setMedicalAllowance(medicalAllowance);
        slip.setOtherAllowances(otherAllowances);
        slip.setGrossSalary(grossSalary);
        slip.setProvidentFund(providentFund);
        slip.setProfessionalTax(professionalTax);
        slip.setIncomeTax(incomeTax);
        slip.setOtherDeductions(otherDeductions);
        slip.setTotalDeductions(totalDeductions);
        slip.setNetSalary(netSalary);
        slip.setWorkingDays(workingDays);
        slip.setPresentDays((int) presentDays);
        slip.setLeaveDays(workingDays - (int) presentDays);
        slip.setGeneratedOn(LocalDate.now().toString());
        slip.setGeneratedBy(auth.getName());
        salaryRepo.save(slip);

        // Notify employee
        if (emp.getUserId() != null) {
            notificationService.sendToUser(emp.getUserId(), "Payroll",
                "Salary Slip - " + monthName,
                "Your salary slip for " + monthName + " has been generated. Net Pay: Rs." + (long) netSalary,
                "SUCCESS", "/salary");
        }

        ra.addFlashAttribute("success", "Salary slip generated for " + emp.getName());
        return "redirect:/salary";
    }

    // Download salary slip as PDF
    @GetMapping("/download/{id}")
    public void downloadSlip(@PathVariable String id, HttpServletResponse response) throws IOException {
        SalarySlip slip = salaryRepo.findById(id).orElse(null);
        if (slip == null) { response.sendError(404); return; }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
            "attachment; filename=salary-slip-" + slip.getPayPeriod() + ".pdf");

        PdfWriter writer   = new PdfWriter(response.getOutputStream());
        PdfDocument pdf    = new PdfDocument(writer);
        Document doc       = new Document(pdf);

        // Header
        doc.add(new Paragraph("SALARY SLIP")
            .setBold().setFontSize(18).setTextAlignment(TextAlignment.CENTER));
        doc.add(new Paragraph(slip.getMonth())
            .setFontSize(13).setTextAlignment(TextAlignment.CENTER).setMarginBottom(10));

        // Employee Info Table
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
            .useAllAvailableWidth().setMarginBottom(15);

        addInfoRow(infoTable, "Employee Name", slip.getEmployeeName());
        addInfoRow(infoTable, "Employee Code", slip.getEmployeeCode() != null ? slip.getEmployeeCode() : "-");
        addInfoRow(infoTable, "Department",    slip.getDepartment() != null ? slip.getDepartment() : "-");
        addInfoRow(infoTable, "Position",      slip.getPosition() != null ? slip.getPosition() : "-");
        addInfoRow(infoTable, "Working Days",  String.valueOf(slip.getWorkingDays()));
        addInfoRow(infoTable, "Present Days",  String.valueOf(slip.getPresentDays()));
        doc.add(infoTable);

        // Earnings & Deductions
        Table earningsTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
            .useAllAvailableWidth().setMarginBottom(15);

        // Headers
        addHeaderCell(earningsTable, "Earnings");
        addHeaderCell(earningsTable, "Amount (Rs.)");
        addHeaderCell(earningsTable, "Deductions");
        addHeaderCell(earningsTable, "Amount (Rs.)");

        addSalaryRow(earningsTable, "Basic Salary",          slip.getBasicSalary(),
                                    "Provident Fund",        slip.getProvidentFund());
        addSalaryRow(earningsTable, "HRA",                   slip.getHra(),
                                    "Professional Tax",      slip.getProfessionalTax());
        addSalaryRow(earningsTable, "Transport Allowance",   slip.getTransportAllowance(),
                                    "Income Tax",            slip.getIncomeTax());
        addSalaryRow(earningsTable, "Medical Allowance",     slip.getMedicalAllowance(),
                                    "Other Deductions",      slip.getOtherDeductions());
        addSalaryRow(earningsTable, "Other Allowances",      slip.getOtherAllowances(),
                                    "", 0);
        doc.add(earningsTable);

        // Net Pay
        Table netTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1}))
            .useAllAvailableWidth();
        netTable.addCell(new Cell().add(new Paragraph("Gross Salary").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY).setPadding(8));
        netTable.addCell(new Cell().add(new Paragraph("Total Deductions").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY).setPadding(8));
        netTable.addCell(new Cell().add(new Paragraph("Net Pay").setBold()).setBackgroundColor(ColorConstants.LIGHT_GRAY).setPadding(8));
        netTable.addCell(new Cell().add(new Paragraph("Rs." + (long) slip.getGrossSalary()).setBold()).setPadding(8));
        netTable.addCell(new Cell().add(new Paragraph("Rs." + (long) slip.getTotalDeductions())).setPadding(8));
        netTable.addCell(new Cell().add(new Paragraph("Rs." + (long) slip.getNetSalary()).setBold().setFontColor(new DeviceRgb(0,100,0))).setPadding(8));
        doc.add(netTable);

        doc.add(new Paragraph("\nGenerated on: " + slip.getGeneratedOn() + " | Generated by: " + slip.getGeneratedBy())
            .setFontSize(9).setFontColor(ColorConstants.GRAY).setTextAlignment(TextAlignment.RIGHT));
        doc.close();
    }

    private void addInfoRow(Table t, String label, String value) {
        t.addCell(new Cell().add(new Paragraph(label).setBold()).setPadding(7).setBackgroundColor(ColorConstants.LIGHT_GRAY));
        t.addCell(new Cell().add(new Paragraph(value)).setPadding(7));
    }

    private void addHeaderCell(Table t, String text) {
        t.addCell(new Cell().add(new Paragraph(text).setBold().setFontColor(ColorConstants.WHITE))
            .setBackgroundColor(ColorConstants.DARK_GRAY).setPadding(8));
    }

    private void addSalaryRow(Table t, String e1, double a1, String e2, double a2) {
        t.addCell(new Cell().add(new Paragraph(e1)).setPadding(7));
        t.addCell(new Cell().add(new Paragraph(a1 > 0 ? "Rs." + (long) a1 : "-")).setPadding(7));
        t.addCell(new Cell().add(new Paragraph(e2)).setPadding(7));
        t.addCell(new Cell().add(new Paragraph(a2 > 0 ? "Rs." + (long) a2 : "-")).setPadding(7));
    }
}
