# LeaveEase.UI (Frontend)

This is the frontend of the **LeaveEase** HR leave management system, developed with Angular 17 and styled using Tailwind CSS. It supports three user roles — Normal Staff, Department Manager, and HR Admin — each with tailored functionalities.

## 🚀 Option 1 – Run via Docker (Recommended)

### Prerequisites
- Docker installed on your system
- PowerShell (for Windows script execution)

### Steps
1. **Download** the Docker archive from: [https://drive.google.com/file/d/1x1jPuMxBbTs8C97wD15S8VTryqVuzgAy/view?usp=sharing](https://drive.google.com/file/d/1x1jPuMxBbTs8C97wD15S8VTryqVuzgAy/view?usp=sharing)
   

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

5. **Wait 1 minute** for all containers to initialize, then open your browser:  
   [http://localhost:4201](http://localhost:4201)

---

## 🛠️ Option 2 – Local Development Setup

### Prerequisites

- **Node.js** (includes npm):  
  👉 [https://nodejs.org/en/download](https://nodejs.org/en/download)

- **Angular CLI** (install after Node):  
  ```bash
  npm install -g @angular/cli
  ```

---

### Setup Steps

```bash
cd LeaveEase.UI
npm install
ng serve
```

- The app will run on: [http://localhost:4200](http://localhost:4200)

---

## ⚙️ Configuration Notes

The frontend communicates with the backend API via:

```ts
// File: src/appconfig.ts (Line 16)
export const baseUrl = 'http://localhost:8080/api';
```

---

## 🐛 Known Angular 19 Build Warning

During hot reload or rebuild, the following console error might appear:

```bash
TypeError: egetOrCreateAngularServerApp is not a function
```

✅ This is a known Angular 19 SSR-related issue.  
💡 **Solution**: Just refresh the browser. It does not affect functionality.

---

## 🔐 Test User Accounts

If you've run the `initial_reset_database.sql` from the backend repo, use these:

| Name    | Role                    | Username | Password  |
|---------|-------------------------|----------|-----------|
| Amy     | Normal Staff (Marketing)| A001     | P@ssw0rd  |
| Bob     | Department Manager      | B001     | P@ssw0rd  |
| Cindy   | HR Admin                | C001     | P@ssw0rd  |
| Daniel  | Normal Staff (Engineer) | D001     | P@ssw0rd  |

---

## 📌 Port Availability Reminder

Before starting the frontend, ensure the following ports are **not used** by other apps:

| Port | Usage                         | When                    |
|------|-------------------------------|-------------------------|
| 4200 | Frontend (Angular)           | Local Development       |
| 4201 | Frontend (Docker)            | Docker Compose Setup    |
| 8080 | Backend API (Spring Boot)    | Local & Docker          |
| 3306 | MySQL Internal (Container)   | Docker Internal DB Port |
| 3307 | MySQL Exposed Port (Host)    | Docker Host Access      |

