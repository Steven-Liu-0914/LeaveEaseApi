# LeaveEase-API (Backend)

This is the backend of the **LeaveEase** HR leave management system, developed using **Spring Boot** with **JDK 17**, and built as a RESTful API. It provides business logic and database interactions for all user types — Normal Staff, Department Manager, and HR Admin.

---

## 🚀 Option 1 – Run via Docker (Recommended)

### Prerequisites
- Docker installed on your system
- PowerShell (for Windows script execution)

### Steps
1. **Download** the Docker archive from:  
   [https://drive.google.com/file/d/1x1jPuMxBbTs8C97wD15S8VTryqVuzgAy/view?usp=sharing](https://drive.google.com/file/d/1x1jPuMxBbTs8C97wD15S8VTryqVuzgAy/view?usp=sharing)

2. **Unzip** the archive (e.g., `LeaveEase.Docker.zip`)

3. **Run** the provided `run.ps1` script (Windows only)

4. **If using macOS/Linux**:  
   Open a terminal and run:

   ```bash
   cd LeaveEase.Docker
   docker load -i mysql.tar
   docker load -i leaveease-backend.tar
   docker load -i leaveease-ui.tar
   docker-compose up -d
   open http://localhost:4201
   ```

5. **Wait ~1 minute** for containers to initialize.  
   Then visit the frontend: [http://localhost:4201](http://localhost:4201)

---

## ⚙️ Backend Setup – Local Development (Alternative)

### Prerequisites

- **Java Development Kit (JDK 17)**  
  👉 [https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)

- **Apache Maven**  
  👉 [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

- **IntelliJ IDEA (Recommended IDE)**  
  👉 [https://www.jetbrains.com/idea/download](https://www.jetbrains.com/idea/download)

---

### Setup Steps

```bash
# Open the project in IntelliJ
# Let IntelliJ auto-import all Maven dependencies
```

1. Open the backend folder (`LeaveEaseApi`) in IntelliJ.
2. Configure the database in:
   ```
   src/main/resources/application.properties
   ```

   Default configuration (for local MySQL):
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/leave_ease
   spring.datasource.username=root
   spring.datasource.password=your_password
   ```

3. Build & run the application (Maven `clean install` > Run Main)

- The backend will be available at:  
  [http://localhost:8080](http://localhost:8080)

---

## 🗃️ Database Initialization (Required)

The backend depends on an existing MySQL database with required schema and seed data.

### Steps:
1. Install **MySQL Server** and **MySQL Workbench**
2. Run the following SQL script to create all tables, views and insert testing data:

📎 [initial_reset_database.sql](https://github.com/Steven-Liu-0914/LeaveEaseApi/blob/master/src/database/initial_reset_database.sql)

---

## 🧪 Test User Accounts

After database setup, use the following accounts for login:

| Name    | Role                    | Username | Password  |
|---------|-------------------------|----------|-----------|
| Amy     | Normal Staff (Marketing)| A001     | P@ssw0rd  |
| Bob     | Department Manager      | B001     | P@ssw0rd  |
| Cindy   | HR Admin                | C001     | P@ssw0rd  |
| Daniel  | Normal Staff (Engineer) | D001     | P@ssw0rd  |

---

## 📌 Port Availability Reminder

Before starting the backend, ensure the following ports are **not used** by other apps:

| Port | Usage                         | When                    |
|------|-------------------------------|-------------------------|
| 4200 | Frontend (Angular)           | Local Development       |
| 4201 | Frontend (Docker)            | Docker Compose Setup    |
| 8080 | Backend API (Spring Boot)    | Local & Docker          |
| 3306 | MySQL Internal (Container)   | Docker Internal DB Port |
| 3307 | MySQL Exposed Port (Host)    | Docker Host Access      |

