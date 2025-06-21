package com.edumana.service;

import com.edumana.model.Degree;
import com.edumana.repository.DegreeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DegreeService {

    @Autowired
    private DegreeRepository degreeRepository;

    // Lấy danh sách tất cả bằng cấp
    public List<Degree> getAllDegrees() {
        return degreeRepository.findAll();
    }

    // Lấy thông tin bằng cấp theo ID
    public Optional<Degree> getDegreeById(Long id) {
        return degreeRepository.findById(id);
    }

    // Thêm mới bằng cấp
    public Degree save(Degree degree) {
        return degreeRepository.save(degree);
    }

    // Xóa bằng cấp
    public void deleteById(Long id) {
        degreeRepository.deleteById(id);
    }

    // Alias for getDegreeById to maintain consistency with other services
    public Optional<Degree> findById(Long id) {
        return getDegreeById(id);
    }

    // Kiểm tra tính hợp lệ của thông tin bằng cấp
    private void validateDegree(Degree degree) {
        // Kiểm tra tên đầy đủ
        if (degree.getFullName() == null || degree.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Tên đầy đủ của bằng cấp không được để trống");
        }

        // Kiểm tra tên viết tắt
        if (degree.getAbbreviation() == null || degree.getAbbreviation().trim().isEmpty()) {
            throw new RuntimeException("Tên viết tắt của bằng cấp không được để trống");
        }

        // Kiểm tra trùng lặp tên đầy đủ
        if (degreeRepository.existsByFullNameIgnoreCase(degree.getFullName())) {
            throw new RuntimeException("Tên đầy đủ của bằng cấp đã tồn tại");
        }

        // Kiểm tra trùng lặp tên viết tắt
        if (degreeRepository.existsByAbbreviationIgnoreCase(degree.getAbbreviation())) {
            throw new RuntimeException("Tên viết tắt của bằng cấp đã tồn tại");
        }
    }

    public boolean existsByAbbreviation(String abbreviation) {
        return degreeRepository.existsByAbbreviation(abbreviation);
    }
}
