package com.college.sms.model;

/**
 * Form backing object for web input binding.
 * Bridges HTML forms to the original Student domain model.
 */
public class StudentForm {
    private Integer id;
    private String name;
    private Integer age;
    private String course;
    private Double marks;

    public StudentForm() {}

    public StudentForm(Integer id, String name, Integer age, String course, Double marks) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.course = course;
        this.marks = marks;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public Double getMarks() { return marks; }
    public void setMarks(Double marks) { this.marks = marks; }
}
