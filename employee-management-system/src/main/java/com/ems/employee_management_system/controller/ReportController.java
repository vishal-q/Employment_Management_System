package com.ems.employee_management_system.controller;

import com.ems.employee_management_system.model.Employee;
import com.ems.employee_management_system.service.EmployeeService;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {

    private final EmployeeService service;

    public ReportController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/downloadEmployees")
    public void downloadEmployees(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=employee-report.pdf");

        PdfWriter writer = new PdfWriter(response.getOutputStream());
        PdfDocument pdf  = new PdfDocument(writer);
        Document doc     = new Document(pdf);

        // Title
        Paragraph title = new Paragraph("Employee Report")
            .setFontSize(20)
            .setBold()
            .setTextAlignment(TextAlignment.CENTER);
        doc.add(title);

        Paragraph date = new Paragraph("Generated: " + LocalDate.now())
            .setFontSize(10)
            .setTextAlignment(TextAlignment.CENTER)
            .setMarginBottom(20);
        doc.add(date);

        // Table
        Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 3, 2, 2, 2}))
            .useAllAvailableWidth();

        // Header
        String[] headers = {"#", "Name", "Email", "Department", "Position", "Salary"};
        for (String h : headers) {
            table.addHeaderCell(new Cell()
                .add(new Paragraph(h).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(ColorConstants.DARK_GRAY)
                .setPadding(8));
        }

        // Data rows
        List<Employee> employees = service.getAllEmployees();
        for (int i = 0; i < employees.size(); i++) {
            Employee emp = employees.get(i);
            table.addCell(new Cell().add(new Paragraph(String.valueOf(i + 1))).setPadding(7));
            table.addCell(new Cell().add(new Paragraph(emp.getName() != null ? emp.getName() : "")).setPadding(7));
            table.addCell(new Cell().add(new Paragraph(emp.getEmail() != null ? emp.getEmail() : "")).setPadding(7));
            table.addCell(new Cell().add(new Paragraph(emp.getDepartment() != null ? emp.getDepartment() : "")).setPadding(7));
            table.addCell(new Cell().add(new Paragraph(emp.getPosition() != null ? emp.getPosition() : "")).setPadding(7));
            table.addCell(new Cell().add(new Paragraph("Rs." + (long) emp.getSalary())).setPadding(7));
        }

        doc.add(table);

        Paragraph footer = new Paragraph("Total Employees: " + employees.size())
            .setFontSize(11).setBold().setMarginTop(15);
        doc.add(footer);

        doc.close();
    }
}
