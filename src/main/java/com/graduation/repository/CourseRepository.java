package com.graduation.repository;

import com.graduation.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Activity 1: Course Repository
 * Data access layer for Course entity
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    /**
     * Find course by course ID
     * Time Complexity: O(log n) due to database indexing
     */
    Optional<Course> findByCourseId(String courseId);

    /**
     * Check if course exists by course ID
     * Time Complexity: O(log n)
     */
    boolean existsByCourseId(String courseId);

    /**
     * Find courses by academic year
     * Time Complexity: O(n)
     */
    List<Course> findByAcademicYear(String academicYear);

    /**
     * Find courses by academic year ordered by course ID
     * Time Complexity: O(n log n)
     */
    List<Course> findByAcademicYearOrderByCourseId(String academicYear);

    /**
     * Find course with students loaded (for LinkedList operations)
     * Activity 1 Requirement: Support student list operations
     * Time Complexity: O(n)
     */
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.courseId = :courseId")
    Optional<Course> findByCourseIdWithStudents(@Param("courseId") String courseId);

    /**
     * Find all courses with student counts
     * Time Complexity: O(n)
     */
    @Query("SELECT c, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c")
    List<Object[]> findAllWithStudentCounts();

    /**
     * Count total students across all courses
     * Time Complexity: O(1) with proper aggregation
     */
    @Query("SELECT COUNT(s) FROM Student s")
    long countAllStudents();

    /**
     * Find courses by student ID
     * Time Complexity: O(n)
     */
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.studentId = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") String studentId);
}
