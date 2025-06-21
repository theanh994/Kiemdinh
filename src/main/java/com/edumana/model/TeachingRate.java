package com.edumana.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "teaching_rates")
public class TeachingRate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Vui lòng nhập định mức tiền cho một tiết")
    @Column(name = "base_rate_per_period", nullable = false)
    private BigDecimal baseRatePerPeriod;

    @Column(name = "start_date", nullable = false)
    private java.time.LocalDate startDate;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    public TeachingRate() {
    }

    public TeachingRate(BigDecimal baseRatePerPeriod, java.time.LocalDate startDate, String description) {
        this.baseRatePerPeriod = baseRatePerPeriod;
        this.startDate = startDate;
        this.description = description;
        this.active = true;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getBaseRatePerPeriod() {
        return baseRatePerPeriod;
    }

    public void setBaseRatePerPeriod(BigDecimal baseRatePerPeriod) {
        this.baseRatePerPeriod = baseRatePerPeriod;
    }

    public java.time.LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(java.time.LocalDate startDate) {
        this.startDate = startDate;
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
