-- Initial data for Graduation Records Management System
-- Activity 1: Sample data for testing

-- Insert sample courses (let AUTO_INCREMENT handle IDs)
INSERT INTO course (course_id, academic_year) VALUES ('CS101', '2024-2025');
INSERT INTO course (course_id, academic_year) VALUES ('CS102', '2024-2025');
INSERT INTO course (course_id, academic_year) VALUES ('MATH101', '2024-2025');

-- Insert sample students (let AUTO_INCREMENT handle IDs)
INSERT INTO student (student_id, average_grade) VALUES ('STU001', 85.5);
INSERT INTO student (student_id, average_grade) VALUES ('STU002', 92.0);
INSERT INTO student (student_id, average_grade) VALUES ('STU003', 78.3);
INSERT INTO student (student_id, average_grade) VALUES ('STU004', 88.7);
INSERT INTO student (student_id, average_grade) VALUES ('STU005', 91.2);

-- Insert sample grades for students (now with course association)
-- STU001 (ID=1): CS101=85.0, CS102=86.0
INSERT INTO grade (student_id, course_id, grade_value) VALUES (1, 1, 85.0);
INSERT INTO grade (student_id, course_id, grade_value) VALUES (1, 2, 86.0);

-- STU002 (ID=2): CS101=90.0, CS102=94.0
INSERT INTO grade (student_id, course_id, grade_value) VALUES (2, 1, 90.0);
INSERT INTO grade (student_id, course_id, grade_value) VALUES (2, 2, 94.0);

-- STU003 (ID=3): CS101=75.0, MATH101=82.0
INSERT INTO grade (student_id, course_id, grade_value) VALUES (3, 1, 75.0);
INSERT INTO grade (student_id, course_id, grade_value) VALUES (3, 3, 82.0);

-- STU004 (ID=4): CS102=88.0, MATH101=89.0
INSERT INTO grade (student_id, course_id, grade_value) VALUES (4, 2, 88.0);
INSERT INTO grade (student_id, course_id, grade_value) VALUES (4, 3, 89.0);

-- STU005 (ID=5): CS102=92.0, MATH101=90.0
INSERT INTO grade (student_id, course_id, grade_value) VALUES (5, 2, 92.0);
INSERT INTO grade (student_id, course_id, grade_value) VALUES (5, 3, 90.0);

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
