package com.graduation.repository;

import com.graduation.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Activity 1: Student Repository
 * Data access layer for Student entity
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    /**
     * Find student by student ID
     * Time Complexity: O(log n) due to database indexing
     */
    Optional<Student> findByStudentId(String studentId);

    /**
     * Check if student exists by student ID
     * Time Complexity: O(log n)
     */
    boolean existsByStudentId(String studentId);

    /**
     * Find all students ordered by average grade descending
     * Activity 1 Requirement: Support ordering by average grade
     * Time Complexity: O(n log n) for sorting
     */
    @Query("SELECT s FROM Student s ORDER BY s.averageGrade DESC")
    List<Student> findAllOrderByAverageGradeDesc();

    /**
     * Find students with average grade above threshold
     * Time Complexity: O(n)
     */
    @Query("SELECT s FROM Student s WHERE s.averageGrade >= :minGrade ORDER BY s.averageGrade DESC")
    List<Student> findByAverageGradeGreaterThanEqual(@Param("minGrade") Double minGrade);

    /**
     * Find students enrolled in a specific course
     * Time Complexity: O(n)
     */
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.courseId = :courseId ORDER BY s.averageGrade DESC")
    List<Student> findByCourseIdOrderByAverageGradeDesc(@Param("courseId") String courseId);

    /**
     * Count students in a course
     * Time Complexity: O(1) with proper indexing
     */
    @Query("SELECT COUNT(s) FROM Student s JOIN s.courses c WHERE c.courseId = :courseId")
    long countByCourseId(@Param("courseId") String courseId);
}
