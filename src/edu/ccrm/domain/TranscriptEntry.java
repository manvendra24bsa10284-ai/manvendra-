package edu.ccrm.domain;

import java.time.LocalDate;

public class TranscriptEntry {
    private String studentId;
    private String courseCode;
    private LocalDate enrollmentDate;
    private Double marks;
    private Grade grade;
    
    public TranscriptEntry(String studentId, String courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
        this.enrollmentDate = LocalDate.now();
        this.marks = null;
        this.grade = null;
    }
    
    public void recordGrade(double marks, Grade grade) {
        this.marks = marks;
        this.grade = grade;
    }
    
    // Getters
    public String getStudentId() { return studentId; }
    public String getCourseCode() { return courseCode; }
    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public Double getMarks() { return marks; }
    public Grade getGrade() { return grade; }
    public boolean isGraded() { return grade != null; }
    
    @Override
    public String toString() {
        return "Student: " + studentId + ", Course: " + courseCode + 
               ", Enrollment Date: " + enrollmentDate + 
               (isGraded() ? ", Marks: " + marks + ", Grade: " + grade : ", Not graded");
    }
}