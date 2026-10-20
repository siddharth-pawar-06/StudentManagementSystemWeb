package com.college.sms.controller;

import com.college.sms.Student;
import com.college.sms.StudentService;
import com.college.sms.model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Dashboard / Home Page
     */
    @GetMapping("/")
    public String index(Model model) {
        List<Student> allStudents = studentService.getAll();
        model.addAttribute("students", allStudents);
        model.addAttribute("totalStudents", studentService.size());
        model.addAttribute("averageMarks", String.format(Locale.US, "%.2f", studentService.getAverageMarks()));
        model.addAttribute("passedCount", studentService.getPassedCount());
        model.addAttribute("failedCount", studentService.getFailedCount());
        model.addAttribute("topStudent", studentService.getTopStudent().orElse(null));
        return "index";
    }

    /**
     * View All Students list with search & filter support
     */
    @GetMapping("/students")
    public String listStudents(@RequestParam(value = "searchType", required = false, defaultValue = "name") String searchType,
                               @RequestParam(value = "query", required = false) String query,
                               Model model) {
        List<Student> results;
        if (query != null && !query.trim().isEmpty()) {
            String trimmed = query.trim();
            if ("id".equalsIgnoreCase(searchType)) {
                try {
                    int id = Integer.parseInt(trimmed);
                    results = studentService.findById(id).map(List::of).orElseGet(Collections::emptyList);
                } catch (NumberFormatException e) {
                    results = Collections.emptyList();
                    model.addAttribute("errorMessage", "Invalid ID: please enter a numeric value.");
                }
            } else {
                results = studentService.searchByName(trimmed);
            }
            model.addAttribute("isSearch", true);
            model.addAttribute("searchQuery", trimmed);
            model.addAttribute("searchType", searchType);
        } else {
            results = studentService.getAll();
            model.addAttribute("isSearch", false);
            model.addAttribute("searchQuery", "");
            model.addAttribute("searchType", searchType);
        }

        model.addAttribute("students", results);
        return "students";
    }

    /**
     * Show form to add a new student
     */
    @GetMapping("/students/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("studentForm")) {
            model.addAttribute("studentForm", new StudentForm());
        }
        return "add-student";
    }

    /**
     * Process add student form submission
     */
    @PostMapping("/students/add")
    public String processAdd(@ModelAttribute("studentForm") StudentForm form,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        try {
            if (form.getId() == null) {
                throw new IllegalArgumentException("Student ID cannot be empty.");
            }
            if (form.getAge() == null) {
                throw new IllegalArgumentException("Age cannot be empty.");
            }
            if (form.getMarks() == null) {
                throw new IllegalArgumentException("Marks cannot be empty.");
            }

            // Directly creates Student using the original domain model and its validations
            Student student = new Student(form.getId(), form.getName(), form.getAge(), form.getCourse(), form.getMarks());

            // Uses original business logic to check uniqueness and add
            boolean added = studentService.add(student);
            if (!added) {
                model.addAttribute("errorMessage", "A student with ID " + form.getId() + " already exists.");
                model.addAttribute("studentForm", form);
                return "add-student";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Student " + student.getName() + " (ID: " + student.getId() + ") added successfully!");
            return "redirect:/students";

        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("studentForm", form);
            return "add-student";
        } catch (Exception ex) {
            model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
            model.addAttribute("studentForm", form);
            return "add-student";
        }
    }

    /**
     * Show form to edit an existing student
     */
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Student> studentOpt = studentService.findById(id);
        if (studentOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Student with ID " + id + " was not found.");
            return "redirect:/students";
        }

        Student student = studentOpt.get();
        StudentForm form = new StudentForm(student.getId(), student.getName(), student.getAge(), student.getCourse(), student.getMarks());
        model.addAttribute("studentForm", form);
        return "edit-student";
    }

    /**
     * Process edit student form submission
     */
    @PostMapping("/students/edit/{id}")
    public String processEdit(@PathVariable("id") int id,
                              @ModelAttribute("studentForm") StudentForm form,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        try {
            if (form.getAge() == null) throw new IllegalArgumentException("Age cannot be empty.");
            if (form.getMarks() == null) throw new IllegalArgumentException("Marks cannot be empty.");

            // Update using existing service logic and model validations
            boolean updated = studentService.update(id, form.getName(), form.getAge(), form.getCourse(), form.getMarks());
            if (!updated) {
                redirectAttributes.addFlashAttribute("errorMessage", "Student with ID " + id + " was not found.");
                return "redirect:/students";
            }

            redirectAttributes.addFlashAttribute("successMessage", "Student ID " + id + " updated successfully!");
            return "redirect:/students";

        } catch (IllegalArgumentException ex) {
            form.setId(id);
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("studentForm", form);
            return "edit-student";
        } catch (Exception ex) {
            form.setId(id);
            model.addAttribute("errorMessage", "An unexpected error occurred: " + ex.getMessage());
            model.addAttribute("studentForm", form);
            return "edit-student";
        }
    }

    /**
     * Delete a student
     */
    @RequestMapping(value = "/students/delete/{id}", method = {RequestMethod.POST, RequestMethod.GET})
    public String deleteStudent(@PathVariable("id") int id, RedirectAttributes redirectAttributes) {
        boolean deleted = studentService.delete(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Student with ID " + id + " was deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Student with ID " + id + " was not found.");
        }
        return "redirect:/students";
    }

    /**
     * Class Report page (matches Main.java showReport())
     */
    @GetMapping("/report")
    public String showReport(Model model) {
        List<Student> students = studentService.getAll();
        model.addAttribute("students", students);
        model.addAttribute("totalStudents", students.size());

        if (!students.isEmpty()) {
            double avg = studentService.getAverageMarks();
            long passed = studentService.getPassedCount();
            long failed = studentService.getFailedCount();
            Student top = studentService.getTopStudent().orElse(null);

            double passRate = (double) passed / students.size() * 100.0;
            double failRate = (double) failed / students.size() * 100.0;

            // Grade distribution
            long countAPlus = students.stream().filter(s -> "A+".equals(s.getGrade())).count();
            long countA = students.stream().filter(s -> "A".equals(s.getGrade())).count();
            long countB = students.stream().filter(s -> "B".equals(s.getGrade())).count();
            long countC = students.stream().filter(s -> "C".equals(s.getGrade())).count();
            long countD = students.stream().filter(s -> "D".equals(s.getGrade())).count();
            long countF = students.stream().filter(s -> "F".equals(s.getGrade())).count();

            model.addAttribute("averageMarks", String.format(Locale.US, "%.2f", avg));
            model.addAttribute("passedCount", passed);
            model.addAttribute("failedCount", failed);
            model.addAttribute("passRate", String.format(Locale.US, "%.1f", passRate));
            model.addAttribute("failRate", String.format(Locale.US, "%.1f", failRate));
            model.addAttribute("topStudent", top);

            model.addAttribute("countAPlus", countAPlus);
            model.addAttribute("countA", countA);
            model.addAttribute("countB", countB);
            model.addAttribute("countC", countC);
            model.addAttribute("countD", countD);
            model.addAttribute("countF", countF);
        }

        return "report";
    }
}
