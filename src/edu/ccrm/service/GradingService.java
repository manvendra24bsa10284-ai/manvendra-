package edu.ccrm.service;

import edu.ccrm.domain.Grade;
import edu.ccrm.domain.TranscriptEntry;
import java.util.List;
import java.util.Optional;

public class GradingService {
    private EnrollmentService enrollmentService;
    
    public GradingService(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }
    
    public boolean recordGrade(String studentId, String courseCode, double marks, Grade grade) {
        Optional<TranscriptEntry> enrollment = enrollmentService.getEnrollment(studentId, courseCode);
        if (enrollment.isPresent()) {
            enrollment.get().recordGrade(marks, grade);
            return true;
        }
        return false;
    }
    
    public boolean recordGrade(String studentId, String courseCode, double marks) {
        return recordGrade(studentId, courseCode, marks, Grade.fromPercentage(marks));
    }
    
    public double calculateGPA(String studentId) {
        List<TranscriptEntry> enrollments = enrollmentService.getStudentEnrollments(studentId);
        
        double totalGradePoints = 0;
        int totalCredits = 0;
        
        for (TranscriptEntry enrollment : enrollments) {
            if (enrollment.isGraded()) {
                // In a real implementation, we would get the course credits from CourseService
                int credits = 3; // Default value for demonstration
                totalGradePoints += enrollment.getGrade().getGradePoint() * credits;
                totalCredits += credits;
            }
        }
        
        return totalCredits > 0 ? totalGradePoints / totalCredits : 0;
    }
    
    public String getGpaDescription(double gpa) {
        if (gpa >= 3.5) return "Excellent";
        else if (gpa >= 3.0) return "Good";
        else if (gpa >= 2.0) return "Satisfactory";
        else if (gpa >= 1.0) return "Passing";
        else return "Failing";
    }
}