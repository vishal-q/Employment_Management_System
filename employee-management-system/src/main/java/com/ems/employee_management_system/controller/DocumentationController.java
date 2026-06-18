package com.ems.employee_management_system.controller;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
public class DocumentationController {

    // ── Colours ──────────────────────────────────────────────────────────────
    private static final DeviceRgb COLOR_HEADER    = new DeviceRgb(0x66, 0x7E, 0xEA); // #667EEA
    private static final DeviceRgb COLOR_SUBHEADER = new DeviceRgb(0x76, 0x4B, 0xA2); // #764BA2
    private static final DeviceRgb COLOR_ACCENT    = new DeviceRgb(0x1A, 0xBC, 0x9C); // #1ABC9C
    private static final DeviceRgb COLOR_WHITE     = new DeviceRgb(255, 255, 255);
    private static final DeviceRgb COLOR_LIGHT_BG  = new DeviceRgb(245, 245, 250);
    private static final DeviceRgb COLOR_DARK_TEXT  = new DeviceRgb(30,  30,  50);
    private static final DeviceRgb COLOR_GREY_TEXT  = new DeviceRgb(100, 100, 120);
    private static final DeviceRgb COLOR_TABLE_HDR  = new DeviceRgb(0x76, 0x4B, 0xA2);
    private static final DeviceRgb COLOR_ROW_ALT    = new DeviceRgb(237, 233, 254);

    // ── Fonts (lazily loaded per request) ────────────────────────────────────
    private PdfFont fontBold;
    private PdfFont fontRegular;
    private PdfFont fontItalic;
    private PdfFont fontMono;

    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/project-documentation")
    public void downloadDocumentation(HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"EMS-Project-Documentation.pdf\"");

