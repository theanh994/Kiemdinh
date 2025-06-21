package com.edumana.dto;

import java.math.BigDecimal;
import java.util.List;

public class TeachingPaymentDTO {
    private String teacherName;
    private String teacherDegree;
    private String semesterName;
    private BigDecimal teacherCoefficient;
    private List<ClassPaymentDetail> classDetails;
    private BigDecimal totalPayment;

    // Constructor
    public TeachingPaymentDTO(String teacherName, String teacherDegree, String semesterName, 
                             BigDecimal teacherCoefficient, List<ClassPaymentDetail> classDetails, 
                             BigDecimal totalPayment) {
        this.teacherName = teacherName;
        this.teacherDegree = teacherDegree;
        this.semesterName = semesterName;
        this.teacherCoefficient = teacherCoefficient;
        this.classDetails = classDetails;
        this.totalPayment = totalPayment;
    }

    // Nested class for class payment details
    public static class ClassPaymentDetail {
        private String classCode;
        private String className;
        private String courseName;
        private int actualPeriods;
        private int numberOfStudents;
        private BigDecimal courseCoefficient;
        private BigDecimal classCoefficient;
        private BigDecimal convertedPeriods;
        private BigDecimal payment;

        public ClassPaymentDetail(String classCode, String className, String courseName,
                                int actualPeriods, int numberOfStudents,
                                BigDecimal courseCoefficient, BigDecimal classCoefficient,
                                BigDecimal convertedPeriods, BigDecimal payment) {
            this.classCode = classCode;
            this.className = className;
            this.courseName = courseName;
            this.actualPeriods = actualPeriods;
            this.numberOfStudents = numberOfStudents;
            this.courseCoefficient = courseCoefficient;
            this.classCoefficient = classCoefficient;
            this.convertedPeriods = convertedPeriods;
            this.payment = payment;
        }

        // Getters
        public String getClassCode() { return classCode; }
        public String getClassName() { return className; }
        public String getCourseName() { return courseName; }
        public int getActualPeriods() { return actualPeriods; }
        public int getNumberOfStudents() { return numberOfStudents; }
        public BigDecimal getCourseCoefficient() { return courseCoefficient; }
        public BigDecimal getClassCoefficient() { return classCoefficient; }
        public BigDecimal getConvertedPeriods() { return convertedPeriods; }
        public BigDecimal getPayment() { return payment; }
    }

    // Getters
    public String getTeacherName() { return teacherName; }
    public String getTeacherDegree() { return teacherDegree; }
    public String getSemesterName() { return semesterName; }
    public BigDecimal getTeacherCoefficient() { return teacherCoefficient; }
    public List<ClassPaymentDetail> getClassDetails() { return classDetails; }
    public BigDecimal getTotalPayment() { return totalPayment; }
}
