package com.college.sms;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StudentServiceTest {

    private StudentService service;

    @BeforeEach
    void setUp() {
        service = new StudentService();
        // Clear any pre-populated data for isolated tests
        for (Student s : service.getAll()) {
            service.delete(s.getId());
        }
    }

    @Test
    void testStudentValidationAndGrades() {
        Student s1 = new Student(1, "Alice", 20, "CS", 95.0);
        assertEquals("A+", s1.getGrade());

        Student s2 = new Student(2, "Bob", 21, "IT", 85.0);
        assertEquals("A", s2.getGrade());

        Student s3 = new Student(3, "Charlie", 22, "CS", 75.0);
        assertEquals("B", s3.getGrade());

        Student s4 = new Student(4, "Dave", 23, "CS", 65.0);
        assertEquals("C", s4.getGrade());

        Student s5 = new Student(5, "Eve", 24, "CS", 45.0);
        assertEquals("D", s5.getGrade());

        Student s6 = new Student(6, "Frank", 25, "CS", 30.0);
        assertEquals("F", s6.getGrade());

        // Validate exception triggers from domain model
        assertThrows(IllegalArgumentException.class, () -> new Student(7, "", 20, "CS", 50.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(8, "Valid", 0, "CS", 50.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(9, "Valid", 20, "", 50.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(10, "Valid", 20, "CS", 105.0));
    }

    @Test
    void testAddAndPreventDuplicates() {
        Student s1 = new Student(101, "Alice", 20, "CS", 90.0);
        assertTrue(service.add(s1));
        assertEquals(1, service.size());

        // Attempt duplicate ID
        Student duplicate = new Student(101, "Another Alice", 21, "IT", 80.0);
        assertFalse(service.add(duplicate), "Service must prevent duplicate student IDs");
        assertEquals(1, service.size());
    }

    @Test
    void testFindById() {
        service.add(new Student(101, "Alice", 20, "CS", 90.0));
        Optional<Student> found = service.findById(101);
        assertTrue(found.isPresent());
        assertEquals("Alice", found.get().getName());

        Optional<Student> notFound = service.findById(999);
        assertTrue(notFound.isEmpty());
    }

    @Test
    void testUpdateStudent() {
        service.add(new Student(101, "Alice", 20, "CS", 90.0));
        boolean updated = service.update(101, "Alice Smith", 21, "Data Science", 95.0);
        assertTrue(updated);

        Student s = service.findById(101).orElseThrow();
        assertEquals("Alice Smith", s.getName());
        assertEquals(21, s.getAge());
        assertEquals("Data Science", s.getCourse());
        assertEquals(95.0, s.getMarks());

        // Updating non-existent ID
        assertFalse(service.update(999, "Non Existent", 20, "CS", 80.0));
    }

    @Test
    void testDeleteStudent() {
        service.add(new Student(101, "Alice", 20, "CS", 90.0));
        assertEquals(1, service.size());

        assertTrue(service.delete(101));
        assertEquals(0, service.size());
        assertFalse(service.delete(101));
    }

    @Test
    void testSearchByName() {
        service.add(new Student(101, "Alice Johnson", 20, "CS", 90.0));
        service.add(new Student(102, "Bob Smith", 21, "IT", 80.0));
        service.add(new Student(103, "Charlie Davis", 22, "CS", 70.0));

        List<Student> results = service.searchByName("alice");
        assertEquals(1, results.size());
        assertEquals("Alice Johnson", results.get(0).getName());

        List<Student> smithResults = service.searchByName("smith");
        assertEquals(1, smithResults.size());
        assertEquals("Bob Smith", smithResults.get(0).getName());

        List<Student> emptyQuery = service.searchByName("");
        assertEquals(3, emptyQuery.size());
    }

    @Test
    void testReportCalculations() {
        service.add(new Student(101, "Alice", 20, "CS", 95.0));
        service.add(new Student(102, "Bob", 21, "IT", 85.0));
        service.add(new Student(103, "Charlie", 22, "CS", 35.0)); // Failed

        assertEquals(3, service.size());
        assertEquals(2, service.getPassedCount());
        assertEquals(1, service.getFailedCount());
        assertEquals((95.0 + 85.0 + 35.0) / 3.0, service.getAverageMarks(), 0.001);

        Optional<Student> top = service.getTopStudent();
        assertTrue(top.isPresent());
        assertEquals(101, top.get().getId());
        assertEquals(95.0, top.get().getMarks());
    }
}
