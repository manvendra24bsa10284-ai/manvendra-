package edu.ccrm.domain;

import java.util.ArrayList;
import java.util.List;

public class Student extends Person {
    private String studentId;
    private boolean active;
    private List<TranscriptEntry> transcript;
    
    public Student(String id, String name, String email, String studentId) {
        super(id, name, email);
        this.studentId = studentId;
        this.active = true;
        this.transcript = new ArrayList<>();
    }
    
    public void addTranscriptEntry(TranscriptEntry entry) {
        transcript.add(entry);
    }
    
    // Getters and setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public List<TranscriptEntry> getTranscript() { return transcript; }
    
    @Override
    public String toString() {
        return super.toString() + ", Student ID: " + studentId + ", Status: " + (active ? "Active" : "Inactive");
    }
}