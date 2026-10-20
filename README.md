# Student Management System — Modern Web Edition (Pure Java Full-Stack)

A college final-year full-stack web application built by modernizing an existing console-based Java project into a simple, functional, responsive web application using **Spring Boot**, **Thymeleaf**, and **Bootstrap 5**, while **reusing 100% of the original Java domain models, calculations, and business logic**.

---

## 📌 Project Overview

This project transforms the original console-based `StudentManagementSystem` into an interactive web application. The original core logic—including validations, grade calculations (`getGrade()`), unique ID enforcement, class averages, pass/fail counts, and top-student detection—has been preserved and connected to a clean, responsive web interface.

- **Backend:** Java 17+ (tested up to OpenJDK 25), Spring Boot 3.3.4, Spring MVC, Spring Web
- **Frontend:** HTML5, CSS3, JavaScript (ES6), Thymeleaf template engine, Bootstrap 5.3.3, Bootstrap Icons
- **Build System:** Apache Maven 3.x
- **Data Persistence:** In-memory collection (`List<Student>`), preserving the original project architecture with pre-seeded sample data for immediate college demonstrations.

---

## 🚀 Quick Start (Run on Windows)

### Prerequisites
Make sure you have installed:
1. **Java Development Kit (JDK 17 or later):** Verify by running `java -version`
2. **Apache Maven (3.8+):** Verify by running `mvn -version`

### Option 1: Run with Maven (Recommended for Development)
Open PowerShell or Command Prompt in the project root directory (`StudentManagementSystemWeb`):

```powershell
mvn spring-boot:run
```

### Option 2: Run with the Pre-Packaged Executable JAR
The project includes a ready-to-run Spring Boot executable JAR in the `target/` directory:

```powershell
java -jar target/student-management-system-1.0.0.jar
```

### Option 3: Build & Package from Scratch
To run automated tests and build a fresh standalone JAR:

```powershell
mvn clean package
java -jar target/student-management-system-1.0.0.jar
```

---

## 🌐 Opening the Website

Once the application starts and prints `Started StudentManagementWebApplication in ... seconds`, open your web browser and navigate to:

👉 **http://localhost:8080**

### Available Pages & Routes
| URL | Page | Description |
|---|---|---|
| `http://localhost:8080/` | **Dashboard** | Overview with KPI summary cards, top student spotlight, quick actions, and recent records. |
| `http://localhost:8080/students` | **Student Directory** | Full table of students with search bar (by Name or by ID), grade badges, and edit/delete actions. |
| `http://localhost:8080/students/add` | **Add Student** | Web form with field validation and real-time interactive grade calculation. |
| `http://localhost:8080/students/edit/{id}` | **Edit Student** | Pre-populated form to update student name, age, course, or marks. |
| `http://localhost:8080/report` | **Class Performance Report** | Academic analytics with class average, pass/fail rate, grade distribution progress bars, and print/PDF support. |
| `http://localhost:8080/api/students` | **REST API (JSON)** | Returns all students in JSON format for API demonstration. |
| `http://localhost:8080/api/report` | **REST API (JSON)** | Returns computed class analytics in JSON format. |

---

## 🎓 How to Open in IntelliJ IDEA

1. Open **IntelliJ IDEA**.
2. Click **File -> Open...** (or click **Open** on the welcome screen).
3. Select the `StudentManagementSystemWeb` folder and click **OK**.
4. Choose **Open as Project** and select **Trust Project** if prompted.
5. IntelliJ will automatically detect the `pom.xml` file and download any required Maven dependencies in the background.
6. In the Project tool window (`Alt + 1`), expand:
   `src/main/java/com/college/sms/StudentManagementWebApplication.java`
7. Right-click `StudentManagementWebApplication.java` and click **Run 'StudentManagementWebApplication'**.
8. Once started, open `http://localhost:8080` in your browser.

---

## 🔄 How the Original Java Logic was Reused

