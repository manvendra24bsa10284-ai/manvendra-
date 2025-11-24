package edu.ccrm.io;

import edu.ccrm.domain.*;
import edu.ccrm.service.StudentService;
import edu.ccrm.service.CourseService;
import edu.ccrm.service.EnrollmentService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class ImportExportService {
    private StudentService studentService;
    private CourseService courseService;
    private EnrollmentService enrollmentService;
    
    public ImportExportService(StudentService studentService, CourseService courseService, 
                              EnrollmentService enrollmentService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollmentService = enrollmentService;
    }
    
    public void importStudentsFromCSV(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        // Skip header
        for (int i = 1; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data.length >= 4) {
                String id = data[0].trim();
                String name = data[1].trim();
                String email = data[2].trim();
                String studentId = data[3].trim();
                
                studentService.addStudent(new Student(id, name, email, studentId));
            }
        }
    }
    
    public void importCoursesFromCSV(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        // Skip header
        for (int i = 1; i < lines.size(); i++) {
            String[] data = lines.get(i).split(",");
            if (data.length >= 7) {
                String courseCode = data[0].trim();
                String title = data[1].trim();
                int credits = Integer.parseInt(data[2].trim());
                String instructorId = data[3].trim();
                Semester semester = Semester.valueOf(data[4].trim().toUpperCase());
                int year = Integer.parseInt(data[5].trim());
                String department = data[6].trim();
                
                courseService.addCourse(new Course(courseCode, title, credits, instructorId, semester, year, department));
            }
        }
    }
    
    public void exportStudentsToCSV(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("ID,Name,Email,StudentID,Active\n");
        
        for (Student student : studentService.getAllStudents()) {
            sb.append(student.getId()).append(",")
              .append(student.getName()).append(",")
              .append(student.getEmail()).append(",")
              .append(student.getStudentId()).append(",")
              .append(student.isActive()).append("\n");
        }
        
        Files.write(Paths.get(filePath), sb.toString().getBytes(), 
                   StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public void exportCoursesToCSV(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Code,Title,Credits,InstructorID,Semester,Year,Department,Active\n");
        
        for (Course course : courseService.getAllCourses()) {
            sb.append(course.getCourseCode()).append(",")
              .append(course.getTitle()).append(",")
              .append(course.getCredits()).append(",")
              .append(course.getInstructorId()).append(",")
              .append(course.getSemester()).append(",")
              .append(course.getYear()).append(",")
              .append(course.getDepartment()).append(",")
              .append(course.isActive()).append("\n");
        }
        
        Files.write(Paths.get(filePath), sb.toString().getBytes(), 
                   StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    
    public void exportEnrollmentsToCSV(String filePath) throws IOException {
        // This would need access to enrollment data
        // Implementation would be similar to the other export methods
    }
}