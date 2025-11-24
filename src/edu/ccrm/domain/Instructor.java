package edu.ccrm.domain;

public class Instructor extends Person {
    private String department;
    private String title;
    
    public Instructor(String id, String name, String email, String department, String title) {
        super(id, name, email);
        this.department = department;
        this.title = title;
    }
    
    // Getters and setters
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    @Override
    public String toString() {
        return super.toString() + ", Department: " + department + ", Title: " + title;
    }
}