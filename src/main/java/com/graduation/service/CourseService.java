package com.graduation.service;

import com.graduation.entity.Course;
import com.graduation.entity.Student;
import com.graduation.repository.CourseRepository;
import com.graduation.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Activity 1: Course Service
 * Business logic layer for course operations with LinkedList sorting
 * Activity 2: Complexity Analysis included in comments
 */
@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private com.graduation.repository.GradeRepository gradeRepository;

    /**
     * Create a new course
     * Time Complexity: O(1) for database insert
     */
    public Course createCourse(String courseId, String academicYear) {
        if (courseRepository.existsByCourseId(courseId)) {
            throw new IllegalArgumentException("Course with ID " + courseId + " already exists");
        }

        Course course = new Course(courseId, academicYear);
        return courseRepository.save(course);
    }

    /**
     * Find course by ID with students loaded
     * Time Complexity: O(n) for loading students
     */
    public Optional<Course> findByCourseIdWithStudents(String courseId) {
        return courseRepository.findByCourseIdWithStudents(courseId);
    }

    /**
     * Add student to course and maintain descending order by average grade
     * Activity 1 Requirement: Maintain list in descending order based on student average grade
     * Time Complexity: O(n log n) due to sorting after insertion
     */
    public Course addStudentToCourse(String courseId, String studentId) {
        Course course = courseRepository.findByCourseIdWithStudents(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        course.addStudent(student);
        return courseRepository.save(course);
    }

    /**
     * Remove student from course
     * Activity 1 Requirement: Allow students to be removed from the student list
     * Time Complexity: O(n) for removal and printing
     */
    public Course removeStudentFromCourse(String courseId, String studentId) {
        Course course = courseRepository.findByCourseIdWithStudents(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        course.removeStudentById(studentId);
        return courseRepository.save(course);
    }

    /**
     * Update student grades for a specific course and resort the course student list
     * Activity 1 Requirement: Allow grades to be updated and reflect in ordering
     * Time Complexity: O(n log n) due to sorting
     */
    public Course updateStudentGrades(String courseId, String studentId, List<Double> newGrades) {
        Course course = courseRepository.findByCourseIdWithStudents(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + studentId));

        // Delete existing grades for this course
        student.getGrades().removeIf(grade -> grade.getCourse().getId().equals(course.getId()));

        // Add new grades for this course
        for (Double gradeValue : newGrades) {
            com.graduation.entity.Grade grade = new com.graduation.entity.Grade(student, course, gradeValue);
            student.getGrades().add(grade);
        }

        // Update student's average grade after grade changes
        updateStudentAverageGrade(student);
        studentRepository.save(student);
        course.updateStudentGrades(student);

        return courseRepository.save(course);
    }

    /**
     * Get course students in descending order by average grade
     * Activity 1 Requirement: Support ordering by average grade
     * Time Complexity: O(n log n) for sorting
     */
    public List<Student> getCourseStudentsOrderedByGrade(String courseId) {
        Course course = courseRepository.findByCourseIdWithStudents(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        List<Student> students = course.getStudents();
        // Sort in descending order (highest grades first)
        students.sort((s1, s2) -> s2.compareTo(s1));
        return students;
    }

    /**
     * Print course student list (for debugging/logging)
     * Activity 1 Requirement: Print student list after every change
     * Time Complexity: O(n)
     */
    public void printCourseStudentList(String courseId) {
        Course course = courseRepository.findByCourseIdWithStudents(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        course.printStudentList();
    }

    /**
     * Get course statistics
     * Time Complexity: O(n) for calculations
     */
    public CourseStatistics getCourseStatistics(String courseId) {
        Course course = courseRepository.findByCourseIdWithStudents(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        List<Student> students = course.getStudents();
        int studentCount = students.size();

        if (studentCount == 0) {
            return new CourseStatistics(courseId, course.getAcademicYear(), 0, 0.0, 0.0, 0.0, 0.0);
        }

        double totalAverage = students.stream()
                .mapToDouble(s -> s.getAverageGrade() != null ? s.getAverageGrade() : 0.0)
                .sum();
        double classAverage = totalAverage / studentCount;

        double highestAverage = students.stream()
                .mapToDouble(s -> s.getAverageGrade() != null ? s.getAverageGrade() : 0.0)
                .max()
                .orElse(0.0);

        double lowestAverage = students.stream()
                .mapToDouble(s -> s.getAverageGrade() != null ? s.getAverageGrade() : 0.0)
                .min()
                .orElse(0.0);

        return new CourseStatistics(courseId, course.getAcademicYear(), studentCount,
                                  classAverage, highestAverage, lowestAverage, totalAverage);
    }

    /**
     * Get all courses with their student counts
     * Time Complexity: O(n) for database query
     */
    public List<CourseSummary> getAllCoursesSummary() {
        List<Object[]> results = courseRepository.findAllWithStudentCounts();
        return results.stream()
                .map(row -> new CourseSummary(
                        (Course) row[0],
                        ((Long) row[1]).intValue()
                ))
                .toList();
    }

    /**
     * Inner class for course statistics
     */
    public static class CourseStatistics {
        private final String courseId;
        private final String academicYear;
        private final int studentCount;
        private final double classAverage;
        private final double highestAverage;
        private final double lowestAverage;
        private final double totalAverageSum;

        public CourseStatistics(String courseId, String academicYear, int studentCount,
                              double classAverage, double highestAverage, double lowestAverage,
                              double totalAverageSum) {
            this.courseId = courseId;
            this.academicYear = academicYear;
            this.studentCount = studentCount;
            this.classAverage = classAverage;
            this.highestAverage = highestAverage;
            this.lowestAverage = lowestAverage;
            this.totalAverageSum = totalAverageSum;
        }

        // Getters
        public String getCourseId() { return courseId; }
        public String getAcademicYear() { return academicYear; }
        public int getStudentCount() { return studentCount; }
        public double getClassAverage() { return classAverage; }
        public double getHighestAverage() { return highestAverage; }
        public double getLowestAverage() { return lowestAverage; }
        public double getTotalAverageSum() { return totalAverageSum; }
    }

    /**
     * Delete a course and all associated data
     * Time Complexity: O(n) for database operations
     */
    public void deleteCourse(String courseId) {
        Course course = courseRepository.findByCourseId(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId));

        // Get all students enrolled in this course to update their average grades later
        List<Student> enrolledStudents = new ArrayList<>(course.getStudents());

        // Delete the course (this will cascade delete grades due to foreign key constraints)
        // But we need to manually handle course_student relationships
        courseRepository.delete(course);

        // Update average grades for all students who were enrolled in this course
        for (Student student : enrolledStudents) {
            updateStudentAverageGrade(student);
            studentRepository.save(student);
        }
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
     * Inner class for course summary
     */
    public static class CourseSummary {
        private final Course course;
        private final int studentCount;

        public CourseSummary(Course course, int studentCount) {
            this.course = course;
            this.studentCount = studentCount;
        }

        public Course getCourse() { return course; }
        public int getStudentCount() { return studentCount; }
    }
}
