package com.college.sms;

public class Student {
    private final int id;
    private String name;
    private int age;
    private String course;
    private double marks;

    public Student(int id, String name, int age, String course, double marks) {
        this.id = id;
        setName(name);
        setAge(age);
        setCourse(course);
        setMarks(marks);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getCourse() { return course; }
    public double getMarks() { return marks; }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty.");
        this.name = name.trim();
    }
    public void setAge(int age) {
        if (age < 1 || age > 120) throw new IllegalArgumentException("Age must be between 1 and 120.");
        this.age = age;
    }
    public void setCourse(String course) {
        if (course == null || course.trim().isEmpty()) throw new IllegalArgumentException("Course cannot be empty.");
        this.course = course.trim();
    }
    public void setMarks(double marks) {
        if (marks < 0 || marks > 100) throw new IllegalArgumentException("Marks must be between 0 and 100.");
        this.marks = marks;
    }
    public String getGrade() {
        if (marks >= 90) return "A+";
        if (marks >= 80) return "A";
        if (marks >= 70) return "B";
        if (marks >= 60) return "C";
        if (marks >= 40) return "D";
        return "F";
    }
}
