package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Semester;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CourseService {
    private List<Course> courses;
    
    public CourseService() {
        this.courses = new ArrayList<>();
    }
    
    public void addCourse(Course course) {
        courses.add(course);
    }
    
    public Optional<Course> getCourseByCode(String courseCode) {
        return courses.stream()
                .filter(c -> c.getCourseCode().equals(courseCode))
                .findFirst();
    }
    
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
    
    public List<Course> getActiveCourses() {
        return courses.stream()
                .filter(Course::isActive)
                .collect(Collectors.toList());
    }
    
    public boolean updateCourse(String courseCode, String title, Integer credits, 
                               String instructorId, Semester semester, Integer year, 
                               String department, Boolean active) {
        Optional<Course> courseOpt = getCourseByCode(courseCode);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            if (title != null) course.setTitle(title);
            if (credits != null) course.setCredits(credits);
            if (instructorId != null) course.setInstructorId(instructorId);
            if (semester != null) course.setSemester(semester);
            if (year != null) course.setYear(year);
            if (department != null) course.setDepartment(department);
            if (active != null) course.setActive(active);
            return true;
        }
        return false;
    }
    
    public boolean deactivateCourse(String courseCode) {
        Optional<Course> courseOpt = getCourseByCode(courseCode);
        if (courseOpt.isPresent()) {
            courseOpt.get().setActive(false);
            return true;
        }
        return false;
    }
    
    public List<Course> getCoursesByInstructor(String instructorId) {
        return courses.stream()
                .filter(c -> c.getInstructorId().equals(instructorId) && c.isActive())
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesByDepartment(String department) {
        return courses.stream()
                .filter(c -> c.getDepartment().equalsIgnoreCase(department) && c.isActive())
                .collect(Collectors.toList());
    }
    
    public List<Course> getCoursesBySemester(Semester semester, Integer year) {
        return courses.stream()
                .filter(c -> c.getSemester() == semester && 
                            (year == null || c.getYear() == year) && 
                            c.isActive())
                .collect(Collectors.toList());
    }
}