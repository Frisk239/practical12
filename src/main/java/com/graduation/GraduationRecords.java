package com.graduation;

import com.graduation.entity.Course;
import com.graduation.entity.Student;
import com.graduation.entity.Grade;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Activity 1: Graduation Records Management System
 * Interactive console application demonstrating course and student management
 * Bilingual interface: English primary, Chinese secondary
 */
public class GraduationRecords {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Student> students = new ArrayList<>();
    private static final List<Course> courses = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("=========================================");
        System.out.println("    Graduation Records Management System");
        System.out.println("    毕业记录管理系统 - Console Application");
        System.out.println("=========================================");
        System.out.println();

        // Main menu loop
        boolean running = true;
        while (running) {
            showMainMenu();
            int choice = getIntInput("Please select (0-9) (请选择): ");

            switch (choice) {
                case 1:
                    createNewStudent();
                    break;
                case 2:
                    createNewCourse();
                    break;
                case 3:
                    addStudentToCourse();
                    break;
                case 4:
                    updateStudentGrades();
                    break;
                case 5:
                    removeStudentFromCourse();
                    break;
                case 6:
                    viewCourseStudentList();
                    break;
                case 7:
                    viewAllStudents();
                    break;
                case 8:
                    viewAllCourses();
                    break;
                case 9:
                    viewStudentGrades();
                    break;
                case 0:
                    running = false;
                    System.out.println("Thank you for using the system! (感谢使用本系统！)");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again. (无效选择，请重试。)");
                    System.out.println();
            }
        }