| Original Console Code (`StudentManagementSystem`) | Web Implementation (`StudentManagementSystemWeb`) | Reusability Status |
|---|---|---|
| `com.college.sms.Student` | `com.college.sms.Student` | **100% Reused** (Preserved all validations: non-empty name/course, age 1-120, marks 0-100, and exact `getGrade()` logic). |
| `StudentService.add()` | `StudentService.add()` | **100% Reused** (Preserved duplicate ID prevention and collection storage). |
| `StudentService.findById()` | `StudentService.findById()` | **100% Reused** (`Optional<Student>` lookup). |
| `StudentService.getAll()` | `StudentService.getAll()` | **100% Reused** (Sorted copy by student ID). |
| `StudentService.update()` | `StudentService.update()` | **100% Reused** (Updates name, age, course, marks). |
| `StudentService.delete()` | `StudentService.delete()` | **100% Reused** (`removeIf` by student ID). |
| `StudentService.searchByName()` | `StudentService.searchByName()` | **100% Reused** (Case-insensitive substring search). |
| `StudentService.getAverageMarks()` | `StudentService.getAverageMarks()` | **100% Reused** (Java Stream `average()`). |
| `Main.java` Menu & `showReport()` | `StudentController` & `report.html` | **Modernized** (Console `System.out.printf` replaced by Thymeleaf template with Bootstrap cards, badges, and progress bars). |
| `Scanner` and `System.in` | `StudentForm` & Spring MVC `@PostMapping` | **Modernized** (Interactive web forms with client-side & server-side validation). |

---

## 📂 Project Structure

```text
StudentManagementSystemWeb/
├── pom.xml                                     # Maven build file with Spring Boot & Thymeleaf dependencies
├── README.md                                   # Complete guide & documentation
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── college/
│   │   │           └── sms/
│   │   │               ├── StudentManagementWebApplication.java  # Spring Boot entry point
│   │   │               ├── Student.java                          # Original Domain Model (Reused)
│   │   │               ├── StudentService.java                   # Original Business Logic (Reused as @Service)
│   │   │               ├── controller/
│   │   │               │   ├── StudentController.java            # Spring MVC Web Controller
│   │   │               │   └── StudentApiController.java         # REST API Controller for JSON
│   │   │               └── model/
│   │   │                   └── StudentForm.java                  # Web Form Data Transfer Object
│   │   └── resources/
│   │       ├── application.properties                            # Application configuration
│   │       ├── static/
│   │       │   ├── css/
│   │       │   │   └── style.css                                 # Custom responsive stylesheet & badge styles
│   │       │   └── js/
│   │       │       └── main.js                                   # Client-side dynamic grade preview & delete prompts
│   │       └── templates/
│   │           ├── fragments/
│   │           │   └── layout.html                               # Shared Navbar and Footer Thymeleaf fragment
│   │           ├── index.html                                    # Dashboard / Home page
│   │           ├── students.html                                 # Student list with search & actions
│   │           ├── add-student.html                              # Add student form
│   │           ├── edit-student.html                             # Edit student form
│   │           └── report.html                                   # Class analytics & performance report
│   └── test/
│       └── java/
│           └── com/
│               └── college/
│                   └── sms/
│                       ├── StudentServiceTest.java               # 7 Unit tests for business logic & calculations
│                       └── StudentControllerTest.java            # 7 MockMvc integration tests for web endpoints
└── target/
    └── student-management-system-1.0.0.jar                       # Ready-to-run executable Spring Boot JAR
```

---

## 🧪 Testing Summary

The project includes an automated test suite verifying both the core business logic and all web endpoints:
- **`StudentServiceTest`**: Tests student validation, grade boundaries (A+, A, B, C, D, F), duplicate ID prevention, find, update, delete, search, class average, pass/fail calculations, and top student detection.
- **`StudentControllerTest`**: Tests MockMvc requests to `/`, `/students`, `/students/add`, `/report`, `/api/students`, and form submission with error validation.

Run tests at any time via:
```powershell
mvn test
```
Result: **14 tests run, 0 failures, 0 errors.**
