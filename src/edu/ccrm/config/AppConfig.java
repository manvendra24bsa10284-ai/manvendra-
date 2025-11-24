package edu.ccrm.config;

import edu.ccrm.service.*;
import edu.ccrm.io.ImportExportService;
import edu.ccrm.util.BackupUtility;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class AppConfig {
    private static AppConfig instance;
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private GradingService gradingService;
    private ImportExportService importExportService;
    private BackupUtility backupUtility;
    private Properties config;
    private String dataDirectory;
    private String backupDirectory;
    
    private AppConfig() {
        this.config = new Properties();
        loadConfig();
        
        // Initialize services
        this.studentService = new StudentService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService(studentService, courseService);
        this.gradingService = new GradingService(enrollmentService);
        this.importExportService = new ImportExportService(studentService, courseService, enrollmentService);
        this.backupUtility = new BackupUtility(importExportService);
    }
    
    public static synchronized AppConfig getInstance() {
        if (instance == null) {
            instance = new AppConfig();
        }
        return instance;
    }
    
    // Load configuration
    private void loadConfig() {
        try {
            // Set default values
            dataDirectory = System.getProperty("user.home") + "/ccrm_data";
            backupDirectory = dataDirectory + "/backups";
            
            // Create directories if they don't exist
            Path dataDir = Paths.get(dataDirectory);
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
                System.out.println("Created data directory: " + dataDirectory);
            }
            
            Path backupDir = Paths.get(backupDirectory);
            if (!Files.exists(backupDir)) {
                Files.createDirectories(backupDir);
                System.out.println("Created backup directory: " + backupDirectory);
            }
            
            System.out.println("Configuration loaded successfully.");
            System.out.println("Data directory: " + dataDirectory);
            System.out.println("Backup directory: " + backupDirectory);
            
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }
    
    // Getters for services
    public StudentService getStudentService() { return studentService; }
    public CourseService getCourseService() { return courseService; }
    public EnrollmentService getEnrollmentService() { return enrollmentService; }
    public GradingService getGradingService() { return gradingService; }
    public ImportExportService getImportExportService() { return importExportService; }
    public BackupUtility getBackupUtility() { return backupUtility; }
    
    public String getDataDirectory() { return dataDirectory; }
    public String getBackupDirectory() { return backupDirectory; }
    
    public void printPlatformNote() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("PLATFORM NOTE: Campus Course & Records Manager (CCRM)");
        System.out.println("=".repeat(60));
        System.out.println("Built on Java SE (Standard Edition) Platform");
        System.out.println("- Java SE: Desktop and server applications (this application)");
        System.out.println("- Java ME: Micro Edition for embedded/mobile devices");
        System.out.println("- Java EE: Enterprise Edition for large-scale systems");
        System.out.println("\nCCRM demonstrates Java SE capabilities including:");
        System.out.println("• OOP Principles (Encapsulation, Inheritance, Polymorphism)");
        System.out.println("• Collections Framework and Stream API");
        System.out.println("• File I/O with NIO.2");
        System.out.println("• Exception Handling and Design Patterns");
        System.out.println("=".repeat(60));
    }
}