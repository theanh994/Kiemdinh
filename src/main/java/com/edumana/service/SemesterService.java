package com.edumana.service;

import com.edumana.model.Semester;
import com.edumana.repository.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    // Lấy danh sách tất cả kỳ học
    public List<Semester> getAllSemesters() {
        return semesterRepository.findAll();
    }

    // Lấy thông tin kỳ học theo ID
    public Optional<Semester> getSemesterById(Long id) {
        return semesterRepository.findById(id);
    }

    public Optional<Semester> findById(Long id) {
        return semesterRepository.findById(id);
    }

    // Thêm mới kỳ học
    @Transactional
    public Semester createSemester(Semester semester) {
        validateSemester(semester);
        return semesterRepository.save(semester);
    }

    // Cập nhật thông tin kỳ học
    @Transactional
    public Semester updateSemester(Long id, Semester semesterDetails) {
        Semester semester = semesterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy kỳ học với ID: " + id));

        // Kiểm tra trùng lặp tên và năm học nếu có thay đổi
        if (!semester.getName().equals(semesterDetails.getName()) || 
            !semester.getAcademicYear().equals(semesterDetails.getAcademicYear())) {
            if (semesterRepository.existsByNameAndAcademicYear(
                    semesterDetails.getName(), semesterDetails.getAcademicYear())) {
                throw new RuntimeException("Kỳ học này đã tồn tại trong năm học " + 
                    semesterDetails.getAcademicYear());
            }
        }

        // Kiểm tra thời gian hợp lệ
        validateDateRange(semesterDetails);

        // Cập nhật thông tin
        semester.setName(semesterDetails.getName());
        semester.setAcademicYear(semesterDetails.getAcademicYear());
        semester.setStartDate(semesterDetails.getStartDate());
        semester.setEndDate(semesterDetails.getEndDate());
        
        return semesterRepository.save(semester);
    }

    // Xóa kỳ học
    @Transactional
    public void deleteSemester(Long id) {
        if (!semesterRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy kỳ học với ID: " + id);
        }
        semesterRepository.deleteById(id);
    }

    // Kiểm tra tính hợp lệ của thông tin kỳ học
    private void validateSemester(Semester semester) {
        // Kiểm tra tên kỳ học
        if (semester.getName() == null || semester.getName().trim().isEmpty()) {
            throw new RuntimeException("Tên kỳ học không được để trống");
        }

        // Kiểm tra năm học
        if (semester.getAcademicYear() == null || semester.getAcademicYear().trim().isEmpty()) {
            throw new RuntimeException("Năm học không được để trống");
        }

        // Kiểm tra định dạng năm học (VD: 2024-2025)
        if (!semester.getAcademicYear().matches("\\d{4}-\\d{4}")) {
            throw new RuntimeException("Năm học phải có định dạng YYYY-YYYY");
        }

        // Kiểm tra thời gian hợp lệ
        validateDateRange(semester);

        // Kiểm tra trùng lặp kỳ học trong năm
        if (semesterRepository.existsByNameAndAcademicYear(semester.getName(), semester.getAcademicYear())) {
            throw new RuntimeException("Kỳ học này đã tồn tại trong năm học " + semester.getAcademicYear());
        }
    }

    // Kiểm tra tính hợp lệ của thời gian
    private void validateDateRange(Semester semester) {
        if (semester.getStartDate() == null) {
            throw new RuntimeException("Ngày bắt đầu không được để trống");
        }

        if (semester.getEndDate() == null) {
            throw new RuntimeException("Ngày kết thúc không được để trống");
        }

        if (!semester.isValidDateRange()) {
            throw new RuntimeException("Ngày kết thúc phải sau ngày bắt đầu");
        }

        // Kiểm tra trùng lặp thời gian với các kỳ học khác
        List<Semester> overlappingSemesters = semesterRepository
            .findByStartDateLessThanEqualAndEndDateGreaterThanEqual(
                semester.getEndDate(), semester.getStartDate());

        boolean hasOverlap = overlappingSemesters.stream()
            .anyMatch(s -> !s.getId().equals(semester.getId())); // Bỏ qua kỳ học hiện tại khi cập nhật

        if (hasOverlap) {
            throw new RuntimeException("Thời gian của kỳ học bị trùng lặp với kỳ học khác");
        }
    }

    // Lấy danh sách kỳ học theo năm học
    public List<Semester> getSemestersByAcademicYear(String academicYear) {
        return semesterRepository.findByAcademicYearOrderByStartDateAsc(academicYear);
    }

    // Lấy danh sách kỳ học theo năm
    public List<Semester> findByYear(int year) {
        return semesterRepository.findByYear(year);
    }
}
