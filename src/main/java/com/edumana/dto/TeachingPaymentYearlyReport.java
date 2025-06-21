package com.edumana.dto;

import java.math.BigDecimal;
import java.util.List;

public class TeachingPaymentYearlyReport {
    private String teacherName;
    private String teacherDegree;
    private String department;
    private int year;
    private BigDecimal teacherCoefficient;
    private List<SemesterPayment> semesterPayments;
    private BigDecimal totalYearlyPayment;

    public TeachingPaymentYearlyReport(String teacherName, String teacherDegree, 
                                      String department, int year,
                                      BigDecimal teacherCoefficient, 
                                      List<SemesterPayment> semesterPayments,
                                      BigDecimal totalYearlyPayment) {
        this.teacherName = teacherName;
        this.teacherDegree = teacherDegree;
        this.department = department;
        this.year = year;
        this.teacherCoefficient = teacherCoefficient;
        this.semesterPayments = semesterPayments;
        this.totalYearlyPayment = totalYearlyPayment;
    }

    // Nested class for semester payment details
    public static class SemesterPayment {
        private String semesterName;
        private int totalClasses;
        private int totalPeriods;
        private BigDecimal totalPayment;

        public SemesterPayment(String semesterName, int totalClasses, 
                             int totalPeriods, BigDecimal totalPayment) {
            this.semesterName = semesterName;
            this.totalClasses = totalClasses;
            this.totalPeriods = totalPeriods;
            this.totalPayment = totalPayment;
        }

        public String getSemesterName() { return semesterName; }
        public int getTotalClasses() { return totalClasses; }
        public int getTotalPeriods() { return totalPeriods; }
        public BigDecimal getTotalPayment() { return totalPayment; }
    }

    // Getters
    public String getTeacherName() { return teacherName; }
    public String getTeacherDegree() { return teacherDegree; }
    public String getDepartment() { return department; }
    public int getYear() { return year; }
    public BigDecimal getTeacherCoefficient() { return teacherCoefficient; }
    public List<SemesterPayment> getSemesterPayments() { return semesterPayments; }
    public BigDecimal getTotalYearlyPayment() { return totalYearlyPayment; }
}
