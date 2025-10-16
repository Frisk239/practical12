package com.graduation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Activity 1: Grade entity
 * Represents a single grade for a student
 */
@Entity
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @Column(name = "grade_value", nullable = false)
    private Double gradeValue;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructors
    public Grade() {
        this.createdAt = LocalDateTime.now();
    }

    public Grade(Double gradeValue) {
        this.gradeValue = gradeValue;
        this.createdAt = LocalDateTime.now();
    }

    public Grade(Student student, Double gradeValue) {
        this.student = student;
        this.gradeValue = gradeValue;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Double getGradeValue() {
        return gradeValue;
    }

    public void setGradeValue(Double gradeValue) {
        this.gradeValue = gradeValue;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * String representation of grade
     * Time Complexity: O(1)
     */
    @Override
    public String toString() {
        return String.format("Grade{id=%d, value=%.2f, studentId='%s'}",
                id,
                gradeValue,
                student != null ? student.getStudentId() : "null");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Grade grade = (Grade) obj;
        return id != null ? id.equals(grade.id) : grade.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
