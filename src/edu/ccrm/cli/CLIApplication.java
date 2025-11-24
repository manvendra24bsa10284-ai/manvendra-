package edu.ccrm.cli;

import edu.ccrm.config.AppConfig;
import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import edu.ccrm.util.BackupUtility;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CLIApplication {
    private AppConfig appConfig;
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    private GradingService gradingService;
    private BackupUtility backupUtility;
    private Scanner scanner;
    
    public CLIApplication() {
        this.appConfig = AppConfig.getInstance();
        this.studentService = appConfig.getStudentService();
        this.courseService = appConfig.getCourseService();
        this.enrollmentService = appConfig.getEnrollmentService();
        this.gradingService = appConfig.getGradingService();
        this.backupUtility = appConfig.getBackupUtility();
        this.scanner = new Scanner(System.in);
    }
    
    public void run() {
        System.out.println("=== Campus Course & Records Manager (CCRM) ===");
        
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageCourses();
                case 3 -> manageEnrollment();
                case 4 -> manageGrades();
                case 5 -> importExportData();
                case 6 -> backupOperations();
                case 7 -> generateReports();
                case 0 -> {
                    System.out.println("Exiting CCRM. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        
        appConfig.printPlatformNote();
        scanner.close();
    }
    
    private void printMainMenu() {
        System.out.println("\n===== MAIN MENU =====");
        System.out.println("1. Manage Students");
        System.out.println("2. Manage Courses");
        System.out.println("3. Manage Enrollment");
        System.out.println("4. Manage Grades");
        System.out.println("5. Import/Export Data");
        System.out.println("6. Backup Operations");
        System.out.println("7. Generate Reports");
        System.out.println("0. Exit");
        System.out.println("=====================");
    }
    
    private void manageStudents() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n===== STUDENT MANAGEMENT =====");
            System.out.println("1. Add Student");
            System.out.println("2. List All Students");
            System.out.println("3. View Student Details");
            System.out.println("4. Update Student");
            System.out.println("5. Deactivate Student");
            System.out.println("6. Search Students by Name");
            System.out.println("0. Back to Main Menu");
            System.out.println("==============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> listAllStudents();
                case 3 -> viewStudentDetails();
                case 4 -> updateStudent();
                case 5 -> deactivateStudent();
                case 6 -> searchStudentsByName();
                case 0 -> backToMain = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addStudent() {
        System.out.println("\n--- Add New Student ---");
        String id = getStringInput("ID: ");
        String name = getStringInput("Name: ");
        String email = getStringInput("Email: ");
        String studentId = getStringInput("Student ID: ");
        
        Student student = new Student(id, name, email, studentId);
        studentService.addStudent(student);
        System.out.println("Student added successfully: " + student);
    }
    
    private void listAllStudents() {
        System.out.println("\n--- All Students ---");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(System.out::println);
        }
    }
    
    private void viewStudentDetails() {
        String studentId = getStringInput("Enter student ID: ");
        Optional<Student> student = studentService.getStudentById(studentId);
        
        if (student.isPresent()) {
            System.out.println("\n--- Student Details ---");
            System.out.println(student.get());
            
            // Show transcript
            List<TranscriptEntry> transcript = studentService.getStudentTranscript(studentId);
            if (transcript.isEmpty()) {
                System.out.println("No courses enrolled.");
            } else {
                System.out.println("\n--- Transcript ---");
                transcript.forEach(System.out::println);
                
                // Calculate GPA
                double gpa = gradingService.calculateGPA(studentId);
                System.out.printf("GPA: %.2f (%s)%n", gpa, gradingService.getGpaDescription(gpa));
            }
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void updateStudent() {
        String studentId = getStringInput("Enter student ID to update: ");
        Optional<Student> studentOpt = studentService.getStudentById(studentId);
        
        if (studentOpt.isPresent()) {
            System.out.println("Leave field blank to keep current value.");
            String name = getStringInput("New name [" + studentOpt.get().getName() + "]: ");
            String email = getStringInput("New email [" + studentOpt.get().getEmail() + "]: ");
            
            // Convert empty strings to null
            name = name.isEmpty() ? null : name;
            email = email.isEmpty() ? null : email;
            
            boolean success = studentService.updateStudent(studentId, name, email, null);
            if (success) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Failed to update student.");
            }
        } else {
            System.out.println("Student not found.");
        }
    }
    
    private void deactivateStudent() {
        String studentId = getStringInput("Enter student ID to deactivate: ");
        boolean success = studentService.deactivateStudent(studentId);
        
        if (success) {
            System.out.println("Student deactivated successfully.");
        } else {
            System.out.println("Student not found or already deactivated.");
        }
    }
    
    private void searchStudentsByName() {
        String name = getStringInput("Enter name to search: ");
        List<Student> students = studentService.getAllStudents().stream()
                .filter(s -> s.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        
        if (students.isEmpty()) {
            System.out.println("No students found with that name.");
        } else {
            System.out.println("\n--- Search Results ---");
            students.forEach(System.out::println);
        }
    }
    
    private void manageCourses() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n===== COURSE MANAGEMENT =====");
            System.out.println("1. Add Course");
            System.out.println("2. List All Courses");
            System.out.println("3. View Course Details");
            System.out.println("4. Update Course");
            System.out.println("5. Deactivate Course");
            System.out.println("6. Search Courses by Department");
            System.out.println("7. Search Courses by Instructor");
            System.out.println("0. Back to Main Menu");
            System.out.println("=============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> addCourse();
                case 2 -> listAllCourses();
                case 3 -> viewCourseDetails();
                case 4 -> updateCourse();
                case 5 -> deactivateCourse();
                case 6 -> searchCoursesByDepartment();
                case 7 -> searchCoursesByInstructor();
                case 0 -> backToMain = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void addCourse() {
        System.out.println("\n--- Add New Course ---");
        String code = getStringInput("Course Code: ");
        String title = getStringInput("Title: ");
        int credits = getIntInput("Credits: ");
        String instructorId = getStringInput("Instructor ID: ");
        
        System.out.println("Available Semesters: SPRING, SUMMER, FALL, WINTER");
        String semesterInput = getStringInput("Semester: ");
        Semester semester;
        try {
            semester = Semester.valueOf(semesterInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid semester. Defaulting to SPRING.");
            semester = Semester.SPRING;
        }
        
        int year = getIntInput("Year: ");
        String department = getStringInput("Department: ");
        
        Course course = new Course(code, title, credits, instructorId, semester, year, department);
        courseService.addCourse(course);
        System.out.println("Course added successfully: " + course);
    }
    
    private void listAllCourses() {
        System.out.println("\n--- All Courses ---");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(System.out::println);
        }
    }
    
    private void viewCourseDetails() {
        String courseCode = getStringInput("Enter course code: ");
        Optional<Course> course = courseService.getCourseByCode(courseCode);
        
        if (course.isPresent()) {
            System.out.println("\n--- Course Details ---");
            System.out.println(course.get());
            
            // Show enrollments
            List<TranscriptEntry> enrollments = enrollmentService.getCourseEnrollments(courseCode);
            if (enrollments.isEmpty()) {
                System.out.println("No students enrolled.");
            } else {
                System.out.println("\n--- Enrollments ---");
                enrollments.forEach(System.out::println);
            }
        } else {
            System.out.println("Course not found.");
        }
    }
    
    private void updateCourse() {
        String courseCode = getStringInput("Enter course code to update: ");
        Optional<Course> courseOpt = courseService.getCourseByCode(courseCode);
        
        if (courseOpt.isPresent()) {
            System.out.println("Leave field blank to keep current value.");
            String title = getStringInput("New title [" + courseOpt.get().getTitle() + "]: ");
            String creditsStr = getStringInput("New credits [" + courseOpt.get().getCredits() + "]: ");
            String instructorId = getStringInput("New instructor ID [" + courseOpt.get().getInstructorId() + "]: ");
            String department = getStringInput("New department [" + courseOpt.get().getDepartment() + "]: ");
            
            // Convert inputs
            Integer credits = creditsStr.isEmpty() ? null : Integer.parseInt(creditsStr);
            
            boolean success = courseService.updateCourse(courseCode, title, credits, instructorId, null, null, department, null);
            if (success) {
                System.out.println("Course updated successfully.");
            } else {
                System.out.println("Failed to update course.");
            }
        } else {
            System.out.println("Course not found.");
        }
    }
    
    private void deactivateCourse() {
        String courseCode = getStringInput("Enter course code to deactivate: ");
        boolean success = courseService.deactivateCourse(courseCode);
        
        if (success) {
            System.out.println("Course deactivated successfully.");
        } else {
            System.out.println("Course not found or already deactivated.");
        }
    }
    
    private void searchCoursesByDepartment() {
        String department = getStringInput("Enter department to search: ");
        List<Course> courses = courseService.getCoursesByDepartment(department);
        
        if (courses.isEmpty()) {
            System.out.println("No courses found in that department.");
        } else {
            System.out.println("\n--- Search Results ---");
            courses.forEach(System.out::println);
        }
    }
    
    private void searchCoursesByInstructor() {
        String instructorId = getStringInput("Enter instructor ID to search: ");
        List<Course> courses = courseService.getCoursesByInstructor(instructorId);
        
        if (courses.isEmpty()) {
            System.out.println("No courses found for that instructor.");
        } else {
            System.out.println("\n--- Search Results ---");
            courses.forEach(System.out::println);
        }
    }
    
    private void manageEnrollment() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n===== ENROLLMENT MANAGEMENT =====");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Unenroll Student from Course");
            System.out.println("3. View Student Enrollments");
            System.out.println("4. View Course Enrollments");
            System.out.println("0. Back to Main Menu");
            System.out.println("=================================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> enrollStudent();
                case 2 -> unenrollStudent();
                case 3 -> viewStudentEnrollments();
                case 4 -> viewCourseEnrollments();
                case 0 -> backToMain = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void enrollStudent() {
        System.out.println("\n--- Enroll Student in Course ---");
        String studentId = getStringInput("Student ID: ");
        String courseCode = getStringInput("Course Code: ");
        
        Optional<TranscriptEntry> enrollment = enrollmentService.enrollStudent(studentId, courseCode);
        if (enrollment.isPresent()) {
            System.out.println("Student enrolled successfully: " + enrollment.get());
        } else {
            System.out.println("Failed to enroll student. Check if student/course exists and is active, or if credit limit is exceeded.");
        }
    }
    
    private void unenrollStudent() {
        System.out.println("\n--- Unenroll Student from Course ---");
        String studentId = getStringInput("Student ID: ");
        String courseCode = getStringInput("Course Code: ");
        
        boolean success = enrollmentService.unenrollStudent(studentId, courseCode);
        if (success) {
            System.out.println("Student unenrolled successfully.");
        } else {
            System.out.println("Failed to unenroll student. Enrollment not found or student already graded.");
        }
    }
    
    private void viewStudentEnrollments() {
        String studentId = getStringInput("Enter student ID: ");
        List<TranscriptEntry> enrollments = enrollmentService.getStudentEnrollments(studentId);
        
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this student.");
        } else {
            System.out.println("\n--- Student Enrollments ---");
            enrollments.forEach(System.out::println);
        }
    }
    
    private void viewCourseEnrollments() {
        String courseCode = getStringInput("Enter course code: ");
        List<TranscriptEntry> enrollments = enrollmentService.getCourseEnrollments(courseCode);
        
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments found for this course.");
        } else {
            System.out.println("\n--- Course Enrollments ---");
            enrollments.forEach(System.out::println);
        }
    }
    
    private void manageGrades() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n===== GRADING MANAGEMENT =====");
            System.out.println("1. Record Grade for Student");
            System.out.println("2. View Student GPA");
            System.out.println("0. Back to Main Menu");
            System.out.println("==============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> recordGrade();
                case 2 -> viewStudentGPA();
                case 0 -> backToMain = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void recordGrade() {
        System.out.println("\n--- Record Grade for Student ---");
        String studentId = getStringInput("Student ID: ");
        String courseCode = getStringInput("Course Code: ");
        double marks = getDoubleInput("Marks: ");
        
        boolean success = gradingService.recordGrade(studentId, courseCode, marks);
        if (success) {
            System.out.println("Grade recorded successfully.");
        } else {
            System.out.println("Failed to record grade. Enrollment not found.");
        }
    }
    
    private void viewStudentGPA() {
        String studentId = getStringInput("Enter student ID: ");
        double gpa = gradingService.calculateGPA(studentId);
        
        System.out.printf("GPA for student %s: %.2f (%s)%n", 
                         studentId, gpa, gradingService.getGpaDescription(gpa));
    }
    
    private void importExportData() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n===== IMPORT/EXPORT DATA =====");
            System.out.println("1. Import Students from CSV");
            System.out.println("2. Import Courses from CSV");
            System.out.println("3. Export Students to CSV");
            System.out.println("4. Export Courses to CSV");
            System.out.println("0. Back to Main Menu");
            System.out.println("==============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> importStudentsFromCSV();
                case 2 -> importCoursesFromCSV();
                case 3 -> exportStudentsToCSV();
                case 4 -> exportCoursesToCSV();
                case 0 -> backToMain = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void importStudentsFromCSV() {
        String filePath = getStringInput("Enter CSV file path: ");
        try {
            appConfig.getImportExportService().importStudentsFromCSV(filePath);
            System.out.println("Students imported successfully.");
        } catch (IOException e) {
            System.out.println("Error importing students: " + e.getMessage());
        }
    }
    
    private void importCoursesFromCSV() {
        String filePath = getStringInput("Enter CSV file path: ");
        try {
            appConfig.getImportExportService().importCoursesFromCSV(filePath);
            System.out.println("Courses imported successfully.");
        } catch (IOException e) {
            System.out.println("Error importing courses: " + e.getMessage());
        }
    }
    
    private void exportStudentsToCSV() {
        String filePath = getStringInput("Enter CSV file path: ");
        try {
            appConfig.getImportExportService().exportStudentsToCSV(filePath);
            System.out.println("Students exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting students: " + e.getMessage());
        }
    }
    
    private void exportCoursesToCSV() {
        String filePath = getStringInput("Enter CSV file path: ");
        try {
            appConfig.getImportExportService().exportCoursesToCSV(filePath);
            System.out.println("Courses exported successfully.");
        } catch (IOException e) {
            System.out.println("Error exporting courses: " + e.getMessage());
        }
    }
    
    private void backupOperations() {
        boolean backToMain = false;
        while (!backToMain) {
            System.out.println("\n===== BACKUP OPERATIONS =====");
            System.out.println("1. Create Backup");
            System.out.println("2. Calculate Backup Size");
            System.out.println("0. Back to Main Menu");
            System.out.println("=============================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1 -> createBackup();
                case 2 -> calculateBackupSize();
                case 0 -> backToMain = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void createBackup() {
        try {
            backupUtility.createBackup(appConfig.getBackupDirectory());
        } catch (IOException e) {
            System.out.println("Error creating backup: " + e.getMessage());
        }
    }
    
    private void calculateBackupSize() {
        try {
            long size = backupUtility.calculateBackupSize(appConfig.getBackupDirectory());
            System.out.println("Total backup size: " + size + " bytes");
        } catch (IOException e) {
            System.out.println("Error calculating backup size: " + e.getMessage());
        }
    }
    
    private void generateReports() {
        System.out.println("\n=== REPORTS ===");
        
        // 1. Top students by GPA (using Streams)
        System.out.println("\n1. Top Students by GPA:");
        List<Student> students = studentService.getAllStudents();
        students.stream()
            .filter(student -> !studentService.getStudentTranscript(student.getId()).isEmpty())
            .sorted((s1, s2) -> Double.compare(
                gradingService.calculateGPA(s2.getId()), 
                gradingService.calculateGPA(s1.getId())
            ))
            .limit(3)
            .forEach(student -> {
                double gpa = gradingService.calculateGPA(student.getId());
                System.out.printf("%s: GPA %.2f (%s)%n", 
                    student.getName(), gpa, gradingService.getGpaDescription(gpa));
            });
        
        // 2. GPA distribution (using Streams)
        System.out.println("\n2. GPA Distribution:");
        long excellent = students.stream()
            .filter(student -> gradingService.calculateGPA(student.getId()) >= 3.5)
            .count();
        long good = students.stream()
            .filter(student -> {
                double gpa = gradingService.calculateGPA(student.getId());
                return gpa >= 3.0 && gpa < 3.5;
            })
            .count();
        long satisfactory = students.stream()
            .filter(student -> {
                double gpa = gradingService.calculateGPA(student.getId());
                return gpa >= 2.0 && gpa < 3.0;
            })
            .count();
        long passing = students.stream()
            .filter(student -> {
                double gpa = gradingService.calculateGPA(student.getId());
                return gpa >= 1.0 && gpa < 2.0;
            })
            .count();
        long failing = students.stream()
            .filter(student -> gradingService.calculateGPA(student.getId()) < 1.0)
            .count();
        
        System.out.printf("Excellent (3.5+): %d students%n", excellent);
        System.out.printf("Good (3.0-3.49): %d students%n", good);
        System.out.printf("Satisfactory (2.0-2.99): %d students%n", satisfactory);
        System.out.printf("Passing (1.0-1.99): %d students%n", passing);
        System.out.printf("Failing (<1.0): %d students%n", failing);
        
        // 3. Course enrollment statistics
        System.out.println("\n3. Course Enrollment Statistics:");
        courseService.getAllCourses().forEach(course -> {
            long enrollmentCount = enrollmentService.getCourseEnrollments(course.getCourseCode()).size();
            System.out.printf("%s: %d students enrolled%n", course.getCourseCode(), enrollmentCount);
        });
    }
    
    // Utility methods for input handling
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
}