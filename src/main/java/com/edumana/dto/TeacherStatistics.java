package com.edumana.dto;

public class TeacherStatistics {
    private long totalTeachers;
    private long teachersByDepartment;
    private long teachersByDegree;
    private String departmentName;
    private String degreeName;

    public TeacherStatistics(long totalTeachers, long teachersByDepartment, long teachersByDegree, 
                            String departmentName, String degreeName) {
        this.totalTeachers = totalTeachers;
        this.teachersByDepartment = teachersByDepartment;
        this.teachersByDegree = teachersByDegree;
        this.departmentName = departmentName;
        this.degreeName = degreeName;
    }

    // Getters and Setters
    public long getTotalTeachers() {
        return totalTeachers;
    }

    public void setTotalTeachers(long totalTeachers) {
        this.totalTeachers = totalTeachers;
    }

    public long getTeachersByDepartment() {
        return teachersByDepartment;
    }

    public void setTeachersByDepartment(long teachersByDepartment) {
        this.teachersByDepartment = teachersByDepartment;
    }

    public long getTeachersByDegree() {
        return teachersByDegree;
    }

    public void setTeachersByDegree(long teachersByDegree) {
        this.teachersByDegree = teachersByDegree;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDegreeName() {
        return degreeName;
    }

    public void setDegreeName(String degreeName) {
        this.degreeName = degreeName;
    }
}
