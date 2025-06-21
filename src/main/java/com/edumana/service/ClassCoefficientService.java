package com.edumana.service;

import com.edumana.model.ClassCoefficient;
import com.edumana.repository.ClassCoefficientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ClassCoefficientService {

    @Autowired
    private ClassCoefficientRepository classCoefficientRepository;

    public List<ClassCoefficient> getAllCoefficients() {
        return classCoefficientRepository.findAll();
    }

    public List<ClassCoefficient> getActiveCoefficients() {
        return classCoefficientRepository.findByActiveTrue();
    }

    public Optional<ClassCoefficient> findById(Long id) {
        return classCoefficientRepository.findById(id);
    }

    @Transactional
    public ClassCoefficient save(ClassCoefficient coefficient) {
        validateCoefficient(coefficient);
        return classCoefficientRepository.save(coefficient);
    }

    private void validateCoefficient(ClassCoefficient coefficient) {
        if (coefficient.getMinStudents() >= coefficient.getMaxStudents()) {
            throw new IllegalArgumentException(
                "Số sinh viên tối thiểu phải nhỏ hơn số sinh viên tối đa");
        }
    }

    public void deleteById(Long id) {
        classCoefficientRepository.deleteById(id);
    }

    public BigDecimal getCoefficientForStudentCount(int studentCount) {
        ClassCoefficient coefficient = classCoefficientRepository
            .findCoefficientForStudentCount(studentCount);
        return coefficient != null ? coefficient.getCoefficient() : BigDecimal.ZERO;
    }
}
