-- 1. Create the Database
CREATE DATABASE IF NOT EXISTS student;

-- 2. Select the Database
USE student;

-- 3. Create the Users Table
-- Matches the columns used in your Java code
CREATE TABLE IF NOT EXISTS students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(255) NOT NULL,
    student_id INT UNIQUE NOT NULL,
    student_grade DECIMAL(5,2),
    dob DATE,
    gender VARCHAR(10),
    contact VARCHAR(15),
    email VARCHAR(255)
);

-- 4. Insert Dummy Data (Optional, for testing)
INSERT INTO students (student_name, student_id, student_grade, dob, gender, contact, email) 
VALUES 
('Albert Einstein', 101, 98.50, '2004-03-14', 'Male', '9876543210', 'albert@physics.com'),
('Marie Curie', 102, 99.00, '2004-11-07', 'Female', '9123456789', 'marie@chemistry.com');
