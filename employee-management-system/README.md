<div align="center">

<img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=32&pause=1000&color=667EEA&center=true&vCenter=true&width=600&lines=Employee+Management+System;Built+with+Spring+Boot+%26+MongoDB;Full-Stack+Java+Web+Application" alt="Typing SVG" />

<br/>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-6.0-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring%20Security-6.2-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)

<br/>

![GitHub stars](https://img.shields.io/github/stars/vishal-q/Employment_Management_System?style=social)
![GitHub forks](https://img.shields.io/github/forks/vishal-q/Employment_Management_System?style=social)
![GitHub last commit](https://img.shields.io/github/last-commit/vishal-q/Employment_Management_System?color=667eea)

</div>

---

<div align="center">
  <h2>🏢 A Complete Enterprise-Grade Employee Management Portal</h2>
  <p><i>Manage your workforce efficiently with a modern, role-based web application</i></p>
</div>

---

## 📌 Table of Contents

- [✨ Features](#-features)
- [🖥️ Tech Stack](#️-tech-stack)
- [📁 Project Structure](#-project-structure)
- [⚙️ Setup & Installation](#️-setup--installation)
- [🔐 Environment Variables](#-environment-variables)
- [👤 Roles & Access](#-roles--access)
- [🚀 Key Highlights](#-key-highlights)
- [📄 License](#-license)

---

## ✨ Features

### 👑 Admin Features
| Feature | Description |
|--------|-------------|
| 👥 Employee Management | Add, Edit, Delete, View full employee profiles |
| 📅 Attendance Tracking | Mark attendance for any employee with status |
| 📋 Leave Management | Approve / Reject leave requests with remarks |
| 🏢 Department Management | Create and manage departments |
| 💰 Salary Slip Generator | Generate monthly payslips with PDF download |
| ⭐ Performance Reviews | Rate employees on 5 parameters (1–5 scale) |
| 🎯 Task Assignment | Assign tasks with priority & due date |
| 📢 Announcements | Post company-wide announcements |
| 🏖️ Holiday Calendar | Add national / company holidays |
| 💬 Internal Messaging | Message individual employees |
| 📊 PDF Export | Download employee reports as PDF |
| 🔔 Notifications | Real-time notification system |

### 👤 Employee Features
| Feature | Description |
|--------|-------------|
| 🙋 My Profile | View personal details, salary, leave balance |
| 📅 My Attendance | View own attendance history |
| 📋 Apply Leave | Apply with leave type, auto balance deduction |
| 🏠 Work From Home | Submit WFH requests |
| ⭐ Performance History | View own review ratings |
| 💰 Salary Slips | View & download monthly payslips |
| 🎯 My Tasks | View assigned tasks, update status |
| 📢 Announcements | View all company announcements |
| 📝 Complaints | Submit grievances (anonymous option) |
| 💬 Message Admin | Direct messaging to HR/Admin |
| 🔔 Notifications | Receive real-time alerts |

---

## 🖥️ Tech Stack

```
╔══════════════════════════════════════════════════════════╗
║                    TECH STACK                            ║
╠═══════════════╦══════════════════════════════════════════╣
║ Backend       ║ Spring Boot 3.2.5, Spring Security 6.2   ║
║ Database      ║ MongoDB 6.0                              ║
║ Frontend      ║ Thymeleaf, HTML5, CSS3, JavaScript       ║
║ Auth          ║ Spring Security + Google OAuth2           ║
║ PDF           ║ iTextPDF 7.2.5                           ║
║ Build Tool    ║ Maven                                    ║
║ Java Version  ║ Java 17 (LTS)                            ║
╚═══════════════╩══════════════════════════════════════════╝
```

---

## 📁 Project Structure

```
employee-management-system/
│
├── src/main/java/com/ems/
│   ├── config/              # Security & OAuth2 Config
│   ├── controller/          # 19 Controllers
│   ├── model/               # 17 Data Models
│   ├── repository/          # 14 MongoDB Repositories
│   ├── service/             # Business Logic Services
│   └── utils/               # AuthHelper & Utilities
│
├── src/main/resources/
│   ├── templates/           # 24 Thymeleaf HTML Templates
│   ├── static/
│   │   ├── css/             # Dashboard & Login Styles
│   │   └── js/              # Dark Mode, Counter Animations
│   └── application.properties
│
├── .env                     # 🔒 Secrets (not pushed)
├── .gitignore
└── README.md
```

---

## ⚙️ Setup & Installation

### Prerequisites
- ✅ Java 17+
- ✅ MongoDB running on `localhost:27017`
- ✅ Maven 3.6+

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/vishal-q/Employment_Management_System.git
cd Employment_Management_System
```

### 2️⃣ Create `.env` File

Create a `.env` file in the project root:

```env
MONGODB_HOST=localhost
MONGODB_PORT=27017
MONGODB_DATABASE=emsdb

GOOGLE_CLIENT_ID=your_google_client_id_here
GOOGLE_CLIENT_SECRET=your_google_client_secret_here
```

> 🔑 Get Google OAuth credentials from [Google Cloud Console](https://console.cloud.google.com)

### 3️⃣ Run the Application

```bash
./mvnw spring-boot:run
```

### 4️⃣ Open in Browser

```
http://localhost:8080
```

---

## 🔐 Environment Variables

| Variable | Description |
|----------|-------------|
| `MONGODB_HOST` | MongoDB host (default: `localhost`) |
| `MONGODB_PORT` | MongoDB port (default: `27017`) |
| `MONGODB_DATABASE` | Database name (default: `emsdb`) |
| `GOOGLE_CLIENT_ID` | Google OAuth2 Client ID |
| `GOOGLE_CLIENT_SECRET` | Google OAuth2 Client Secret |

---

## 👤 Roles & Access

```
┌─────────────────────────────────────────┐
│              ADMIN                       │
│  Full access to all features             │
│  Can manage employees, approve requests  │
│  Generate reports, assign tasks          │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│              EMPLOYEE                    │
│  Access to own data only                 │
│  Apply leaves, view salary slips         │
│  Submit requests, message admin          │
└─────────────────────────────────────────┘
```

**First time setup:**
1. Register as **Admin**
2. Add employees via Admin panel
3. Create login accounts for employees (checkbox in Add Employee form)
4. Employees can now login with their credentials

---

## 🚀 Key Highlights

```
🔒  Spring Security with Role-Based Access Control
🔑  Google OAuth2 Login Support
🌙  Dark Mode with localStorage persistence
📱  Responsive Design
🔔  Real-time Notification System
📄  PDF Generation for Salary Slips & Reports
🔄  2-Way Admin ↔ Employee Communication
⚡  Counter Animations on Dashboard
🌐  Thymeleaf + Spring Security integration
```

---

## 📊 Database Collections

| Collection | Description |
|-----------|-------------|
| `users` | Login credentials & roles |
| `employees` | Employee profiles |
| `attendance` | Attendance records |
| `leave_requests` | Leave applications |
| `salary_slips` | Monthly payslips |
| `performance_reviews` | Review ratings |
| `tasks` | Assigned tasks |
| `messages` | Internal messages |
| `announcements` | Company announcements |
| `notifications` | System notifications |
| `complaints` | Grievances |
| `wfh_requests` | Work from home requests |
| `holidays` | Company holidays |
| `departments` | Department list |

---

## 📄 License

```
MIT License — Feel free to use, modify, and distribute.
```

---

<div align="center">

### ⭐ If you found this project helpful, please give it a star!

**Made with ❤️ using Spring Boot & MongoDB**

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Ready-6DB33F?style=flat-square&logo=springboot)
![MongoDB](https://img.shields.io/badge/MongoDB-Connected-47A248?style=flat-square&logo=mongodb)
![Java](https://img.shields.io/badge/Java%2017-Powered-ED8B00?style=flat-square&logo=openjdk)

</div>
