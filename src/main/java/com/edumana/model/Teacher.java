package com.edumana.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "teachers")
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "teacher_code", unique = true, nullable = false)
    private String teacherCode; // Mã số giáo viên

    @Column(name = "full_name", nullable = false)
    private String fullName; // Họ tên giáo viên

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate; // Ngày sinh

    @Column(name = "phone", nullable = false)
    private String phone; // Số điện thoại

    @Column(name = "email", nullable = false, unique = true)
    private String email; // Email

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department; // Khoa

    @ManyToOne
    @JoinColumn(name = "degree_id", nullable = false)
    private Degree degree; // Bằng cấp

    @Transient
    private Long departmentId;

    @Transient
    private Long degreeId;

    // Constructors
    public Teacher() {
    }

    public Teacher(String teacherCode, String fullName, LocalDate birthDate, 
                  String phone, String email, Department department, Degree degree) {
        this.teacherCode = teacherCode;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.email = email;
        this.department = department;
        this.degree = degree;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        this.degree = degree;
    }

    public Long getDepartmentId() {
        return this.department != null ? this.department.getId() : this.departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getDegreeId() {
        return this.degree != null ? this.degree.getId() : this.degreeId;
    }

    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", teacherCode='" + teacherCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", birthDate=" + birthDate +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", department=" + department +
                ", degree=" + degree +
                '}';
    }
}
