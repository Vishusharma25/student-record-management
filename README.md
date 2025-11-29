# Student Management System (Java Swing + MySQL)

A robust desktop application for managing student records, featuring a Graphical User Interface (GUI) built with Java Swing and a MySQL database backend.

## 🚀 Project Overview
- **Type:** Desktop Application
- **Language:** Java (JDK 8+)
- **Database:** MySQL
- **Interface:** Java Swing (GUI)

## 📂 File Structure
- `StudentManagementSystem.java`: The main application source code.
- `database_setup.sql`: SQL script to initialize the database.
- `index.html` / `style.css`: Web presentation of this project (Portfolio).

## 🛠️ How to Run the Application (Locally)

### Prerequisites
1. **Java Development Kit (JDK)** installed.
2. **MySQL Server** installed and running.
3. **MySQL JDBC Driver** (connector jar file).

### Step 1: Database Setup
1. Open MySQL Workbench or your Command Line Client.
2. Open the `database_setup.sql` file provided in this repository.
3. Run the script to create the `student` database and `students` table.

### Step 2: Configure the Code
1. Open `StudentManagementSystem.java`.
2. Locate the **Configuration Section** at the top of the file.
3. Update `DB_USER` and `DB_PASSWORD` to match your local MySQL credentials.

### Step 3: Run
1. Compile the code: 
   `javac -cp .;mysql-connector-j-8.0.33.jar StudentManagementSystem.java`
2. Run the application:
   `java -cp .;mysql-connector-j-8.0.33.jar StudentManagementSystem`

## 🌐 Viewing the Project Presentation
The `index.html` file included in this repository is a **presentation slide deck** showcasing the project. You can view it live via GitHub Pages to understand the project features without running the Java code.

---
**Developed by Group 5**
