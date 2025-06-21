package com.edumana.service;

import com.edumana.model.Department;
import com.edumana.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Lấy danh sách tất cả khoa
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    // Lấy thông tin khoa theo ID
    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    // Thêm mới khoa
    @Transactional
    public Department createDepartment(Department department) {
        validateDepartment(department);
        return departmentRepository.save(department);
    }

    // Cập nhật thông tin khoa
    @Transactional
    public Department updateDepartment(Long id, Department departmentDetails) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa với ID: " + id));

        // Chỉ kiểm tra trùng lặp nếu thông tin thay đổi
        if (!department.getFullName().equalsIgnoreCase(departmentDetails.getFullName()) || 
            !department.getAbbreviation().equalsIgnoreCase(departmentDetails.getAbbreviation())) {
            validateDepartment(departmentDetails);
        }

        department.setFullName(departmentDetails.getFullName());
        department.setAbbreviation(departmentDetails.getAbbreviation());
        department.setDescription(departmentDetails.getDescription());
        
        return departmentRepository.save(department);
    }

    // Xóa khoa
    @Transactional
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy khoa với ID: " + id);
        }
        departmentRepository.deleteById(id);
    }

    // Kiểm tra tính hợp lệ của thông tin khoa
    private void validateDepartment(Department department) {
        // Kiểm tra tên đầy đủ
        if (department.getFullName() == null || department.getFullName().trim().isEmpty()) {
            throw new RuntimeException("Tên đầy đủ của khoa không được để trống");
        }

        // Kiểm tra tên viết tắt
        if (department.getAbbreviation() == null || department.getAbbreviation().trim().isEmpty()) {
            throw new RuntimeException("Tên viết tắt của khoa không được để trống");
        }

        // Kiểm tra mô tả
        if (department.getDescription() == null || department.getDescription().trim().isEmpty()) {
            throw new RuntimeException("Mô tả nhiệm vụ của khoa không được để trống");
        }

        // Kiểm tra trùng lặp tên đầy đủ
        if (departmentRepository.existsByFullNameIgnoreCase(department.getFullName())) {
            throw new RuntimeException("Tên đầy đủ của khoa đã tồn tại");
        }

        // Kiểm tra trùng lặp tên viết tắt
        if (departmentRepository.existsByAbbreviationIgnoreCase(department.getAbbreviation())) {
            throw new RuntimeException("Tên viết tắt của khoa đã tồn tại");
        }
    }

    public Optional<Department> findById(Long id) {
        return departmentRepository.findById(id);
    }
}
