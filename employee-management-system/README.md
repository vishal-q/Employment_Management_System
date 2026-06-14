<div align="center">

<img src="https://capsule-render.vercel.app/api?type=waving&color=667eea&height=200&section=header&text=Employee%20Management%20System&fontSize=40&fontColor=ffffff&animation=fadeIn&fontAlignY=38&desc=A%20Modern%20HR%20Portal%20Built%20with%20Spring%20Boot&descAlignY=55&descAlign=50" width="100%"/>

<br/>

<a href="https://employment-management-system-ed8c.onrender.com" target="_blank">
  <img src="https://img.shields.io/badge/🚀%20LIVE%20DEMO-Click%20Here-667eea?style=for-the-badge&logoColor=white" alt="Live Demo"/>
</a>

<br/><br/>

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-Atlas-47A248?style=for-the-badge&logo=mongodb&logoColor=white)
![Java](https://img.shields.io/badge/Java-17-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Deployed-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-Live-46E3B7?style=for-the-badge&logo=render&logoColor=white)

<br/>

![GitHub stars](https://img.shields.io/github/stars/vishal-q/Employment_Management_System?style=social)
![GitHub forks](https://img.shields.io/github/forks/vishal-q/Employment_Management_System?style=social)
![GitHub last commit](https://img.shields.io/github/last-commit/vishal-q/Employment_Management_System?color=667eea&style=flat-square)
![GitHub repo size](https://img.shields.io/github/repo-size/vishal-q/Employment_Management_System?color=764ba2&style=flat-square)

</div>

---

<div align="center">

## 🌐 [https://employment-management-system-ed8c.onrender.com](https://employment-management-system-ed8c.onrender.com)

> *A complete enterprise-grade HR portal with role-based access, real-time notifications, and full employee lifecycle management.*

</div>

---

## 📸 Preview

```
┌─────────────────────────────────────────────────────────────┐
│                    🏢 EMS Dashboard                          │
│  ┌──────────┐  ┌─────────────────────────────────────────┐  │
│  │          │  │  👥 Total    🟢 Present  📋 Pending      │  │
│  │ SIDEBAR  │  │  Employees    Today      Leaves          │  │
│  │          │  │   127          89          12            │  │
│  │ Dashboard│  ├─────────────────────────────────────────┤  │
│  │ Employees│  │  ⚡ Quick Actions                        │  │
│  │ Attendance│  │  [Add Employee] [Mark Attendance]       │  │
│  │ Leave    │  │  [Apply Leave]  [Download PDF]           │  │
│  │ Tasks    │  ├─────────────────────────────────────────┤  │
│  │ Salary   │  │  📊 Recent Employees Table               │  │
│  │ Messages │  │  EMP-001 | John Doe | Engineering        │  │
│  │ ...      │  │  EMP-002 | Jane Smith | HR               │  │
│  └──────────┘  └─────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## ✨ Features

<table>
<tr>
<td width="50%">

### 👑 Admin Panel
- 👥 **Employee Management** — Full CRUD with profiles
- 📅 **Attendance Tracking** — Department-wise reports
- 📋 **Leave Management** — Approve/Reject with remarks
- 💰 **Salary Slips** — Generate & download PDF
- ⭐ **Performance Reviews** — 5-parameter rating system
- 🎯 **Task Assignment** — With priority & due dates
- 📢 **Announcements** — Company-wide broadcasts
- 🏖️ **Holiday Calendar** — National & company holidays
- 💬 **Internal Messaging** — Direct employee messaging
- 📊 **PDF Reports** — Export employee data
- 🔔 **Notifications** — Real-time alert system

</td>
<td width="50%">

### 👤 Employee Portal
- 🙋 **My Profile** — Personal & job information
- 📅 **My Attendance** — Personal history
- 📋 **Leave Requests** — Apply with balance tracking
- 🏠 **Work From Home** — WFH request system
- 💰 **Salary Slips** — View & download payslips
- ⭐ **Performance History** — View ratings
- 🎯 **My Tasks** — Update task status
- 📢 **Announcements** — Read company updates
- 📝 **Complaints** — Submit anonymously
- 💬 **Message Admin** — Direct communication
- 🔔 **Notifications** — Personal alerts

</td>
</tr>
</table>

---

## 🛠️ Tech Stack

<div align="center">

| Layer | Technology | Version |
|-------|-----------|---------|
| ☕ Backend | Spring Boot | 3.2.5 |
| 🗄️ Database | MongoDB Atlas | Cloud |
| 🎨 Frontend | Thymeleaf + HTML/CSS/JS | — |
| 🔐 Security | Spring Security + OAuth2 | 6.2 |
| 📄 PDF | iTextPDF | 7.2.5 |
| 🐳 Container | Docker | Latest |
| ☁️ Hosting | Render | Free Tier |
| ☕ Java | OpenJDK | 17 LTS |

</div>

---

## 🚀 Quick Start

### Prerequisites
```
✅ Java 17+
✅ MongoDB (local or Atlas)
✅ Maven 3.6+
```

### 1️⃣ Clone
```bash
git clone https://github.com/vishal-q/Employment_Management_System.git
cd Employment_Management_System/employee-management-system
```

### 2️⃣ Configure `.env`
```env
MONGODB_URI=mongodb://localhost:27017/employee_management
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
APP_URL=http://localhost:8080
```

### 3️⃣ Run
```bash
./mvnw spring-boot:run
```

### 4️⃣ Open
```
http://localhost:8080
```

---

## 🔐 Default Access

```
1. Register as ADMIN first
2. Login → Go to Dashboard
3. Add Employees → Create their login accounts
4. Employees login with their credentials
```

---

## 📁 Project Structure

```
employee-management-system/
├── 🔧 config/          → Security & OAuth2 & WebConfig
├── 🎮 controller/      → 19 REST Controllers
├── 📦 model/           → 17 MongoDB Documents
├── 🗃️ repository/      → 14 MongoDB Repositories
├── ⚙️ service/         → Business Logic Layer
├── 🛡️ security/        → CustomUserDetails
├── 🔨 utils/           → AuthHelper
├── 📄 templates/       → 24 Thymeleaf Templates
├── 🎨 static/          → CSS + JS Assets
├── 🐳 Dockerfile       → Container Config
└── ⚙️ application.properties
```

---

## 🗄️ MongoDB Collections

<div align="center">

| Collection | Purpose |
|-----------|---------|
| `users` | Login & roles |
| `employees` | Employee profiles |
| `attendance` | Daily records |
| `leave_requests` | Leave applications |
| `salary_slips` | Monthly payslips |
| `performance_reviews` | Ratings |
| `tasks` | Assignments |
| `messages` | Internal chat |
| `announcements` | Company updates |
| `notifications` | Alerts |
| `complaints` | Grievances |
| `wfh_requests` | WFH applications |
| `holidays` | Calendar |
| `departments` | Org structure |

</div>

---

## 🌟 Key Highlights

```
🔒  Role-Based Access Control (ADMIN / EMPLOYEE)
🔑  Google OAuth2 Single Sign-On
🌙  Dark Mode with localStorage persistence
📱  Responsive Design
🔔  Real-time Notification System
📄  PDF Generation (Salary Slips + Reports)
🔄  2-Way Admin ↔ Employee Communication
🐳  Dockerized for easy deployment
☁️  Deployed on Render with MongoDB Atlas
```

---

<div align="center">

## 📄 License

**MIT License** — Free to use, modify & distribute

---

### ⭐ If this project helped you, please give it a star!

<img src="https://capsule-render.vercel.app/api?type=waving&color=667eea&height=100&section=footer" width="100%"/>

</div>
