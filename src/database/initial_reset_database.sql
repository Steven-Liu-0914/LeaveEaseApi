-- 1. Drop and recreate database
DROP DATABASE IF EXISTS leave_ease;
CREATE DATABASE leave_ease;
USE leave_ease;

-- 2. Table: staff
CREATE TABLE staff (
                       StaffId INT AUTO_INCREMENT PRIMARY KEY,
                       StaffNumber NVARCHAR(50),
                       FullName NVARCHAR(100),
                       Email NVARCHAR(100),
                       Phone NVARCHAR(20),
                       Department NVARCHAR(100),
                       JobTitle NVARCHAR(100),
                       PasswordHash NVARCHAR(255),
                       PasswordSalt NVARCHAR(255),
                       Role NVARCHAR(50)
);

-- 3. Table: leaveApplication
CREATE TABLE leaveApplication (
                                  LeaveApplicationId INT AUTO_INCREMENT PRIMARY KEY,
                                  StaffId INT,
                                  LeaveType NVARCHAR(50),
                                  StartDate DATE,
                                  EndDate DATE,
                                  Reason NVARCHAR(500),
                                  Status NVARCHAR(20),
                                  CreatedAt DATETIME DEFAULT NOW(),
                                  UpdatedAt DATETIME DEFAULT NOW() ON UPDATE NOW(),
                                  FOREIGN KEY (StaffId) REFERENCES staff(StaffId)
);

-- 4. Table: leaveQuota
CREATE TABLE leaveQuota (
                            LeaveQuotaId INT AUTO_INCREMENT PRIMARY KEY,
                            StaffId INT,
                            Annual INT DEFAULT 14,
                            Children INT DEFAULT 6,
                            Sick INT DEFAULT 14,
                            Emergency INT DEFAULT 5,
                            UpdatedAt DATETIME DEFAULT NOW() ON UPDATE NOW(),
                            FOREIGN KEY (StaffId) REFERENCES staff(StaffId)
);

-- 5. Table: publicHoliday
CREATE TABLE publicHoliday (
                               PublicHolidayId INT AUTO_INCREMENT PRIMARY KEY,
                               Name NVARCHAR(100),
                               Date DATE,
                               Day NVARCHAR(20)
);

-- 6. View: userprofileview
CREATE
ALGORITHM = UNDEFINED
    DEFINER = `root`@`localhost`
    SQL SECURITY DEFINER
VIEW userprofileview AS
SELECT
    StaffId,
    FullName,
    Email,
    Phone,
    Department,
    JobTitle
FROM staff;

-- 7. Sample staff
INSERT INTO staff
(StaffNumber, FullName, Email, Phone, Department, JobTitle, PasswordHash, PasswordSalt, Role)
VALUES
    ('A001', 'Amy Tan', 'amy.tan@example.com', '81234567', 'Marketing', 'Executive', '5ea90d5f3adb038a880f664546be500378a4d86146cbefdc30073b07d00229d9', 'a1b2c3d4e5f6g7h8', 'user'),
    ('B001', 'Bob Lim', 'bob.lim@example.com', '81234568', 'Marketing', 'Manager', '5ea90d5f3adb038a880f664546be500378a4d86146cbefdc30073b07d00229d9', 'a1b2c3d4e5f6g7h8', 'admin'),
    ('C001', 'Cindy Goh', 'cindy.goh@example.com', '81234569', 'Human Resources', 'HR Admin', '5ea90d5f3adb038a880f664546be500378a4d86146cbefdc30073b07d00229d9', 'a1b2c3d4e5f6g7h8', 'admin'),
    ('D001', 'Daniel Smith', 'daniel.smith@example.com', '82223645', 'Engineering', 'Technician', '5ea90d5f3adb038a880f664546be500378a4d86146cbefdc30073b07d00229d9', 'a1b2c3d4e5f6g7h8', 'user');

-- 8. Leave Quota
INSERT INTO leaveQuota (StaffId, Annual, Children, Sick, Emergency) VALUES
                                                                        (1, 10, 5, 14, 2),
                                                                        (2, 16, 5, 14, 5),
                                                                        (3, 16, 5, 14, 5),
                                                                        (4, 10, 5, 14, 5);

-- 9. Public Holidays
INSERT INTO publicHoliday (Name, Date, Day) VALUES
                                                ('New Year’s Day', '2025-01-01', 'Wednesday'),
                                                ('Chinese New Year', '2025-01-29', 'Wednesday'),
                                                ('Chinese New Year', '2025-01-30', 'Thursday'),
                                                ('Hari Raya Puasa', '2025-03-31', 'Monday'),
                                                ('Good Friday', '2025-04-18', 'Friday'),
                                                ('Labour Day', '2025-05-01', 'Thursday'),
                                                ('Vesak Day', '2025-05-12', 'Monday'),
                                                ('Hari Raya Haji', '2025-06-07', 'Saturday'),
                                                ('National Day', '2025-08-09', 'Saturday'),
                                                ('Deepavali', '2025-10-20', 'Monday'),
                                                ('Christmas Day', '2025-12-25', 'Thursday');

-- 10. Sample Leave Applications
INSERT INTO leaveApplication
(StaffId, LeaveType, StartDate, EndDate, Reason, Status)
VALUES
    (2, 'Annual Leave', '2025-04-14', '2025-04-16', 'HOLIDAY', 'Approved'),
    (2, 'Children Leave', '2025-04-28', '2025-04-30', 'Take Care Children', 'Approved'),
    (4, 'Children Leave', '2025-04-28', '2025-04-30', 'Take Care Children', 'Pending');
