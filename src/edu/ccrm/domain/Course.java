package edu.ccrm.domain;

public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String instructorId;
    private Semester semester;
    private int year;
    private String department;
    private boolean active;
    
    public Course(String courseCode, String title, int credits, String instructorId, 
                 Semester semester, int year, String department) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.instructorId = instructorId;
        this.semester = semester;
        this.year = year;
        this.department = department;
        this.active = true;
    }
    
    // Getters and setters
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    
    public String getInstructorId() { return instructorId; }
    public void setInstructorId(String instructorId) { this.instructorId = instructorId; }
    
    public Semester getSemester() { return semester; }
    public void setSemester(Semester semester) { this.semester = semester; }
    
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    @Override
    public String toString() {
        return courseCode + ": " + title + " (" + credits + " credits), Instructor: " + instructorId + 
               ", " + semester + " " + year + ", Department: " + department;
    }
}