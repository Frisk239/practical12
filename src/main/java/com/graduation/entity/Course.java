package com.graduation.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.*;
import java.util.LinkedList;

/**
 * Activity 1: Course entity
 * Manages students with LinkedList and maintains descending order by average grade
 */
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false, unique = true)
    private String courseId;

    @Column(name = "academic_year", nullable = false)
    private String academicYear;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "course_student",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    @JsonManagedReference
    private List<Student> students = new LinkedList<>();

    // Constructors
    public Course() {}

    public Course(String courseId, String academicYear) {
        this.courseId = courseId;
        this.academicYear = academicYear;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    /**
     * Add a student to the course and maintain descending order by average grade
     * Time Complexity: O(n log n) due to sorting after insertion
     * Activity 1 Requirement: Maintain list in descending order based on student average grade
     */
    public void addStudent(Student student) {
        if (student == null) {
            return;
        }

        // Check if student is already enrolled
        for (Student existing : students) {
            if (existing.getStudentId().equals(student.getStudentId())) {
                return; // Already enrolled
            }
        }

        students.add(student);
        sortStudentsByGradeDescending();
        printStudentList();
    }

    /**
     * Remove a student from the course
     * Time Complexity: O(n) for removal and printing
     * Activity 1 Requirement: Allow students to be removed from the student list
     */
    public boolean removeStudent(Student student) {
        if (student == null) {
            return false;
        }

        boolean removed = students.removeIf(s -> s.getStudentId().equals(student.getStudentId()));
        if (removed) {
            printStudentList();
        }
        return removed;
    }

    /**
     * Remove student by student ID
     * Time Complexity: O(n) for removal and printing
     */
    public boolean removeStudentById(String studentId) {
        if (studentId == null) {
            return false;
        }

        boolean removed = students.removeIf(s -> s.getStudentId().equals(studentId));
        if (removed) {
            printStudentList();
        }
        return removed;
    }

    /**
     * Update grades for a student and resort the list
     * Time Complexity: O(n log n) due to sorting
     * Activity 1 Requirement: Allow grades to be updated and reflect in ordering
     */
    public void updateStudentGrades(Student student) {
        if (student == null) {
            return;
        }

        // Find and update the student in our list
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(student.getStudentId())) {
                students.set(i, student);
                sortStudentsByGradeDescending();
                printStudentList();
                return;
            }
        }
    }

    /**
     * Sort students by average grade in descending order (highest first)
     * Time Complexity: O(n log n) using Collections.sort
     */
    private void sortStudentsByGradeDescending() {
        students.sort((s1, s2) -> s2.compareTo(s1)); // Descending order
    }

    /**
     * Print the current student list after every change
     * Activity 1 Requirement: Print student list after every change
     * Time Complexity: O(n)
     */
    public void printStudentList() {
        System.out.println("=== Course: " + courseId + " (" + academicYear + ") ===");
        System.out.println("Current Students (sorted by average grade descending):");

        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
        } else {
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                System.out.printf("%d. %s%n", i + 1, student.toString());
            }
        }
        System.out.println("Total students: " + students.size());
        System.out.println("=====================================");
    }

    /**
     * Get student count
     * Time Complexity: O(1)
     */
    public int getStudentCount() {
        return students.size();
    }

    /**
     * Check if student is enrolled
     * Time Complexity: O(n)
     */
    public boolean isStudentEnrolled(String studentId) {
        if (studentId == null) {
            return false;
        }
        return students.stream().anyMatch(s -> s.getStudentId().equals(studentId));
    }

    /**
     * Get student by ID
     * Time Complexity: O(n)
     */
    public Student getStudentById(String studentId) {
        if (studentId == null) {
            return null;
        }
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * String representation of course
     * Time Complexity: O(1)
     */
    @Override
    public String toString() {
        return String.format("Course{id='%s', academicYear='%s', studentCount=%d}",
                courseId, academicYear, students.size());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Course course = (Course) obj;
        return courseId != null ? courseId.equals(course.courseId) : course.courseId == null;
    }

    @Override
    public int hashCode() {
        return courseId != null ? courseId.hashCode() : 0;
    }
}
