package com.graduation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Activity 1: Graduation Records Management System Main Application
 * Spring Boot application entry point with smart database initialization
 */
@SpringBootApplication
public class GraduationRecordsApplication implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(GraduationRecordsApplication.class, args);
        System.out.println("=== Graduation Records Management System Started ===");
        System.out.println("Web Interface: http://localhost:8080");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("API Base URL: http://localhost:8080/api");
        System.out.println("Health Check: http://localhost:8080/api/health");
        System.out.println("==================================================");
    }

    @Override
    public void run(String... args) throws Exception {
        initializeDatabase();
    }

    /**
     * Smart database initialization
     * Only creates tables and inserts data if they don't exist
     */
    private void initializeDatabase() {
        try {
            // Check if tables exist
            boolean tablesExist = checkIfTablesExist();

            if (!tablesExist) {
                System.out.println("=== Initializing Database Schema ===");
                // Create tables
                ResourceDatabasePopulator schemaPopulator = new ResourceDatabasePopulator();
                schemaPopulator.addScript(new ClassPathResource("schema.sql"));
                schemaPopulator.execute(dataSource);
                System.out.println("Database schema created successfully");
            }

            // Check if data exists
            boolean dataExists = checkIfDataExists();

            if (!dataExists) {
                System.out.println("=== Initializing Sample Data ===");
                // Insert sample data
                ResourceDatabasePopulator dataPopulator = new ResourceDatabasePopulator();
                dataPopulator.addScript(new ClassPathResource("data.sql"));
                dataPopulator.execute(dataSource);
                System.out.println("Sample data inserted successfully");
            } else {
                System.out.println("=== Database Already Initialized ===");
                System.out.println("Tables and data already exist, skipping initialization");
            }

        } catch (Exception e) {
            System.err.println("Database initialization failed: " + e.getMessage());
            // Don't fail the application if initialization fails
        }
    }

    /**
     * Check if required tables exist
     */
    private boolean checkIfTablesExist() {
        try {
            // Check for student table
            jdbcTemplate.queryForObject("SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'STUDENT'", Integer.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check if sample data exists
     */
    private boolean checkIfDataExists() {
        try {
            // Check if students exist
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM student", Integer.class);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }
}
