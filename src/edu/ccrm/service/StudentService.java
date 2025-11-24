package edu.ccrm.service;

import edu.ccrm.domain.Student;
import edu.ccrm.domain.TranscriptEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentService {
    private List<Student> students;
    
    public StudentService() {
        this.students = new ArrayList<>();
    }
    
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public Optional<Student> getStudentById(String id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }
    
    public Optional<Student> getStudentByStudentId(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public List<Student> getActiveStudents() {
        return students.stream()
                .filter(Student::isActive)
                .collect(Collectors.toList());
    }
    
    public boolean updateStudent(String id, String name, String email, Boolean active) {
        Optional<Student> studentOpt = getStudentById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (name != null) student.setName(name);
            if (email != null) student.setEmail(email);
            if (active != null) student.setActive(active);
            return true;
        }
        return false;
    }
    
    public boolean deactivateStudent(String id) {
        Optional<Student> studentOpt = getStudentById(id);
        if (studentOpt.isPresent()) {
            studentOpt.get().setActive(false);
            return true;
        }
        return false;
    }
    
    public void addTranscriptEntry(String studentId, TranscriptEntry entry) {
        getStudentById(studentId).ifPresent(student -> student.addTranscriptEntry(entry));
    }
    
    public List<TranscriptEntry> getStudentTranscript(String studentId) {
        return getStudentById(studentId)
                .map(Student::getTranscript)
                .orElse(new ArrayList<>());
    }
}