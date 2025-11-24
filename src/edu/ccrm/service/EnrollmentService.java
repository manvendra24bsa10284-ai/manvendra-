package edu.ccrm.service;

import edu.ccrm.domain.TranscriptEntry;
import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentService {
    private StudentService studentService;
    private CourseService courseService;
    private List<TranscriptEntry> enrollments;
    private static final int MAX_CREDITS_PER_SEMESTER = 18;
    
    public EnrollmentService(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.enrollments = new ArrayList<>();
    }
    
    public Optional<TranscriptEntry> enrollStudent(String studentId, String courseCode) {
        Optional<Student> studentOpt = studentService.getStudentById(studentId);
        Optional<Course> courseOpt = courseService.getCourseByCode(courseCode);
        
        if (studentOpt.isEmpty() || courseOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Student student = studentOpt.get();
        Course course = courseOpt.get();
        
        if (!student.isActive() || !course.isActive()) {
            return Optional.empty();
        }
        
        // Check if already enrolled
        if (isEnrolled(studentId, courseCode)) {
            return Optional.empty();
        }
        
        // Check credit limit
        if (getCurrentSemesterCredits(studentId, course.getSemester(), course.getYear()) + course.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            return Optional.empty();
        }
        
        TranscriptEntry enrollment = new TranscriptEntry(studentId, courseCode);
        enrollments.add(enrollment);
        studentService.addTranscriptEntry(studentId, enrollment);
        
        return Optional.of(enrollment);
    }
    
    public boolean unenrollStudent(String studentId, String courseCode) {
        Optional<TranscriptEntry> enrollment = getEnrollment(studentId, courseCode);
        if (enrollment.isPresent()) {
            enrollments.remove(enrollment.get());
            
            // Remove from student's transcript
            Optional<Student> studentOpt = studentService.getStudentById(studentId);
            studentOpt.ifPresent(student -> 
                student.getTranscript().removeIf(e -> 
                    e.getCourseCode().equals(courseCode) && !e.isGraded()));
            
            return true;
        }
        return false;
    }
    
    public boolean isEnrolled(String studentId, String courseCode) {
        return getEnrollment(studentId, courseCode).isPresent();
    }
    
    public Optional<TranscriptEntry> getEnrollment(String studentId, String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId) && e.getCourseCode().equals(courseCode))
                .findFirst();
    }
    
    public List<TranscriptEntry> getStudentEnrollments(String studentId) {
        return enrollments.stream()
                .filter(e -> e.getStudentId().equals(studentId))
                .collect(java.util.stream.Collectors.toList());
    }
    
    public List<TranscriptEntry> getCourseEnrollments(String courseCode) {
        return enrollments.stream()
                .filter(e -> e.getCourseCode().equals(courseCode))
                .collect(java.util.stream.Collectors.toList());
    }
    
    private int getCurrentSemesterCredits(String studentId, Semester semester, int year) {
        return getStudentEnrollments(studentId).stream()
                .filter(e -> {
                    Optional<Course> course = courseService.getCourseByCode(e.getCourseCode());
                    return course.isPresent() && 
                           course.get().getSemester() == semester && 
                           course.get().getYear() == year;
                })
                .mapToInt(e -> {
                    Optional<Course> course = courseService.getCourseByCode(e.getCourseCode());
                    return course.map(Course::getCredits).orElse(0);
                })
                .sum();
    }
}