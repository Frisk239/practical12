-- Database schema for Graduation Records Management System
-- Activity 1: Core graduation records functionality

-- Student table
CREATE TABLE student (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id VARCHAR(50) NOT NULL UNIQUE,
    average_grade DOUBLE DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Course table
CREATE TABLE course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_id VARCHAR(50) NOT NULL UNIQUE,
    academic_year VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Grade table (one-to-many relationship with Student)
CREATE TABLE grade (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    grade_value DOUBLE NOT NULL CHECK (grade_value >= 0.0 AND grade_value <= 100.0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

-- Course-Student relationship table (many-to-many)
CREATE TABLE course_student (
    course_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (course_id, student_id),
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student(id) ON DELETE CASCADE
);

-- Indexes for performance
CREATE INDEX idx_student_student_id ON student(student_id);
CREATE INDEX idx_course_course_id ON course(course_id);
CREATE INDEX idx_grade_student_id ON grade(student_id);
CREATE INDEX idx_course_student_course_id ON course_student(course_id);
CREATE INDEX idx_course_student_student_id ON course_student(student_id);