        scanner.close();
    }

    /**
     * Display main menu
     */
    private static void showMainMenu() {
        System.out.println("=========================================");
        System.out.println("    Graduation Records Management System");
        System.out.println("    毕业记录管理系统 - Console Application");
        System.out.println("=========================================");
        System.out.println("Please select an operation (请选择操作):");
        System.out.println("1. Create New Student (创建新学生)");
        System.out.println("2. Create New Course (创建新课程)");
        System.out.println("3. Add Student to Course (将学生添加到课程)");
        System.out.println("4. Update Student Grades (更新学生成绩)");
        System.out.println("5. Remove Student from Course (从课程移除学生)");
        System.out.println("6. View Course Student List (查看课程学生列表)");
        System.out.println("7. View All Students (查看所有学生)");
        System.out.println("8. View All Courses (查看所有课程)");
        System.out.println("9. View Student Grades (查看学生成绩)");
        System.out.println("0. Exit Program (退出程序)");
        System.out.println("=========================================");
    }

    /**
     * Show submenu options (available in all submenus)
     */
    private static void showSubMenuOptions() {
        System.out.println("h. Back to Main Menu (返回主菜单)");
        System.out.println("0. Exit Program (退出程序)");
    }

    /**
     * Get integer input with validation
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number. (请输入有效数字。)");
            }
        }
    }

    /**
     * Get string input
     */
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    /**
     * Get grades input (comma-separated)
     */
    private static List<Double> getGradesInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                return new ArrayList<>();
            }

            try {
                String[] parts = input.split(",");
                List<Double> grades = new ArrayList<>();
                for (String part : parts) {
                    double grade = Double.parseDouble(part.trim());
                    if (grade < 0.0 || grade > 100.0) {
                        throw new IllegalArgumentException("Grade must be between 0 and 100 (成绩必须在0-100之间)");
                    }
                    grades.add(grade);
                }
                return grades;
            } catch (Exception e) {
                System.out.println("Invalid grades format. Please use comma-separated numbers (e.g., 85.5,92.0) (无效成绩格式，请使用逗号分隔的数字，如：85.5,92.0)");
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    /**
     * 1. Create New Student
     */
    private static void createNewStudent() {
        System.out.println("\n=== Create New Student (创建新学生) ===");
        showSubMenuOptions();

        String studentId = getStringInput("Enter Student ID (输入学生ID): ");
        if (studentId.isEmpty()) {
            System.out.println("Student ID cannot be empty. (学生ID不能为空。)");
            return;
        }

        // Check for menu commands
        if (studentId.equals("h")) {
            return; // Back to main menu
        } else if (studentId.equals("0")) {
            System.exit(0); // Exit program
        }

        // Check if student already exists
        for (Student student : students) {
            if (student.getStudentId().equals(studentId)) {
                System.out.println("Student with ID '" + studentId + "' already exists. (学生ID '" + studentId + "' 已存在。)");
                return;
            }
        }

        // Create student with no initial grades (pure student creation)
        Student student = new Student(studentId);
        students.add(student);

        System.out.println("Student created successfully! (学生创建成功！)");
        System.out.println("Student ID: " + studentId + " (学生ID: " + studentId + ")");
        System.out.println("Note: Grades will be added when student is enrolled in courses. (注意：成绩将在学生选课后添加。)");
        System.out.println();
    }

    /**
     * 2. Create New Course
     */
    private static void createNewCourse() {
        System.out.println("\n=== Create New Course (创建新课程) ===");
        showSubMenuOptions();

        String courseId = getStringInput("Enter Course ID (输入课程ID): ");
        if (courseId.isEmpty()) {
            System.out.println("Course ID cannot be empty. (课程ID不能为空。)");
            return;
        }

        // Check for menu commands
        if (courseId.equals("h")) {
            return; // Back to main menu
        } else if (courseId.equals("0")) {
            System.exit(0); // Exit program
        }

        String academicYear = getStringInput("Enter Academic Year (输入学年): ");
        if (academicYear.isEmpty()) {
            System.out.println("Academic Year cannot be empty. (学年不能为空。)");
            return;
        }

        // Check for menu commands
        if (academicYear.equals("h")) {
            return; // Back to main menu
        } else if (academicYear.equals("0")) {
            System.exit(0); // Exit program
        }

        // Check if course already exists
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                System.out.println("Course with ID '" + courseId + "' already exists. (课程ID '" + courseId + "' 已存在。)");
                return;
            }
        }

        Course course = new Course(courseId, academicYear);
        courses.add(course);

        System.out.println("Course created successfully! (课程创建成功！)");
        System.out.println(course.toString());
        System.out.println();
    }

    /**
     * 3. Add Student to Course
     */
    private static void addStudentToCourse() {
        System.out.println("\n=== Add Student to Course (将学生添加到课程) ===");
        showSubMenuOptions();

        if (courses.isEmpty()) {
            System.out.println("No courses available. Please create a course first. (没有可用课程，请先创建课程。)");
            return;
        }

        if (students.isEmpty()) {
            System.out.println("No students available. Please create a student first. (没有可用学生，请先创建学生。)");
            return;
        }

        // Display available courses
        System.out.println("Available Courses (可用课程):");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.printf("%d. %s (%d students) (%s - %d名学生)%n",
                i + 1, course.getCourseId(), course.getStudentCount(),
                course.getCourseId(), course.getStudentCount());
        }

        int courseChoice = getIntInput("Select course number (选择课程编号): ");
        if (courseChoice == 0) {
            System.exit(0); // Exit program
        } else if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection. (无效的课程选择。)");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);

        // Display available students
        System.out.println("Available Students (可用学生):");
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.printf("%d. %s (%.2f)%n",
                i + 1, student.getStudentId(), student.getAverageGrade());
        }

        int studentChoice = getIntInput("Select student number (选择学生编号): ");
        if (studentChoice == 0) {
            System.exit(0); // Exit program
        } else if (studentChoice < 1 || studentChoice > students.size()) {
            System.out.println("Invalid student selection. (无效的学生选择。)");
            return;
        }

        Student selectedStudent = students.get(studentChoice - 1);

        // Check if student is already in the course
        if (selectedCourse.isStudentEnrolled(selectedStudent.getStudentId())) {
            System.out.println("Student is already enrolled in this course. (学生已经在这门课程中。)");
            return;
        }

        selectedCourse.addStudent(selectedStudent);
        System.out.println("Student added to course successfully! (学生成功添加到课程！)");
        System.out.println();
    }

    /**
     * 4. Update Student Grades
     */
    private static void updateStudentGrades() {
        System.out.println("\n=== Update Student Grades (更新学生成绩) ===");
        showSubMenuOptions();

        if (courses.isEmpty()) {
            System.out.println("No courses available. (没有可用课程。)");
            return;
        }

        // Display available courses
        System.out.println("Available Courses (可用课程):");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.printf("%d. %s (%d students)%n",
                i + 1, course.getCourseId(), course.getStudentCount());
        }

        int courseChoice = getIntInput("Select course number (选择课程编号): ");
        if (courseChoice == 0) {
            System.exit(0); // Exit program
        } else if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection. (无效的课程选择。)");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);

        if (selectedCourse.getStudentCount() == 0) {
            System.out.println("No students in this course. (这门课程没有学生。)");
            return;
        }

        // Display students in the course
        System.out.println("Students in Course (课程中的学生):");
        List<Student> courseStudents = selectedCourse.getStudents();
        for (int i = 0; i < courseStudents.size(); i++) {
            Student student = courseStudents.get(i);
            System.out.printf("%d. %s (%.2f)%n",
                i + 1, student.getStudentId(), student.getAverageGrade());
        }

        int studentChoice = getIntInput("Select student number (选择学生编号): ");
        if (studentChoice == 0) {
            System.exit(0); // Exit program
        } else if (studentChoice < 1 || studentChoice > courseStudents.size()) {
            System.out.println("Invalid student selection. (无效的学生选择。)");
            return;
        }

        Student selectedStudent = courseStudents.get(studentChoice - 1);

        System.out.println("Current grades for " + selectedStudent.getStudentId() + ":");
        List<Grade> currentGrades = selectedStudent.getGrades();
        if (currentGrades.isEmpty()) {
            System.out.println("No grades recorded. (没有记录成绩。)");
        } else {
            for (int i = 0; i < currentGrades.size(); i++) {
                System.out.printf("Grade %d: %.1f%n", i + 1, currentGrades.get(i).getGradeValue());
            }
        }

        List<Double> newGrades = getGradesInput("Enter new grades (comma-separated) or empty to keep current (输入新成绩，用逗号分隔，或留空保持当前): ");

        if (!newGrades.isEmpty()) {
            // Clear existing grades and add new ones
            selectedStudent.getGrades().clear();
            for (Double grade : newGrades) {
                selectedStudent.addGrade(new Grade(grade));
            }

            // Update the student in the course list and resort
            selectedCourse.updateStudentGrades(selectedStudent);
            System.out.println("Student grades updated successfully! (学生成绩更新成功！)");
        } else {
            System.out.println("No changes made. (未做任何更改。)");
        }
        System.out.println();
    }

    /**
     * 5. Remove Student from Course
     */
    private static void removeStudentFromCourse() {
        System.out.println("\n=== Remove Student from Course (从课程移除学生) ===");
        showSubMenuOptions();

        if (courses.isEmpty()) {
            System.out.println("No courses available. (没有可用课程。)");
            return;
        }

        // Display available courses
        System.out.println("Available Courses (可用课程):");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.printf("%d. %s (%d students)%n",
                i + 1, course.getCourseId(), course.getStudentCount());
        }

        int courseChoice = getIntInput("Select course number (选择课程编号): ");
        if (courseChoice == 0) {
            System.exit(0); // Exit program
        } else if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection. (无效的课程选择。)");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);

        if (selectedCourse.getStudentCount() == 0) {
            System.out.println("No students in this course. (这门课程没有学生。)");
            return;
        }

        // Display students in the course
        System.out.println("Students in Course (课程中的学生):");
        List<Student> courseStudents = selectedCourse.getStudents();
        for (int i = 0; i < courseStudents.size(); i++) {
            Student student = courseStudents.get(i);
            System.out.printf("%d. %s (%.2f)%n",
                i + 1, student.getStudentId(), student.getAverageGrade());
        }

        int studentChoice = getIntInput("Select student number to remove (选择要移除的学生编号): ");
        if (studentChoice == 0) {
            System.exit(0); // Exit program
        } else if (studentChoice < 1 || studentChoice > courseStudents.size()) {
            System.out.println("Invalid student selection. (无效的学生选择。)");
            return;
        }

        Student selectedStudent = courseStudents.get(studentChoice - 1);

        boolean removed = selectedCourse.removeStudentById(selectedStudent.getStudentId());
        if (removed) {
            System.out.println("Student removed from course successfully! (学生成功从课程移除！)");
        } else {
            System.out.println("Failed to remove student. (移除学生失败。)");
        }
        System.out.println();
    }

    /**
     * 6. View Course Student List
     */
    private static void viewCourseStudentList() {
        System.out.println("\n=== View Course Student List (查看课程学生列表) ===");
        showSubMenuOptions();

        if (courses.isEmpty()) {
            System.out.println("No courses available. (没有可用课程。)");
            return;
        }

        // Display available courses
        System.out.println("Available Courses (可用课程):");
        for (int i = 0; i < courses.size(); i++) {
            Course course = courses.get(i);
            System.out.printf("%d. %s (%d students)%n",
                i + 1, course.getCourseId(), course.getStudentCount());
        }

        int courseChoice = getIntInput("Select course number (选择课程编号): ");
        if (courseChoice == 0) {
            System.exit(0); // Exit program
        } else if (courseChoice < 1 || courseChoice > courses.size()) {
            System.out.println("Invalid course selection. (无效的课程选择。)");
            return;
        }

        Course selectedCourse = courses.get(courseChoice - 1);
        selectedCourse.printStudentList();
        System.out.println();
    }

    /**
     * 7. View All Students
     */
    private static void viewAllStudents() {
        System.out.println("\n=== View All Students (查看所有学生) ===");

        if (students.isEmpty()) {
            System.out.println("No students in the system. (系统中没有学生。)");
        } else {
            System.out.println("All Students (所有学生):");
            System.out.println("=========================================");
            for (int i = 0; i < students.size(); i++) {
                Student student = students.get(i);
                System.out.printf("%d. %s%n", i + 1, student.toString());
            }
            System.out.println("=========================================");
            System.out.println("Total students: " + students.size() + " (总学生数: " + students.size() + ")");
        }
        System.out.println();
    }

    /**
     * 8. View All Courses
     */
    private static void viewAllCourses() {
        System.out.println("\n=== View All Courses (查看所有课程) ===");

        if (courses.isEmpty()) {
            System.out.println("No courses in the system. (系统中没有课程。)");
        } else {
            System.out.println("All Courses (所有课程):");
            System.out.println("=========================================");
            for (int i = 0; i < courses.size(); i++) {
                Course course = courses.get(i);
                System.out.printf("%d. %s%n", i + 1, course.toString());
            }
            System.out.println("=========================================");
            System.out.println("Total courses: " + courses.size() + " (总课程数: " + courses.size() + ")");
        }
        System.out.println();
    }

    /**
     * 9. View Student Grades
     */
    private static void viewStudentGrades() {
        System.out.println("\n=== View Student Grades (查看学生成绩) ===");
        showSubMenuOptions();

        if (students.isEmpty()) {
            System.out.println("No students in the system. (系统中没有学生。)");
            return;
        }

        // Display available students
        System.out.println("Available Students (可用学生):");
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            System.out.printf("%d. %s (%.2f)%n",
                i + 1, student.getStudentId(), student.getAverageGrade());
        }

        int studentChoice = getIntInput("Select student number (选择学生编号): ");
        if (studentChoice == 0) {
            System.exit(0); // Exit program
        } else if (studentChoice < 1 || studentChoice > students.size()) {
            System.out.println("Invalid student selection. (无效的学生选择。)");
            return;
        }

        Student selectedStudent = students.get(studentChoice - 1);

        System.out.println("\n=== Grades for Student: " + selectedStudent.getStudentId() + " (学生成绩: " + selectedStudent.getStudentId() + ") ===");
        System.out.println("Average Grade: " + String.format("%.2f", selectedStudent.getAverageGrade()) + " (平均分: " + String.format("%.2f", selectedStudent.getAverageGrade()) + ")");

        // Find all courses this student is enrolled in and show grades
        boolean hasGrades = false;
        for (Course course : courses) {
            if (course.isStudentEnrolled(selectedStudent.getStudentId())) {
                Student courseStudent = course.getStudentById(selectedStudent.getStudentId());
                if (courseStudent != null && !courseStudent.getGrades().isEmpty()) {
                    System.out.println(course.getCourseId() + ": " + String.format("%.1f", courseStudent.getAverageGrade()));
                    hasGrades = true;
                }
            }
        }

        if (!hasGrades) {
            System.out.println("No grades recorded for this student. (该学生没有成绩记录。)");
        }

        System.out.println("=====================================");
        System.out.println();
    }
}
