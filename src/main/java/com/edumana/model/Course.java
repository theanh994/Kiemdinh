package com.edumana.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_code", unique = true, nullable = false)
    @NotBlank(message = "Mã học phần không được để trống")
    private String courseCode; // Mã số học phần

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Tên học phần không được để trống")
    private String name; // Tên học phần

    @Column(name = "credits", nullable = false)
    @NotNull(message = "Số tín chỉ không được để trống")
    @Min(value = 1, message = "Số tín chỉ phải lớn hơn 0")
    private Integer credits; // Số tín chỉ

    @Column(name = "coefficient", nullable = false)
    private Double coefficient; // Hệ số học phần

    @Column(name = "periods", nullable = false)
    @NotNull(message = "Số tiết học không được để trống")
    @Min(value = 1, message = "Số tiết học phải lớn hơn 0")
    private Integer periods; // Số tiết học

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    @NotNull(message = "Vui lòng chọn khoa quản lý")
    private Department department; // Khoa quản lý

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    // Constructors
    public Course() {
    }

    public Course(String courseCode, String name, Integer credits, Double coefficient, Integer periods) {
        this.courseCode = courseCode;
        this.name = name;
        this.credits = credits;
        this.coefficient = coefficient;
        this.periods = periods;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(Double coefficient) {
        this.coefficient = coefficient;
    }

    public Integer getPeriods() {
        return periods;
    }

    public void setPeriods(Integer periods) {
        this.periods = periods;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", courseCode='" + courseCode + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", coefficient=" + coefficient +
                ", periods=" + periods +
                '}';
    }
}
