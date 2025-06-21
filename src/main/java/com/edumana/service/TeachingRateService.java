package com.edumana.service;

import com.edumana.model.TeachingRate;
import com.edumana.repository.TeachingRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TeachingRateService {

    @Autowired
    private TeachingRateRepository teachingRateRepository;

    public List<TeachingRate> getAllRates() {
        return teachingRateRepository.findAll();
    }

    public Optional<TeachingRate> getCurrentRate() {
        return teachingRateRepository.findCurrentRate();
    }

    @Transactional
    public TeachingRate save(TeachingRate newRate) {
        if (newRate.isActive()) {
            // Deactivate all other active rates
            teachingRateRepository.findAll().stream()
                .filter(TeachingRate::isActive)
                .forEach(rate -> {
                    rate.setActive(false);
                    teachingRateRepository.save(rate);
                });
        }
        return teachingRateRepository.save(newRate);
    }

    public Optional<TeachingRate> findById(Long id) {
        return teachingRateRepository.findById(id);
    }

    public void deleteById(Long id) {
        teachingRateRepository.deleteById(id);
    }

    public double calculateClassCoefficient(int numberOfStudents) {
        if (numberOfStudents < 20) return -0.3;
        if (numberOfStudents < 30) return -0.2;
        if (numberOfStudents < 40) return -0.1;
        if (numberOfStudents < 50) return 0.0;
        if (numberOfStudents < 60) return 0.1;
        if (numberOfStudents < 70) return 0.2;
        return 0.3;
    }
}
