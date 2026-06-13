# Employee Management System (EMS)

A full-featured Employee Management System built with **Spring Boot**, **MongoDB**, and **Thymeleaf**.

## Features

- **Role-Based Access** — Admin & Employee roles
- **Google OAuth2 Login** — Sign in with Google
- **Employee Management** — Add, Edit, Delete, View profiles
- **Attendance Tracking** — Mark and view attendance
- **Leave Management** — Apply, Approve, Reject leaves with balance tracking
- **Work From Home Requests**
- **Task Assignment** — Admin assigns tasks, employees update status
- **Announcements** — Admin posts, employees view
- **Internal Messaging** — 2-way communication
- **Complaints / Grievances**
- **Salary Slip Generation** — PDF download
- **Performance Reviews**
- **Holiday Calendar**
- **Notifications System**
- **Password Change**
- **Dark Mode**
- **PDF Export**

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Spring Boot 3.2.5 |
| Database | MongoDB |
| Frontend | Thymeleaf + HTML/CSS/JS |
| Security | Spring Security + Google OAuth2 |
| PDF | iTextPDF 7.2.5 |

## Setup Instructions

### Prerequisites
- Java 17+
- MongoDB running on `localhost:27017`
- Maven

### 1. Clone the repository
```bash
git clone https://github.com/vishal-q/employee-management-system.git
cd employee-management-system
```

### 2. Create `.env` file
Create a `.env` file in the project root with:
```
MONGODB_HOST=localhost
MONGODB_PORT=27017
MONGODB_DATABASE=emsdb
GOOGLE_CLIENT_ID=your_google_client_id
GOOGLE_CLIENT_SECRET=your_google_client_secret
```

### 3. Run the application
```bash
./mvnw spring-boot:run
```

### 4. Open in browser
```
http://localhost:8080
```

## Default Setup
- Register as **Admin** first
- Then add employees and create their login accounts
- Employees can login and access their own dashboard

## Screenshots
> Coming soon

## License
MIT
