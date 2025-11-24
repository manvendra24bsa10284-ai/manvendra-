package edu.ccrm.domain;


public enum Grade {
    A(4.0), B(3.0), C(2.0), D(1.0), F(0.0);
    
    private final double gradePoint;
    
    Grade(double gradePoint) {
        this.gradePoint = gradePoint;
    }
    
    public double getGradePoint() {
        return gradePoint;
    }
    
    public static Grade fromPercentage(double percentage) {
        if (percentage >= 90) return A;
        else if (percentage >= 80) return B;
        else if (percentage >= 70) return C;
        else if (percentage >= 60) return D;
        else return F;
    }
    
    @Override
    public String toString() {
        return name();
    }
}