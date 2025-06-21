package com.edumana.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.edumana.model.Degree;
import com.edumana.model.Department;
import com.edumana.model.Teacher;
import com.edumana.repository.TeacherRepository;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    // Lấy danh sách tất cả giáo viên
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    // Lấy thông tin giáo viên theo ID
    public Optional<Teacher> getTeacherById(Long id) {
        return teacherRepository.findById(id);
    }

    public Optional<Teacher> findById(Long id) {
        return teacherRepository.findById(id);
    }

    // Tạo mã giáo viên tự động
    private String generateTeacherCode() {
        long count = teacherRepository.countBy() + 1;
        return String.format("GV%05d", count); // Tạo mã dạng GV00001, GV00002,...
    }

    // Thêm mới giáo viên
    @Transactional
    public Teacher createTeacher(Teacher teacher) {
        // Nếu không có mã giáo viên, tự động tạo mã
        if (teacher.getTeacherCode() == null || teacher.getTeacherCode().trim().isEmpty()) {
            teacher.setTeacherCode(generateTeacherCode());
        }
        validateTeacher(teacher);
        return teacherRepository.save(teacher);
    }

    // Cập nhật thông tin giáo viên
    @Transactional
    public Teacher updateTeacher(Long id, Teacher teacherDetails) {
        Teacher teacher = teacherRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy giáo viên với ID: " + id));

        // Kiểm tra email và số điện thoại nếu thay đổi
        if (!teacher.getEmail().equals(teacherDetails.getEmail()) && 
            teacherRepository.existsByEmail(teacherDetails.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        }
        if (!teacher.getPhone().equals(teacherDetails.getPhone()) && 
            teacherRepository.existsByPhone(teacherDetails.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống");
        }

        // Cập nhật thông tin
        teacher.setFullName(teacherDetails.getFullName());
        teacher.setBirthDate(teacherDetails.getBirthDate());
        teacher.setPhone(teacherDetails.getPhone());
        teacher.setEmail(teacherDetails.getEmail());
        teacher.setDepartment(teacherDetails.getDepartment());
        teacher.setDegree(teacherDetails.getDegree());
        
        return teacherRepository.save(teacher);
    }

    // Xóa giáo viên
    @Transactional
    public void deleteTeacher(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy giáo viên với ID: " + id);
        }
        teacherRepository.deleteById(id);
    }

    // Kiểm tra tính hợp lệ của thông tin giáo viên
    private void validateTeacher(Teacher teacher) {
        // Kiểm tra họ tên
        if (teacher.getFullName() == null || teacher.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Họ tên giáo viên không được để trống");
        }

        // Kiểm tra ngày sinh
        if (teacher.getBirthDate() == null) {
            throw new RuntimeException("Ngày sinh không được để trống");
        }

        // Kiểm tra số điện thoại
        if (teacher.getPhone() == null || !teacher.getPhone().matches("\\d{10}")) {
            throw new RuntimeException("Số điện thoại không hợp lệ (phải có 10 chữ số)");
        }

        // Kiểm tra email
        if (teacher.getEmail() == null || !teacher.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new RuntimeException("Email không hợp lệ");
        }

        // Kiểm tra khoa
        if (teacher.getDepartment() == null) {
            throw new RuntimeException("Vui lòng chọn khoa");
        }

        // Kiểm tra bằng cấp
        if (teacher.getDegree() == null) {
            throw new RuntimeException("Vui lòng chọn bằng cấp");
        }

        // Kiểm tra trùng lặp email
        if (teacherRepository.existsByEmail(teacher.getEmail())) {
            throw new RuntimeException("Email đã tồn tại trong hệ thống");
        }

        // Kiểm tra trùng lặp số điện thoại
        if (teacherRepository.existsByPhone(teacher.getPhone())) {
            throw new RuntimeException("Số điện thoại đã tồn tại trong hệ thống");
        }

        // Kiểm tra trùng lặp mã giáo viên
        if (teacherRepository.existsByTeacherCode(teacher.getTeacherCode())) {
            throw new RuntimeException("Mã giáo viên đã tồn tại trong hệ thống");
        }
    }

    // Thống kê số lượng giáo viên
    public long getTotalTeachers() {
        return teacherRepository.count();
    }

    // Thống kê giáo viên theo khoa
    public Map<String, Long> getTeachersByDepartment() {
        Map<String, Long> stats = new HashMap<>();
        List<Teacher> teachers = teacherRepository.findAll();
        
        stats = teachers.stream()
            .collect(Collectors.groupingBy(
                t -> t.getDepartment().getFullName(),
                Collectors.counting()
            ));
        
        return stats;
    }

    // Thống kê giáo viên theo bằng cấp
    public Map<String, Long> getTeachersByDegree() {
        Map<String, Long> stats = new HashMap<>();
        List<Teacher> teachers = teacherRepository.findAll();
        
        stats = teachers.stream()
            .collect(Collectors.groupingBy(
                t -> t.getDegree().getFullName(),
                Collectors.counting()
            ));
        
        return stats;
    }

    // Lấy danh sách giáo viên theo bằng cấp
    public List<Teacher> findByDegree(Degree degree) {
        return teacherRepository.findByDegree(degree);
    }

    // Lấy danh sách giáo viên theo bộ môn
    public List<Teacher> findByDepartment(Department department) {
        return teacherRepository.findByDepartment(department);
    }
}
