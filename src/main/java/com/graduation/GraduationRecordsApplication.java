package com.graduation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Activity 1: Graduation Records Management System Main Application
 * Spring Boot application entry point
 */
@SpringBootApplication
public class GraduationRecordsApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraduationRecordsApplication.class, args);
        System.out.println("=== Graduation Records Management System Started ===");
        System.out.println("Web Interface: http://localhost:8080");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("API Base URL: http://localhost:8080/api");
        System.out.println("Health Check: http://localhost:8080/api/health");
        System.out.println("==================================================");
    }
}
