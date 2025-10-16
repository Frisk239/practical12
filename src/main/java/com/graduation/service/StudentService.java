package com.graduation.service;

import com.graduation.entity.Course;
import com.graduation.entity.Grade;
import com.graduation.entity.Student;
import com.graduation.repository.CourseRepository;
import com.graduation.repository.GradeRepository;
import com.graduation.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Activity 1: Student Service
 * Business logic layer for student operations
 * Activity 2: Complexity Analysis included in comments
 */
@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * Create a new student
     * Time Complexity: O(1) for database insert
     */
    public Student createStudent(String studentId) {
        if (studentRepository.existsByStudentId(studentId)) {
            throw new IllegalArgumentException("Student with ID " + studentId + " already exists");
        }

        Student student = new Student(studentId);
        return studentRepository.save(student);
    }

    /**
     * Find student by ID
     * Time Complexity: O(log n) due to database indexing
     */
    public Optional<Student> findByStudentId(String studentId) {
        return studentRepository.findByStudentId(studentId);
    }

    /**
     * Get all students ordered by average grade descending
     * Activity 1 Requirement: Support ordering by average grade
     * Time Complexity: O(n log n) for database sorting
     */
    public List<Student> getAllStudentsOrderedByGrade() {
        return studentRepository.findAllOrderByAverageGradeDesc();
    }

    /**
     * Add grade to student for a specific course
     * Time Complexity: O(1) for database insert
     */
    public Student addGrade(String studentId, String courseId, double gradeValue) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Grade grade = new Grade(student, course, gradeValue);
        gradeRepository.save(grade);

        // Update student's average grade
        updateStudentAverageGrade(student);
        return studentRepository.save(student);
    }

    /**
     * Update student's grades (replace all)
     * Time Complexity: O(n) for database operations
     */
    public Student updateGrades(String studentId, List<Double> newGrades) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        // Delete existing grades
        gradeRepository.deleteByStudentId(studentId);

        // Add new grades
        for (Double gradeValue : newGrades) {
            Grade grade = new Grade(student, gradeValue);
            gradeRepository.save(grade);
        }

        // Update student's average grade
        updateStudentAverageGrade(student);
        return studentRepository.save(student);
    }

    /**
     * Update student's average grade
     * Time Complexity: O(1) for database aggregation
     */
    private void updateStudentAverageGrade(Student student) {
        Double average = gradeRepository.findAverageGradeByStudentId(student.getStudentId());
        student.setAverageGrade(average != null ? average : 0.0);
    }

    /**
     * Delete student and all associated grades
     * Time Complexity: O(n) for cascade delete
     */
    public void deleteStudent(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        studentRepository.delete(student);
        // Grades will be deleted automatically due to cascade configuration
    }

    /**
     * Get student's grades
     * Time Complexity: O(n) for database query
     */
    public List<Grade> getStudentGrades(String studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    /**
     * Get student grades mapped by course
     * Time Complexity: O(n*m) where n is courses, m is grades per course
     */
    public Map<String, List<Double>> getStudentGradesByCourse(String studentId) {
        System.out.println("=== getStudentGradesByCourse called for student: " + studentId);

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        System.out.println("Found student: " + student.getStudentId());

        Map<String, List<Double>> gradesByCourse = new HashMap<>();

        // Get all courses this student is enrolled in
        List<Course> courses = courseRepository.findCoursesByStudentId(studentId);
        System.out.println("Found " + courses.size() + " courses for student");

        for (Course course : courses) {
            System.out.println("Processing course: " + course.getCourseId());
            List<Double> courseGrades = gradeRepository.findGradesByStudentIdAndCourseId(studentId, course.getId())
                    .stream()
                    .map(Grade::getGradeValue)
                    .collect(Collectors.toList());

            System.out.println("Found " + courseGrades.size() + " grades for course " + course.getCourseId());

            if (!courseGrades.isEmpty()) {
                gradesByCourse.put(course.getCourseId(), courseGrades);
            }
        }

        System.out.println("Returning gradesByCourse with " + gradesByCourse.size() + " entries");
        return gradesByCourse;
    }

    /**
     * Get student statistics
     * Time Complexity: O(1) for database aggregation
     */
    public StudentStatistics getStudentStatistics(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        List<Grade> grades = gradeRepository.findByStudentId(studentId);
        Double averageFromDb = gradeRepository.findAverageGradeByStudentId(studentId);

        return new StudentStatistics(
                studentId,
                grades.size(),
                student.getAverageGrade(),
                averageFromDb,
                grades.stream().mapToDouble(Grade::getGradeValue).max().orElse(0.0),
                grades.stream().mapToDouble(Grade::getGradeValue).min().orElse(0.0)
        );
    }

    /**
     * Inner class for student statistics
     */
    public static class StudentStatistics {
        private final String studentId;
        private final int gradeCount;
        private final Double calculatedAverage;
        private final Double databaseAverage;
        private final double highestGrade;
        private final double lowestGrade;

        public StudentStatistics(String studentId, int gradeCount, Double calculatedAverage,
                               Double databaseAverage, double highestGrade, double lowestGrade) {
            this.studentId = studentId;
            this.gradeCount = gradeCount;
            this.calculatedAverage = calculatedAverage;
            this.databaseAverage = databaseAverage;
            this.highestGrade = highestGrade;
            this.lowestGrade = lowestGrade;
        }

        // Getters
        public String getStudentId() { return studentId; }
        public int getGradeCount() { return gradeCount; }
        public Double getCalculatedAverage() { return calculatedAverage; }
        public Double getDatabaseAverage() { return databaseAverage; }
        public double getHighestGrade() { return highestGrade; }
        public double getLowestGrade() { return lowestGrade; }
    }
}
