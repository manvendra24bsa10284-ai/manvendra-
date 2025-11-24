package edu.ccrm.domain;

import java.time.LocalDate;

public abstract class Person {
    private String id;
    private String name;
    private String email;
    private LocalDate dateCreated;
    
    public Person(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateCreated = LocalDate.now();
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getDateCreated() { return dateCreated; }
    
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Email: " + email;
    }
}