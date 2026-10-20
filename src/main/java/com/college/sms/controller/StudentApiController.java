package com.college.sms.controller;

import com.college.sms.Student;
import com.college.sms.StudentService;
import com.college.sms.model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class StudentApiController {

    private final StudentService studentService;

    @Autowired
    public StudentApiController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentService.getAll();
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable int id) {
        Optional<Student> student = studentService.findById(id);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Student with ID " + id + " not found"));
    }

    @PostMapping("/students")
    public ResponseEntity<?> createStudent(@RequestBody StudentForm form) {
        try {
            if (form.getId() == null || form.getName() == null || form.getAge() == null
                    || form.getCourse() == null || form.getMarks() == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "All fields are required"));
            }

            Student student = new Student(form.getId(), form.getName(), form.getAge(), form.getCourse(), form.getMarks());
            boolean added = studentService.add(student);
            if (!added) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Student ID " + form.getId() + " already exists"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable int id) {
        boolean deleted = studentService.delete(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("message", "Student deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Student with ID " + id + " not found"));
    }

    @GetMapping("/report")
    public Map<String, Object> getReport() {
        Map<String, Object> report = new HashMap<>();
        List<Student> students = studentService.getAll();
        report.put("totalStudents", students.size());
        report.put("averageMarks", studentService.getAverageMarks());
        report.put("passedCount", studentService.getPassedCount());
        report.put("failedCount", studentService.getFailedCount());
        report.put("topStudent", studentService.getTopStudent().orElse(null));
        return report;
    }
}