        fontBold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        fontItalic  = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);
        fontMono    = PdfFontFactory.createFont(StandardFonts.COURIER);

        PdfWriter   writer  = new PdfWriter(response.getOutputStream());
        PdfDocument pdf     = new PdfDocument(writer);
        Document    doc     = new Document(pdf, PageSize.A4);
        doc.setMargins(50, 50, 50, 50);

        buildCoverPage(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildTableOfContents(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildProjectOverview(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildTechStack(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildArchitecture(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildDatabaseDesign(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildSecurity(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildFeatures(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildApiEndpoints(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildDeployment(doc);
        doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        buildConclusion(doc);

        doc.close();
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  HELPER – SECTION HEADING
    // ═════════════════════════════════════════════════════════════════════════
    private void addSectionHeading(Document doc, String number, String title) throws IOException {
        doc.add(new Paragraph()
                .add(new Text(number + "  ").setFont(fontBold).setFontSize(22).setFontColor(COLOR_SUBHEADER))
                .add(new Text(title).setFont(fontBold).setFontSize(22).setFontColor(COLOR_HEADER))
                .setMarginBottom(4));

        SolidLine line = new SolidLine(2f);
        line.setColor(COLOR_ACCENT);
        doc.add(new LineSeparator(line).setMarginBottom(14));
    }

    private void addSubHeading(Document doc, String text) throws IOException {
        doc.add(new Paragraph(text)
                .setFont(fontBold).setFontSize(13)
                .setFontColor(COLOR_SUBHEADER)
                .setMarginTop(10).setMarginBottom(4));
    }

    private void addBody(Document doc, String text) throws IOException {
        doc.add(new Paragraph(text)
                .setFont(fontRegular).setFontSize(10.5f)
                .setFontColor(COLOR_DARK_TEXT)
                .setMultipliedLeading(1.5f)
                .setMarginBottom(6));
    }

    private void addBullet(Document doc, String text) throws IOException {
        doc.add(new Paragraph("  •   " + text)
                .setFont(fontRegular).setFontSize(10.5f)
                .setFontColor(COLOR_DARK_TEXT)
                .setMultipliedLeading(1.4f)
                .setMarginBottom(2));
    }

    private void addMonoBlock(Document doc, String text) throws IOException {
        doc.add(new Paragraph(text)
                .setFont(fontMono).setFontSize(9f)
                .setFontColor(COLOR_DARK_TEXT)
                .setBackgroundColor(COLOR_LIGHT_BG)
                .setPadding(10)
                .setMarginBottom(8));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  1. COVER PAGE
    // ═════════════════════════════════════════════════════════════════════════
    private void buildCoverPage(Document doc) throws IOException {
        // Top decorative band
        Table band = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginBottom(0);
        Cell bandCell = new Cell()
                .setBackgroundColor(COLOR_HEADER)
                .setBorder(Border.NO_BORDER)
                .setPadding(28)
                .add(new Paragraph("EMPLOYEE MANAGEMENT SYSTEM")
                        .setFont(fontBold).setFontSize(28)
                        .setFontColor(COLOR_WHITE)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(0));
        band.addCell(bandCell);
        doc.add(band);

        // Subtitle strip
        Table subBand = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(30);
        Cell subCell = new Cell()
                .setBackgroundColor(COLOR_SUBHEADER)
                .setBorder(Border.NO_BORDER)
                .setPaddingTop(10).setPaddingBottom(10)
                .add(new Paragraph("Complete Technical Documentation")
                        .setFont(fontItalic).setFontSize(14)
                        .setFontColor(COLOR_WHITE)
                        .setTextAlignment(TextAlignment.CENTER));
        subBand.addCell(subCell);
        doc.add(subBand);

        doc.add(new Paragraph("Version 1.0.0")
                .setFont(fontBold).setFontSize(12).setFontColor(COLOR_GREY_TEXT)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(4));

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"));
        doc.add(new Paragraph("Generated: " + today)
                .setFont(fontRegular).setFontSize(11).setFontColor(COLOR_GREY_TEXT)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(40));

        // Tech-stack badge table
        doc.add(new Paragraph("TECHNOLOGY STACK")
                .setFont(fontBold).setFontSize(13).setFontColor(COLOR_HEADER)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(10));

        String[] badges = {"Spring Boot 3.2.5", "MongoDB", "Thymeleaf",
                "Spring Security", "OAuth2 / Google", "iTextPDF 7.2.5",
                "Docker", "Render"};
        Table badgeTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                .setWidth(UnitValue.createPercentValue(90))
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setMarginBottom(40);
        for (int i = 0; i < badges.length; i++) {
            DeviceRgb bg = (i % 2 == 0) ? COLOR_HEADER : COLOR_SUBHEADER;
            Cell c = new Cell()
                    .setBackgroundColor(bg)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(8).setMargin(3)
                    .add(new Paragraph(badges[i])
                            .setFont(fontBold).setFontSize(10)
                            .setFontColor(COLOR_WHITE)
                            .setTextAlignment(TextAlignment.CENTER));
            badgeTable.addCell(c);
        }
        doc.add(badgeTable);

        // Divider
        SolidLine sl = new SolidLine(1.5f);
        sl.setColor(COLOR_ACCENT);
        doc.add(new LineSeparator(sl).setMarginBottom(20));

        doc.add(new Paragraph("A full-featured, cloud-deployable Employee Management System built with\n"
                + "Java, Spring Boot, and MongoDB — supporting Admin & Employee roles,\n"
                + "attendance tracking, leave management, salary processing, and more.")
                .setFont(fontRegular).setFontSize(11).setFontColor(COLOR_DARK_TEXT)
                .setTextAlignment(TextAlignment.CENTER).setMultipliedLeading(1.6f));
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  2. TABLE OF CONTENTS
    // ═════════════════════════════════════════════════════════════════════════
    private void buildTableOfContents(Document doc) throws IOException {
        addSectionHeading(doc, "", "Table of Contents");

        String[][] toc = {
            {"1", "Project Overview",     "3"},
            {"2", "Technology Stack",     "4"},
            {"3", "System Architecture",  "5"},
            {"4", "Database Design",      "6"},
            {"5", "Security",             "7"},
            {"6", "Features",             "8"},
            {"7", "API Endpoints",        "9"},
            {"8", "Deployment",           "10"},
            {"9", "Conclusion",           "11"},
        };

        Table t = new Table(UnitValue.createPercentArray(new float[]{6, 80, 14}))
                .setWidth(UnitValue.createPercentValue(100));

        // header row
        for (String h : new String[]{"#", "Section", "Page"}) {
            t.addHeaderCell(new Cell()
                    .setBackgroundColor(COLOR_TABLE_HDR)
                    .setBorder(Border.NO_BORDER).setPadding(8)
                    .add(new Paragraph(h).setFont(fontBold).setFontSize(11).setFontColor(COLOR_WHITE)));
        }

        boolean alt = false;
        for (String[] row : toc) {
            DeviceRgb bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
            t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(7)
                    .add(new Paragraph(row[0]).setFont(fontBold).setFontSize(10.5f).setFontColor(COLOR_HEADER)));
            t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(7)
                    .add(new Paragraph(row[1]).setFont(fontRegular).setFontSize(10.5f).setFontColor(COLOR_DARK_TEXT)));
            t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(7)
                    .add(new Paragraph(row[2]).setFont(fontRegular).setFontSize(10.5f)
                            .setFontColor(COLOR_DARK_TEXT).setTextAlignment(TextAlignment.CENTER)));
            alt = !alt;
        }
        doc.add(t);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  3. PROJECT OVERVIEW
    // ═════════════════════════════════════════════════════════════════════════
    private void buildProjectOverview(Document doc) throws IOException {
        addSectionHeading(doc, "1.", "Project Overview");

        addSubHeading(doc, "What is the Employee Management System?");
        addBody(doc, "The Employee Management System (EMS) is a full-stack web application designed to "
                + "digitise and streamline all HR processes within an organisation. It provides a centralised "
                + "platform where HR administrators and employees can manage day-to-day operational tasks "
                + "efficiently, securely, and from anywhere in the world via a cloud deployment.");

        addSubHeading(doc, "Purpose");
        addBody(doc, "Traditional HR workflows — attendance registers, paper-based leave forms, manual salary "
                + "calculations — are slow and error-prone. EMS replaces these with an automated, role-aware "
                + "digital system that saves time and reduces administrative overhead.");

        addSubHeading(doc, "Project Goals");
        String[] goals = {
            "Provide a secure, role-based portal for Admins and Employees.",
            "Automate attendance tracking, leave approvals, and payroll generation.",
            "Enable real-time communication through announcements and messaging.",
            "Support Google OAuth2 for frictionless, passwordless login.",
            "Deploy to the cloud using Docker containers on Render with MongoDB Atlas.",
            "Generate professional PDF reports and documentation on demand.",
            "Maintain a clean, responsive UI built with Thymeleaf and custom CSS.",
        };
        for (String g : goals) addBullet(doc, g);

        addSubHeading(doc, "Target Users");
        addBullet(doc, "HR Administrators — full system control, approvals, reporting.");
        addBullet(doc, "Employees — self-service portal for attendance, leave, tasks, and salary slips.");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  4. TECHNOLOGY STACK
    // ═════════════════════════════════════════════════════════════════════════
    private void buildTechStack(Document doc) throws IOException {
        addSectionHeading(doc, "2.", "Technology Stack");

        Object[][] techs = {
            {"Spring Boot 3.2.5",
             "An opinionated, production-ready framework built on top of Spring Framework.",
             "Rapid development with auto-configuration, embedded Tomcat server, and a rich ecosystem of starters reduce boilerplate and accelerate delivery."},
            {"MongoDB",
             "A NoSQL, document-oriented database that stores data in flexible BSON documents.",
             "Schema flexibility suits HR data models that evolve over time; horizontal scalability and MongoDB Atlas provide a managed cloud database."},
            {"Thymeleaf",
             "A modern server-side Java template engine for rendering HTML views.",
             "Native Spring Boot integration, natural templating syntax, and rich attribute expressions keep view logic clean and testable."},
            {"Spring Security",
             "A comprehensive authentication and authorisation framework for Spring applications.",
             "Provides form login, role-based access control (ADMIN / EMPLOYEE), CSRF protection, and session management out of the box."},
            {"OAuth2 / Google Login",
             "An industry-standard authorisation protocol enabling third-party identity providers.",
             "Employees can sign in with Google accounts, eliminating password fatigue and improving security through Google's hardened auth infrastructure."},
            {"iTextPDF 7.2.5",
             "A powerful Java PDF creation and manipulation library.",
             "Enables server-side generation of salary slips, attendance reports, and this documentation without any external processes."},
            {"Docker",
             "A containerisation platform that packages applications and their dependencies.",
             "Guarantees environment parity between development and production; a single Dockerfile describes the entire runtime."},
            {"Render",
             "A fully managed cloud platform for deploying web services and containers.",
             "Zero-downtime deploys, automatic TLS, and simple GitHub integration make production deployments straightforward and cost-effective."},
        };

        for (Object[] tech : techs) {
            Table row = new Table(UnitValue.createPercentArray(new float[]{28, 72}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .setMarginBottom(8);

            Cell label = new Cell()
                    .setBackgroundColor(COLOR_HEADER)
                    .setBorder(Border.NO_BORDER).setPadding(10)
                    .add(new Paragraph((String) tech[0])
                            .setFont(fontBold).setFontSize(11).setFontColor(COLOR_WHITE));
            row.addCell(label);

            Cell detail = new Cell()
                    .setBackgroundColor(COLOR_LIGHT_BG)
                    .setBorder(new SolidBorder(COLOR_ACCENT, 1.5f)).setPadding(10)
                    .add(new Paragraph((String) tech[1])
                            .setFont(fontItalic).setFontSize(10).setFontColor(COLOR_SUBHEADER)
                            .setMarginBottom(3))
                    .add(new Paragraph((String) tech[2])
                            .setFont(fontRegular).setFontSize(10).setFontColor(COLOR_DARK_TEXT));
            row.addCell(detail);

            doc.add(row);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  5. SYSTEM ARCHITECTURE
    // ═════════════════════════════════════════════════════════════════════════
    private void buildArchitecture(Document doc) throws IOException {
        addSectionHeading(doc, "3.", "System Architecture");

        addBody(doc, "EMS follows a classic layered (N-tier) architecture with clear separation of concerns "
                + "across five layers. Each layer communicates only with its immediate neighbour, keeping "
                + "the codebase modular and testable.");

        addMonoBlock(doc,
            "  ┌─────────────────────────────────────────────────────┐\n" +
            "  │                    CLIENT BROWSER                   │\n" +
            "  │          (Thymeleaf HTML + CSS + JavaScript)        │\n" +
            "  └──────────────────────┬──────────────────────────────┘\n" +
            "                         │  HTTP/HTTPS\n" +
            "  ┌──────────────────────▼──────────────────────────────┐\n" +
            "  │              PRESENTATION LAYER                     │\n" +
            "  │     Spring MVC Controllers  +  REST Endpoints       │\n" +
            "  └──────────────────────┬──────────────────────────────┘\n" +
            "                         │\n" +
            "  ┌──────────────────────▼──────────────────────────────┐\n" +
            "  │                SERVICE LAYER                        │\n" +
            "  │   Business Logic  •  Validations  •  PDF Generation │\n" +
            "  └──────────────────────┬──────────────────────────────┘\n" +
            "                         │\n" +
            "  ┌──────────────────────▼──────────────────────────────┐\n" +
            "  │              REPOSITORY LAYER                       │\n" +
            "  │       Spring Data MongoDB  (MongoRepository)        │\n" +
            "  └──────────────────────┬──────────────────────────────┘\n" +
            "                         │  BSON / JSON\n" +
            "  ┌──────────────────────▼──────────────────────────────┐\n" +
            "  │                 DATA LAYER                          │\n" +
            "  │          MongoDB Atlas  (Cloud Database)            │\n" +
            "  └─────────────────────────────────────────────────────┘");

        addSubHeading(doc, "Security Cross-Cut");
        addBody(doc, "Spring Security sits as a filter chain in front of every Controller, enforcing "
                + "authentication and role-based authorisation before any business logic executes. "
                + "OAuth2 token exchange is handled transparently via Spring's OAuth2 client support.");

        addSubHeading(doc, "Deployment Architecture");
        addMonoBlock(doc,
            "  GitHub Repository\n" +
            "        │  git push / PR merge\n" +
            "        ▼\n" +
            "  Render.com  ──►  Docker Build  ──►  Container Deploy\n" +
            "                                            │\n" +
            "                                  MongoDB Atlas (Cloud DB)\n" +
            "                                            │\n" +
            "                             HTTPS Public URL  ◄──  End Users");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  6. DATABASE DESIGN
    // ═════════════════════════════════════════════════════════════════════════
    private void buildDatabaseDesign(Document doc) throws IOException {
        addSectionHeading(doc, "4.", "Database Design");

        addBody(doc, "All data is persisted in MongoDB Atlas. The system uses 14 collections, "
                + "each mapped to a Spring Data MongoDB document class.");

        Object[][] collections = {
            {"employees",      "id, employeeId, name, email, password, role, department, designation, phone, address, salary, joinDate, profilePic, active"},
            {"departments",    "id, name, description, headName, createdAt"},
            {"attendance",     "id, employeeId, date, checkIn, checkOut, status (PRESENT/ABSENT/HALF_DAY), hoursWorked"},
            {"leaves",         "id, employeeId, employeeName, leaveType, startDate, endDate, days, reason, status (PENDING/APPROVED/REJECTED), appliedAt, respondedAt"},
            {"salaries",       "id, employeeId, month, year, basicSalary, allowances, deductions, netSalary, generatedAt, generatedBy"},
            {"tasks",          "id, title, description, assignedTo, assignedBy, dueDate, priority, status (TODO/IN_PROGRESS/DONE), createdAt"},
            {"announcements",  "id, title, content, postedBy, postedAt, targetRole"},
            {"messages",       "id, senderId, senderName, receiverId, receiverName, content, timestamp, read"},
            {"notifications",  "id, userId, message, type, read, createdAt"},
            {"complaints",     "id, employeeId, employeeName, subject, description, status (OPEN/RESOLVED), response, submittedAt, resolvedAt"},
            {"work_from_home", "id, employeeId, employeeName, startDate, endDate, reason, status (PENDING/APPROVED/REJECTED), requestedAt"},
            {"performance",    "id, employeeId, reviewerName, period, rating (1-5), comments, createdAt"},
            {"holidays",       "id, name, date, type (PUBLIC/OPTIONAL), description"},
            {"users",          "id, username (email), password (BCrypt), role, enabled, oauthProvider"},
        };

        Table t = new Table(UnitValue.createPercentArray(new float[]{25, 75}))
                .setWidth(UnitValue.createPercentValue(100));

        t.addHeaderCell(new Cell().setBackgroundColor(COLOR_TABLE_HDR).setBorder(Border.NO_BORDER).setPadding(8)
                .add(new Paragraph("Collection").setFont(fontBold).setFontSize(11).setFontColor(COLOR_WHITE)));
        t.addHeaderCell(new Cell().setBackgroundColor(COLOR_TABLE_HDR).setBorder(Border.NO_BORDER).setPadding(8)
                .add(new Paragraph("Fields").setFont(fontBold).setFontSize(11).setFontColor(COLOR_WHITE)));

        boolean alt = false;
        for (Object[] row : collections) {
            DeviceRgb bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
            t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(7)
                    .add(new Paragraph((String) row[0])
                            .setFont(fontBold).setFontSize(10).setFontColor(COLOR_SUBHEADER)));
            t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(7)
                    .add(new Paragraph((String) row[1])
                            .setFont(fontMono).setFontSize(9).setFontColor(COLOR_DARK_TEXT)));
            alt = !alt;
        }
        doc.add(t);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  7. SECURITY
    // ═════════════════════════════════════════════════════════════════════════
    private void buildSecurity(Document doc) throws IOException {
        addSectionHeading(doc, "5.", "Security");

        addSubHeading(doc, "Role-Based Access Control (RBAC)");
        addBody(doc, "EMS defines two roles stored in MongoDB: ROLE_ADMIN and ROLE_EMPLOYEE. "
                + "Every HTTP request is evaluated by the SecurityFilterChain before reaching a controller. "
                + "Admin-only endpoints (employee CRUD, leave approvals, salary generation) require ROLE_ADMIN, "
                + "while self-service endpoints require ROLE_ADMIN or ROLE_EMPLOYEE.");

        addSubHeading(doc, "Spring Security Configuration");
        addBullet(doc, "BCryptPasswordEncoder (strength 10) hashes all locally stored passwords.");
        addBullet(doc, "DaoAuthenticationProvider loads user details from MongoDB via CustomUserDetailsService.");
        addBullet(doc, "Form login endpoint: POST /login — redirects to /dashboard on success.");
        addBullet(doc, "Failed login redirects to /login?error=true with a user-friendly message.");
        addBullet(doc, "Logout via POST /logout invalidates session, clears JSESSIONID cookie.");
        addBullet(doc, "CSRF protection is enabled by default for all state-changing requests.");

        addSubHeading(doc, "Google OAuth2 Login");
        addBody(doc, "Employees may choose 'Sign in with Google' on the login page. The OAuth2 flow:");
        addMonoBlock(doc,
            "  User clicks 'Sign in with Google'\n" +
            "        │\n" +
            "  Spring redirects  →  Google Authorization Server\n" +
            "        │\n" +
            "  User consents  →  Google issues Authorization Code\n" +
            "        │\n" +
            "  Spring exchanges code  →  Access Token  +  UserInfo\n" +
            "        │\n" +
            "  CustomOAuth2UserService  →  loads/creates user in MongoDB\n" +
            "        │\n" +
            "  OAuth2LoginSuccessHandler  →  sets role, redirects to /dashboard");

        addSubHeading(doc, "Public Endpoints (No Auth Required)");
        addBullet(doc, "GET  /   — Landing / redirect page");
        addBullet(doc, "GET  /login   — Login page");
        addBullet(doc, "GET  /register   — Registration page");
        addBullet(doc, "GET  /project-documentation   — This PDF file");
        addBullet(doc, "Static assets: /css/**, /js/**, /images/**");
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  8. FEATURES
    // ═════════════════════════════════════════════════════════════════════════
    private void buildFeatures(Document doc) throws IOException {
        addSectionHeading(doc, "6.", "Features");

        // Admin
        Table adminTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
        adminTable.addHeaderCell(new Cell()
                .setBackgroundColor(COLOR_SUBHEADER).setBorder(Border.NO_BORDER).setPadding(10)
                .add(new Paragraph("Admin Features").setFont(fontBold).setFontSize(13).setFontColor(COLOR_WHITE)));
        String[] adminFeatures = {
            "Employee CRUD — add, edit, view, delete employees; export CSV.",
            "Department Management — create and manage departments.",
            "Attendance Management — view all attendance records; edit entries.",
            "Leave Management — view leave requests, approve or reject with comments.",
            "Salary Generation — compute and generate monthly payslips per employee.",
            "Task Assignment — create tasks, assign to employees, set priority & deadline.",
            "Announcements — post organisation-wide or role-targeted announcements.",
            "WFH Requests — approve or reject employee Work From Home requests.",
            "Complaints — view and respond to employee complaints.",
            "Performance Reviews — add and delete performance evaluation records.",
            "Holiday Calendar — add and remove public / optional holidays.",
            "Reports — generate PDF reports for attendance, salary, and employees.",
            "Notifications — system-generated alerts for key admin actions.",
        };
        for (String f : adminFeatures) {
            adminTable.addCell(new Cell().setBorder(Border.NO_BORDER)
                    .setBackgroundColor(COLOR_LIGHT_BG).setPadding(6)
                    .add(new Paragraph("  ✔  " + f)
                            .setFont(fontRegular).setFontSize(10.5f).setFontColor(COLOR_DARK_TEXT)));
        }
        doc.add(adminTable);

        // Employee
        Table empTable = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(8);
        empTable.addHeaderCell(new Cell()
                .setBackgroundColor(COLOR_ACCENT).setBorder(Border.NO_BORDER).setPadding(10)
                .add(new Paragraph("Employee Features").setFont(fontBold).setFontSize(13).setFontColor(COLOR_WHITE)));
        String[] empFeatures = {
            "Dashboard — personalised summary: attendance %, pending leaves, upcoming tasks.",
            "My Attendance — mark daily attendance (check-in / check-out); view history.",
            "Leave Application — submit leave requests; track approval status.",
            "Salary Slips — view and download monthly payslips as PDF.",
            "Task Tracker — view assigned tasks, update status (TODO → IN_PROGRESS → DONE).",
            "Work From Home — submit and track WFH requests.",
            "Complaints — raise HR complaints and track responses.",
            "Announcements — read all organisation-wide and personal announcements.",
            "Messaging — internal messaging with colleagues and HR.",
            "Performance — view personal performance review history.",
            "Holiday Calendar — view all upcoming holidays.",
            "Profile Management — update personal details and profile picture.",
            "Change Password — securely update login credentials.",
        };
        for (String f : empFeatures) {
            empTable.addCell(new Cell().setBorder(Border.NO_BORDER)
                    .setBackgroundColor(COLOR_LIGHT_BG).setPadding(6)
                    .add(new Paragraph("  ✔  " + f)
                            .setFont(fontRegular).setFontSize(10.5f).setFontColor(COLOR_DARK_TEXT)));
        }
        doc.add(empTable);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  9. API ENDPOINTS
    // ═════════════════════════════════════════════════════════════════════════
    private void buildApiEndpoints(Document doc) throws IOException {
        addSectionHeading(doc, "7.", "API Endpoints");

        addBody(doc, "EMS exposes Spring MVC routes for both server-rendered pages (Thymeleaf) and "
                + "JSON REST responses. All authenticated endpoints require a valid session cookie.");

        Object[][] groups = {
            {"Auth & Public",
             new String[][]{
                {"GET",  "/login",                   "Login page"},
                {"POST", "/login",                   "Form login submit"},
                {"GET",  "/register",                "Registration page"},
                {"POST", "/register",                "Submit registration"},
                {"POST", "/logout",                  "Logout (invalidates session)"},
                {"GET",  "/project-documentation",   "Download this PDF"},
             }},
            {"Dashboard",
             new String[][]{
                {"GET", "/dashboard", "Main dashboard (role-aware)"},
             }},
            {"Employees (Admin)",
             new String[][]{
                {"GET",  "/employees",          "List all employees"},
                {"GET",  "/addEmployee",         "Add employee form"},
                {"POST", "/saveEmployee",        "Save new / edited employee"},
                {"GET",  "/editEmployee/{id}",   "Edit employee form"},
                {"GET",  "/deleteEmployee/{id}", "Delete employee"},
                {"GET",  "/downloadEmployees",   "Export employees as CSV"},
             }},
            {"Attendance",
             new String[][]{
                {"GET",  "/attendance",          "View attendance records"},
                {"POST", "/saveAttendance",      "Admin save attendance"},
                {"POST", "/markMyAttendance",    "Employee check-in / check-out"},
             }},
            {"Leave",
             new String[][]{
                {"GET",  "/leave",               "View leaves"},
                {"POST", "/applyLeave",          "Submit leave request"},
                {"GET",  "/approveLeave/{id}",   "Approve leave (Admin)"},
                {"GET",  "/rejectLeave/{id}",    "Reject leave (Admin)"},
             }},
            {"Salary",
             new String[][]{
                {"GET",  "/salary",              "View salary records"},
                {"POST", "/salary/generate",     "Generate payslip (Admin)"},
                {"GET",  "/salary/download/{id}","Download salary slip PDF"},
             }},
            {"Tasks",
             new String[][]{
                {"GET",  "/tasks",               "View tasks"},
                {"POST", "/tasks/assign",        "Assign task (Admin)"},
                {"POST", "/tasks/updateStatus",  "Update task status"},
                {"GET",  "/tasks/delete/{id}",   "Delete task (Admin)"},
             }},
            {"Other Modules",
             new String[][]{
                {"*", "/departments/**",         "Department CRUD (Admin)"},
                {"*", "/announcements/**",       "Announcements"},
                {"*", "/messages/**",            "Internal messaging"},
                {"*", "/wfh/**",                 "Work From Home requests"},
                {"*", "/complaints/**",          "Complaints"},
                {"*", "/notifications/**",       "Notifications"},
                {"*", "/performance/**",         "Performance reviews"},
                {"*", "/holidays/**",            "Holiday calendar"},
                {"*", "/reports/**",             "PDF reports (Admin)"},
                {"GET", "/my-profile",           "View / edit profile"},
                {"GET", "/change-password",      "Change password page"},
             }},
        };

        for (Object[] group : groups) {
            addSubHeading(doc, (String) group[0]);
            String[][] endpoints = (String[][]) group[1];

            Table t = new Table(UnitValue.createPercentArray(new float[]{12, 40, 48}))
                    .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(10);

            for (String h : new String[]{"Method", "Path", "Description"}) {
                t.addHeaderCell(new Cell().setBackgroundColor(COLOR_TABLE_HDR)
                        .setBorder(Border.NO_BORDER).setPadding(6)
                        .add(new Paragraph(h).setFont(fontBold).setFontSize(10).setFontColor(COLOR_WHITE)));
            }

            boolean alt = false;
            for (String[] ep : endpoints) {
                DeviceRgb bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
                DeviceRgb methodColor = ep[0].equals("GET") ? COLOR_ACCENT
                        : ep[0].equals("POST") ? COLOR_HEADER : COLOR_SUBHEADER;
                t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(5)
                        .add(new Paragraph(ep[0]).setFont(fontBold).setFontSize(9).setFontColor(methodColor)));
                t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(5)
                        .add(new Paragraph(ep[1]).setFont(fontMono).setFontSize(9).setFontColor(COLOR_DARK_TEXT)));
                t.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(5)
                        .add(new Paragraph(ep[2]).setFont(fontRegular).setFontSize(9.5f).setFontColor(COLOR_DARK_TEXT)));
                alt = !alt;
            }
            doc.add(t);
        }
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  10. DEPLOYMENT
    // ═════════════════════════════════════════════════════════════════════════
    private void buildDeployment(Document doc) throws IOException {
        addSectionHeading(doc, "8.", "Deployment");

        addSubHeading(doc, "Docker");
        addBody(doc, "The application ships with a multi-stage Dockerfile. The build stage compiles the "
                + "Spring Boot fat-JAR with Maven, and the runtime stage uses a slim JRE 17 image to keep "
                + "the final image small and secure.");
        addMonoBlock(doc,
            "  # Build stage\n" +
            "  FROM maven:3.9-eclipse-temurin-17 AS build\n" +
            "  WORKDIR /app\n" +
            "  COPY . .\n" +
            "  RUN mvn clean package -DskipTests\n\n" +
            "  # Runtime stage\n" +
            "  FROM eclipse-temurin:17-jre-alpine\n" +
            "  WORKDIR /app\n" +
            "  COPY --from=build /app/target/*.jar app.jar\n" +
            "  EXPOSE 8080\n" +
            "  ENTRYPOINT [\"java\",\"-jar\",\"app.jar\"]");

        addSubHeading(doc, "Render");
        addBody(doc, "Render.com hosts the containerised application as a Web Service:");
        addBullet(doc, "Connect GitHub repository to Render — auto-deploy on every push to main.");
        addBullet(doc, "Set environment variables (MONGO_URI, GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, etc.) in Render dashboard.");
        addBullet(doc, "Render provisions a free TLS certificate and assigns a public HTTPS URL.");
        addBullet(doc, "Health-check endpoint ensures zero-downtime rolling deploys.");

        addSubHeading(doc, "MongoDB Atlas");
        addBody(doc, "MongoDB Atlas provides a fully managed, globally distributed cloud database:");
        addBullet(doc, "Create a free M0 cluster in the AWS region closest to your Render service.");
        addBullet(doc, "Add the Render service IP (or 0.0.0.0/0 for simplicity) to Atlas IP Access List.");
        addBullet(doc, "Set MONGO_URI in Render environment variables using the Atlas connection string.");
        addBullet(doc, "Enable Atlas automatic backups and monitoring for production resilience.");

        addSubHeading(doc, "Environment Variables");
        Table envTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(8);
        envTable.addHeaderCell(new Cell().setBackgroundColor(COLOR_TABLE_HDR).setBorder(Border.NO_BORDER).setPadding(7)
                .add(new Paragraph("Variable").setFont(fontBold).setFontSize(10.5f).setFontColor(COLOR_WHITE)));
        envTable.addHeaderCell(new Cell().setBackgroundColor(COLOR_TABLE_HDR).setBorder(Border.NO_BORDER).setPadding(7)
                .add(new Paragraph("Description").setFont(fontBold).setFontSize(10.5f).setFontColor(COLOR_WHITE)));
        String[][] envVars = {
            {"MONGO_URI",              "Full MongoDB Atlas connection string"},
            {"GOOGLE_CLIENT_ID",       "Google OAuth2 client ID"},
            {"GOOGLE_CLIENT_SECRET",   "Google OAuth2 client secret"},
            {"SERVER_PORT",            "App port (default 8080)"},
            {"SPRING_PROFILES_ACTIVE", "Active Spring profile (prod)"},
        };
        boolean alt = false;
        for (String[] ev : envVars) {
            DeviceRgb bg = alt ? COLOR_ROW_ALT : COLOR_WHITE;
            envTable.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(6)
                    .add(new Paragraph(ev[0]).setFont(fontMono).setFontSize(9.5f).setFontColor(COLOR_SUBHEADER)));
            envTable.addCell(new Cell().setBackgroundColor(bg).setBorder(Border.NO_BORDER).setPadding(6)
                    .add(new Paragraph(ev[1]).setFont(fontRegular).setFontSize(9.5f).setFontColor(COLOR_DARK_TEXT)));
            alt = !alt;
        }
        doc.add(envTable);
    }

    // ═════════════════════════════════════════════════════════════════════════
    //  11. CONCLUSION
    // ═════════════════════════════════════════════════════════════════════════
    private void buildConclusion(Document doc) throws IOException {
        addSectionHeading(doc, "9.", "Conclusion");

        addBody(doc, "The Employee Management System is a production-ready, cloud-native application that "
                + "demonstrates the power of the Spring ecosystem combined with MongoDB's flexible document "
                + "model. It covers the full lifecycle of an HR workflow: from hiring an employee and "
                + "managing their day-to-day activities, to processing payroll and tracking performance.");

        addBody(doc, "The system's layered architecture ensures clean separation of concerns, making it "
                + "straightforward to extend — adding new modules, integrating third-party services, "
                + "or migrating to microservices — without disrupting existing functionality.");

        addBody(doc, "Security is treated as a first-class concern through role-based access control, "
                + "BCrypt password hashing, CSRF protection, and Google OAuth2, ensuring that sensitive "
                + "HR data is protected at every layer.");

        addBody(doc, "Docker containerisation and Render deployment mean the application is portable and "
                + "can be shipped to any cloud provider with minimal configuration changes, while MongoDB "
                + "Atlas handles all database infrastructure concerns.");

        SolidLine sl = new SolidLine(2f);
        sl.setColor(COLOR_HEADER);
        doc.add(new LineSeparator(sl).setMarginTop(20).setMarginBottom(16));

        // Footer block
        Table footer = new Table(UnitValue.createPercentArray(new float[]{1}))
                .setWidth(UnitValue.createPercentValue(100));
        footer.addCell(new Cell()
                .setBackgroundColor(COLOR_HEADER)
                .setBorder(Border.NO_BORDER).setPadding(20)
                .add(new Paragraph("Employee Management System  —  Technical Documentation")
                        .setFont(fontBold).setFontSize(12).setFontColor(COLOR_WHITE)
                        .setTextAlignment(TextAlignment.CENTER).setMarginBottom(4))
                .add(new Paragraph("Built with Spring Boot 3.2.5  •  MongoDB  •  Thymeleaf  •  Docker  •  Render")
                        .setFont(fontRegular).setFontSize(10).setFontColor(COLOR_WHITE)
                        .setTextAlignment(TextAlignment.CENTER).setMarginBottom(4))
                .add(new Paragraph("Generated: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")))
                        .setFont(fontItalic).setFontSize(9).setFontColor(COLOR_WHITE)
                        .setTextAlignment(TextAlignment.CENTER)));
        doc.add(footer);
    }
}
