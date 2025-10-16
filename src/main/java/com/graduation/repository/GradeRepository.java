package com.graduation.repository;

import com.graduation.entity.Grade;
import com.graduation.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Activity 1: Grade Repository
 * Data access layer for Grade entity
 */
@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * Find all grades for a student
     * Time Complexity: O(n)
     */
    List<Grade> findByStudent(Student student);

    /**
     * Find all grades for a student by student ID
     * Time Complexity: O(n)
     */
    @Query("SELECT g FROM Grade g WHERE g.student.studentId = :studentId ORDER BY g.createdAt DESC")
    List<Grade> findByStudentId(@Param("studentId") String studentId);

    /**
     * Find grades within a range
     * Time Complexity: O(n)
     */
    List<Grade> findByGradeValueBetween(double minGrade, double maxGrade);

    /**
     * Calculate average grade for a student
     * Activity 1 Requirement: Support average grade calculations
     * Time Complexity: O(1) with database aggregation
     */
    @Query("SELECT AVG(g.gradeValue) FROM Grade g WHERE g.student.studentId = :studentId")
    Double findAverageGradeByStudentId(@Param("studentId") String studentId);

    /**
     * Count grades for a student
     * Time Complexity: O(1)
     */
    long countByStudent(Student student);

    /**
     * Count grades for a student by student ID
     * Time Complexity: O(1)
     */
    @Query("SELECT COUNT(g) FROM Grade g WHERE g.student.studentId = :studentId")
    long countByStudentId(@Param("studentId") String studentId);

    /**
     * Delete all grades for a student
     * Time Complexity: O(n)
     */
    void deleteByStudent(Student student);

    /**
     * Delete all grades for a student by student ID
     * Time Complexity: O(n)
     */
    @Query("DELETE FROM Grade g WHERE g.student.studentId = :studentId")
    void deleteByStudentId(@Param("studentId") String studentId);
}
