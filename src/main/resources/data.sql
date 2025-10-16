-- Initial data for Graduation Records Management System
-- Activity 1: Sample data for testing

-- Insert sample courses
INSERT INTO course (course_id, academic_year) VALUES ('CS101', '2024-2025');
INSERT INTO course (course_id, academic_year) VALUES ('CS102', '2024-2025');
INSERT INTO course (course_id, academic_year) VALUES ('MATH101', '2024-2025');

-- Insert sample students
INSERT INTO student (student_id, average_grade) VALUES ('STU001', 85.5);
INSERT INTO student (student_id, average_grade) VALUES ('STU002', 92.0);
INSERT INTO student (student_id, average_grade) VALUES ('STU003', 78.3);
INSERT INTO student (student_id, average_grade) VALUES ('STU004', 88.7);
INSERT INTO student (student_id, average_grade) VALUES ('STU005', 91.2);

-- Insert sample grades for students
INSERT INTO grade (student_id, grade_value) VALUES (1, 85.0);
INSERT INTO grade (student_id, grade_value) VALUES (1, 86.0);
INSERT INTO grade (student_id, grade_value) VALUES (2, 90.0);
INSERT INTO grade (student_id, grade_value) VALUES (2, 94.0);
INSERT INTO grade (student_id, grade_value) VALUES (3, 75.0);
INSERT INTO grade (student_id, grade_value) VALUES (3, 82.0);
INSERT INTO grade (student_id, grade_value) VALUES (4, 88.0);
INSERT INTO grade (student_id, grade_value) VALUES (4, 89.0);
INSERT INTO grade (student_id, grade_value) VALUES (5, 92.0);
INSERT INTO grade (student_id, grade_value) VALUES (5, 90.0);

-- Enroll students in courses (this will trigger sorting by average grade)
INSERT INTO course_student (course_id, student_id) VALUES (1, 1);
INSERT INTO course_student (course_id, student_id) VALUES (1, 2);
INSERT INTO course_student (course_id, student_id) VALUES (1, 3);
INSERT INTO course_student (course_id, student_id) VALUES (2, 2);
INSERT INTO course_student (course_id, student_id) VALUES (2, 4);
INSERT INTO course_student (course_id, student_id) VALUES (2, 5);
INSERT INTO course_student (course_id, student_id) VALUES (3, 1);
INSERT INTO course_student (course_id, student_id) VALUES (3, 3);
INSERT INTO course_student (course_id, student_id) VALUES (3, 5);
