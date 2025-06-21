package com.edumana.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "semesters")
public class Semester {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name; // Tên kỳ học (Học kỳ 1, Học kỳ 2, Học kỳ hè)

    @Column(name = "academic_year", nullable = false)
    private String academicYear; // Năm học (VD: 2024-2025)

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate; // Ngày bắt đầu

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate; // Ngày kết thúc

    // Constructors
    public Semester() {
    }

    public Semester(String name, String academicYear, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.academicYear = academicYear;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public int getYear() {
        return startDate != null ? startDate.getYear() : 0;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Lấy số kỳ học: 1 cho Học kỳ 1, 2 cho Học kỳ 2, 3 cho Học kỳ hè
    public String getTerm() {
        if (name == null) return "1";
        
        if (name.toLowerCase().contains("hè")) {
            return "3";
        } else if (name.contains("2")) {
            return "2";
        } else {
            return "1";
        }
    }

    // Lấy năm học đầu tiên từ chuỗi năm học (VD: từ "2023-2024" lấy "2023")
    public String getStartYear() {
        if (academicYear == null || academicYear.isEmpty()) {
            return String.valueOf(LocalDate.now().getYear());
        }
        return academicYear.split("-")[0];
    }

    @Override
    public String toString() {
        return "Semester{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    // Phương thức tiện ích để kiểm tra tính hợp lệ của ngày
    public boolean isValidDateRange() {
        if (startDate == null || endDate == null) {
            return false;
        }
        return !endDate.isBefore(startDate);
    }
}
