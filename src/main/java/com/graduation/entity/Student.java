package com.graduation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity 1: Student entity
 * Stores student information and implements Comparable for sorting by average grade
 */
@Entity
@Table(name = "student")
public class Student implements Comparable<Student> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_id", nullable = false, unique = true)
    private String studentId;

    @Column(name = "average_grade")
    private Double averageGrade = 0.0;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Grade> grades = new ArrayList<>();

    @ManyToMany(mappedBy = "students")
    @JsonBackReference
    private List<Course> courses = new ArrayList<>();

    // Constructors
    public Student() {}

    public Student(String studentId) {
        this.studentId = studentId;
        this.averageGrade = 0.0;
    }

    public Student(String studentId, Double averageGrade) {
        this.studentId = studentId;
        this.averageGrade = averageGrade != null ? averageGrade : 0.0;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Double getAverageGrade() {
        return averageGrade;
    }

    public void setAverageGrade(Double averageGrade) {
        this.averageGrade = averageGrade != null ? averageGrade : 0.0;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    /**
     * Calculate average grade from grades list
     * Time Complexity: O(n) where n is number of grades
     */
    public void calculateAverageGrade() {
        if (grades == null || grades.isEmpty()) {
            this.averageGrade = 0.0;
            return;
        }

        double sum = 0.0;
        for (Grade grade : grades) {
            sum += grade.getGradeValue();
        }
        this.averageGrade = sum / grades.size();
    }

    /**
     * Add a grade to the student and recalculate average
     * Time Complexity: O(n) for average calculation
     */
    public void addGrade(Grade grade) {
        if (grades == null) {
            grades = new ArrayList<>();
        }
        grades.add(grade);
        grade.setStudent(this);
        calculateAverageGrade();
    }

    /**
     * Remove a grade from the student and recalculate average
     * Time Complexity: O(n) for average calculation
     */
    public void removeGrade(Grade grade) {
        if (grades != null) {
            grades.remove(grade);
            calculateAverageGrade();
        }
    }

    /**
     * Implementation of Comparable interface
     * Students are compared by average grade in descending order (highest first)
     * Time Complexity: O(1)
     */
    @Override
    public int compareTo(Student other) {
        if (other == null) {
            return -1;
        }

        // Compare by average grade (descending order)
        double thisGrade = this.averageGrade != null ? this.averageGrade : 0.0;
        double otherGrade = other.averageGrade != null ? other.averageGrade : 0.0;

        // For descending order: higher grades come first
        return Double.compare(otherGrade, thisGrade);
    }

    /**
     * Human-friendly string representation
     * Time Complexity: O(1)
     */
    @Override
    public String toString() {
        return String.format("Student{id='%s', averageGrade=%.2f, gradeCount=%d}",
                studentId,
                averageGrade != null ? averageGrade : 0.0,
                grades != null ? grades.size() : 0);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null ? studentId.equals(student.studentId) : student.studentId == null;
    }

    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}
