package com.edumana.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Entity
@Table(name = "class_coefficients")
public class ClassCoefficient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Số sinh viên tối thiểu không được để trống")
    @Min(value = 0, message = "Số sinh viên tối thiểu phải lớn hơn hoặc bằng 0")
    @Column(name = "min_students", nullable = false)
    private Integer minStudents;

    @NotNull(message = "Số sinh viên tối đa không được để trống")
    @Min(value = 1, message = "Số sinh viên tối đa phải lớn hơn 0")
    @Column(name = "max_students", nullable = false)
    private Integer maxStudents;

    @NotNull(message = "Hệ số lớp không được để trống")
    @DecimalMin(value = "-0.5", message = "Hệ số lớp phải lớn hơn -0.5")
    @DecimalMax(value = "0.5", message = "Hệ số lớp phải nhỏ hơn 0.5")
    @Column(name = "coefficient", nullable = false, precision = 3, scale = 2)
    private BigDecimal coefficient;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    // Constructors
    public ClassCoefficient() {
    }

    public ClassCoefficient(Integer minStudents, Integer maxStudents, BigDecimal coefficient, String description) {
        this.minStudents = minStudents;
        this.maxStudents = maxStudents;
        this.coefficient = coefficient;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinStudents() {
        return minStudents;
    }

    public void setMinStudents(Integer minStudents) {
        this.minStudents = minStudents;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public BigDecimal getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(BigDecimal coefficient) {
        this.coefficient = coefficient;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
