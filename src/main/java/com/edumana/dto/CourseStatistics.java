package com.edumana.dto;

public class CourseStatistics {
    private String courseCode;
    private String courseName;
    private int credits;
    private int totalClasses;
    private int totalStudents;
    private String semester;

    public CourseStatistics(String courseCode, String courseName, int credits, int totalClasses, int totalStudents, String semester) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.totalClasses = totalClasses;
        this.totalStudents = totalStudents;
        this.semester = semester;
    }

    // Getters
    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public String getSemester() {
        return semester;
    }
}
