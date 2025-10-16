package com.graduation.controller;

import com.graduation.entity.Course;
import com.graduation.entity.Student;
import com.graduation.service.CourseService;
import com.graduation.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Activity 1: Graduation Records REST Controller
 * Provides Web API for graduation records management
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow frontend access
public class GraduationController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    /**
     * Get all students ordered by average grade descending
     * Activity 1 Requirement: Support ordering by average grade
     */
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudentsOrderedByGrade();
        return ResponseEntity.ok(students);
    }

    /**
     * Get student by ID
     */
    @GetMapping("/students/{studentId}")
    public ResponseEntity<Student> getStudent(@PathVariable String studentId) {
        return studentService.findByStudentId(studentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Create new student
     */
    @PostMapping("/students")
    public ResponseEntity<Student> createStudent(@RequestBody Map<String, String> request) {
        String studentId = request.get("studentId");
        if (studentId == null || studentId.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Student student = studentService.createStudent(studentId.trim());
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Add grade to student
     * Activity 1 Requirement: Allow grades to be updated
     */
    @PostMapping("/students/{studentId}/grades")
    public ResponseEntity<Student> addGrade(@PathVariable String studentId,
                                          @RequestBody Map<String, Double> request) {
        Double gradeValue = request.get("gradeValue");
        if (gradeValue == null || gradeValue < 0.0 || gradeValue > 100.0) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Student student = studentService.addGrade(studentId, gradeValue);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update student's grades (replace all)
     */
    @PutMapping("/students/{studentId}/grades")
    public ResponseEntity<Student> updateGrades(@PathVariable String studentId,
                                              @RequestBody List<Double> grades) {
        if (grades == null || grades.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validate grade values
        for (Double grade : grades) {
            if (grade == null || grade < 0.0 || grade > 100.0) {
                return ResponseEntity.badRequest().build();
            }
        }

        try {
            Student student = studentService.updateGrades(studentId, grades);
            return ResponseEntity.ok(student);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete student
     */
    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
        try {
            studentService.deleteStudent(studentId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get student's grades
     */
    @GetMapping("/students/{studentId}/grades")
    public ResponseEntity<?> getStudentGrades(@PathVariable String studentId) {
        try {
            var gradesByCourse = studentService.getStudentGradesByCourse(studentId);
            var statistics = studentService.getStudentStatistics(studentId);

            Map<String, Object> response = new HashMap<>();
            response.put("gradesByCourse", gradesByCourse);
            response.put("statistics", statistics);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get course students ordered by grade
     * Activity 1 Requirement: Support ordering by average grade
     */
    @GetMapping("/courses/{courseId}/students")
    public ResponseEntity<List<Student>> getCourseStudents(@PathVariable String courseId) {
        try {
            List<Student> students = courseService.getCourseStudentsOrderedByGrade(courseId);
            return ResponseEntity.ok(students);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Add student to course
     * Activity 1 Requirement: Allow students to be inserted into the student list
     */
    @PostMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<Course> addStudentToCourse(@PathVariable String courseId,
                                                   @PathVariable String studentId) {
        try {
            Course course = courseService.addStudentToCourse(courseId, studentId);
            return ResponseEntity.ok(course);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Remove student from course
     * Activity 1 Requirement: Allow students to be removed from the student list
     */
    @DeleteMapping("/courses/{courseId}/students/{studentId}")
    public ResponseEntity<Course> removeStudentFromCourse(@PathVariable String courseId,
                                                        @PathVariable String studentId) {
        try {
            Course course = courseService.removeStudentFromCourse(courseId, studentId);
            return ResponseEntity.ok(course);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Update student grades in course context
     * Activity 1 Requirement: Allow grades to be updated and reflect in ordering
     */
    @PutMapping("/courses/{courseId}/students/{studentId}/grades")
    public ResponseEntity<Course> updateStudentGradesInCourse(@PathVariable String courseId,
                                                            @PathVariable String studentId,
                                                            @RequestBody List<Double> grades) {
        if (grades == null || grades.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validate grade values
        for (Double grade : grades) {
            if (grade == null || grade < 0.0 || grade > 100.0) {
                return ResponseEntity.badRequest().build();
            }
        }

        try {
            Course course = courseService.updateStudentGrades(courseId, studentId, grades);
            return ResponseEntity.ok(course);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Get course statistics
     */
    @GetMapping("/courses/{courseId}/statistics")
    public ResponseEntity<?> getCourseStatistics(@PathVariable String courseId) {
        try {
            var statistics = courseService.getCourseStatistics(courseId);
            return ResponseEntity.ok(statistics);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Create new course
     */
    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Map<String, String> request) {
        String courseId = request.get("courseId");
        String academicYear = request.get("academicYear");

        if (courseId == null || courseId.trim().isEmpty() ||
            academicYear == null || academicYear.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Course course = courseService.createCourse(courseId.trim(), academicYear.trim());
            return ResponseEntity.ok(course);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all courses summary
     */
    @GetMapping("/courses")
    public ResponseEntity<List<CourseService.CourseSummary>> getAllCourses() {
        List<CourseService.CourseSummary> courses = courseService.getAllCoursesSummary();
        return ResponseEntity.ok(courses);
    }

    /**
     * Print course student list (for debugging)
     * Activity 1 Requirement: Print student list after every change
     */
    @PostMapping("/courses/{courseId}/print")
    public ResponseEntity<Void> printCourseList(@PathVariable String courseId) {
        try {
            courseService.printCourseStudentList(courseId);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "Graduation Records Management System");
        response.put("version", "1.0.0");
        return ResponseEntity.ok(response);
    }
}
