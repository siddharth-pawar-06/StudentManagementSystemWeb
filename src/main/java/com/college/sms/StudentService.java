package com.college.sms;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final List<Student> students = new ArrayList<>();

    @PostConstruct
    public void initSampleData() {
        // Pre-populate with realistic initial records for college demo
        if (students.isEmpty()) {
            add(new Student(101, "Alice Johnson", 20, "Computer Science", 92.5));
            add(new Student(102, "Bob Smith", 21, "Information Technology", 84.0));
            add(new Student(103, "Charlie Davis", 19, "Computer Science", 73.5));
            add(new Student(104, "Diana Prince", 22, "Data Science", 65.0));
            add(new Student(105, "Ethan Hunt", 20, "Cyber Security", 38.0));
        }
    }

    public boolean add(Student student) {
        if (findById(student.getId()).isPresent()) return false;
        students.add(student);
        return true;
    }

    public Optional<Student> findById(int id) {
        return students.stream().filter(s -> s.getId() == id).findFirst();
    }

    public List<Student> getAll() {
        List<Student> copy = new ArrayList<>(students);
        copy.sort(Comparator.comparingInt(Student::getId));
        return copy;
    }

    public boolean update(int id, String name, int age, String course, double marks) {
        Optional<Student> result = findById(id);
        if (result.isEmpty()) return false;
        Student old = result.get();
        old.setName(name);
        old.setAge(age);
        old.setCourse(course);
        old.setMarks(marks);
        return true;
    }

    public boolean delete(int id) {
        return students.removeIf(s -> s.getId() == id);
    }

    public List<Student> searchByName(String query) {
        if (query == null || query.trim().isEmpty()) {
            return getAll();
        }
        String q = query.toLowerCase().trim();
        List<Student> found = new ArrayList<>();
        for (Student s : students) {
            if (s.getName().toLowerCase().contains(q)) found.add(s);
        }
        return found;
    }

    public double getAverageMarks() {
        return students.stream().mapToDouble(Student::getMarks).average().orElse(0.0);
    }

    public int size() { return students.size(); }

    // Report helper calculations matching the original Main.java showReport() logic:
    public long getPassedCount() {
        return students.stream().filter(s -> s.getMarks() >= 40.0).count();
    }

    public long getFailedCount() {
        return size() - getPassedCount();
    }

    public Optional<Student> getTopStudent() {
        return students.stream().max(Comparator.comparingDouble(Student::getMarks));
    }
}
