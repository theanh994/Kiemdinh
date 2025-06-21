package com.edumana.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;

@Entity
@Table(name = "degrees")
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên đầy đủ không được để trống")
    @Column(name = "full_name", nullable = false)
    private String fullName; // Tên đầy đủ của bằng cấp

    @NotBlank(message = "Tên viết tắt không được để trống")
    @Column(name = "abbreviation", nullable = false, unique = true)
    private String abbreviation; // Tên viết tắt

    @NotNull(message = "Hệ số giảng dạy không được để trống")
    @DecimalMin(value = "1.0", message = "Hệ số giảng dạy phải lớn hơn hoặc bằng 1.0")
    @Column(name = "teaching_coefficient", nullable = false)
    private BigDecimal teachingCoefficient;

    // Constructors
    public Degree() {
    }

    public Degree(String fullName, String abbreviation, BigDecimal teachingCoefficient) {
        this.fullName = fullName;
        this.abbreviation = abbreviation;
        this.teachingCoefficient = teachingCoefficient;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public BigDecimal getTeachingCoefficient() {
        return teachingCoefficient;
    }

    public void setTeachingCoefficient(BigDecimal teachingCoefficient) {
        this.teachingCoefficient = teachingCoefficient;
    }

    @Override
    public String toString() {
        return "Degree{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", teachingCoefficient=" + teachingCoefficient +
                '}';
    }
}
