package com.edumana.service;

import com.edumana.model.Course;
import com.edumana.model.CourseClass;
import com.edumana.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseClassService courseClassService;

    // Lấy danh sách tất cả học phần
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Lấy thông tin học phần theo ID
    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    // Tạo mã học phần tự động
    private String generateCourseCode() {
        long count = courseRepository.countBy() + 1;
        return String.format("HP%05d", count); // Tạo mã dạng HP00001, HP00002,...
    }

    // Thêm mới học phần
    @Transactional
    public Course createCourse(Course course) {
        // Nếu không có mã học phần, tự động tạo mã
        if (course.getCourseCode() == null || course.getCourseCode().trim().isEmpty()) {
            course.setCourseCode(generateCourseCode());
        }
        validateCourse(course);
        return courseRepository.save(course);
    }

    // Cập nhật thông tin học phần
    @Transactional
    public Course updateCourse(Long id, Course courseDetails) {
        Course course = courseRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần với ID: " + id));

        // Kiểm tra trùng tên nếu tên thay đổi
        if (!course.getName().equals(courseDetails.getName()) && 
            courseRepository.existsByName(courseDetails.getName())) {
            throw new RuntimeException("Tên học phần đã tồn tại");
        }

        // Cập nhật thông tin
        course.setName(courseDetails.getName());
        course.setCredits(courseDetails.getCredits());
        course.setCoefficient(courseDetails.getCoefficient());
        course.setPeriods(courseDetails.getPeriods());
        
        return courseRepository.save(course);
    }

    // Xóa học phần
    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy học phần với ID: " + id);
        }
        courseRepository.deleteById(id);
    }

    // Kiểm tra tính hợp lệ của thông tin học phần
    private void validateCourse(Course course) {
        // Kiểm tra khoa quản lý
        if (course.getDepartment() == null) {
            throw new RuntimeException("Vui lòng chọn khoa quản lý");
        }

        // Kiểm tra tên học phần
        if (course.getName() == null || course.getName().trim().isEmpty()) {
            throw new RuntimeException("Tên học phần không được để trống");
        }

        // Kiểm tra số tín chỉ
        if (course.getCredits() == null || course.getCredits() < 1) {
            throw new RuntimeException("Số tín chỉ phải lớn hơn 0");
        }

        // Kiểm tra hệ số học phần
        if (course.getCoefficient() == null || course.getCoefficient() <= 0) {
            throw new RuntimeException("Hệ số học phần phải lớn hơn 0");
        }

        // Kiểm tra số tiết
        if (course.getPeriods() == null || course.getPeriods() < 1) {
            throw new RuntimeException("Số tiết phải lớn hơn 0");
        }

        // Kiểm tra trùng mã học phần
        if (course.getId() == null && courseRepository.existsByCourseCode(course.getCourseCode())) {
            throw new RuntimeException("Mã học phần đã tồn tại");
        }

        // Kiểm tra trùng tên học phần
        if (course.getId() == null && courseRepository.existsByName(course.getName())) {
            throw new RuntimeException("Tên học phần đã tồn tại");
        }
    }

    public List<Course> findRecentCoursesByDepartment(Long departmentId, LocalDate since) {
        // Convert LocalDate to LocalDateTime (start of day)
        LocalDateTime sinceDateTime = since.atStartOfDay();
        return courseRepository.findAll().stream()
            .filter(course -> course.getDepartment() != null 
                && course.getDepartment().getId().equals(departmentId)
                && course.getCreatedAt() != null 
                && course.getCreatedAt().isAfter(sinceDateTime))
            .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
            .collect(Collectors.toList());
    }

    public List<Course> findOlderCoursesByDepartment(Long departmentId, LocalDate before) {
        // Convert LocalDate to LocalDateTime (end of day)
        LocalDateTime beforeDateTime = before.atTime(LocalTime.MAX);
        return courseRepository.findAll().stream()
            .filter(course -> course.getDepartment() != null 
                && course.getDepartment().getId().equals(departmentId)
                && (course.getCreatedAt() == null || course.getCreatedAt().isBefore(beforeDateTime)))
            .sorted((c1, c2) -> {
                if (c1.getCreatedAt() == null) return 1;
                if (c2.getCreatedAt() == null) return -1;
                return c2.getCreatedAt().compareTo(c1.getCreatedAt());
            })
            .collect(Collectors.toList());
    }

    public List<Course> findRecentCourses(LocalDate since) {
        // Convert LocalDate to LocalDateTime (start of day)
        LocalDateTime sinceDateTime = since.atStartOfDay();
        return courseRepository.findAll().stream()
            .filter(course -> course.getCreatedAt() != null 
                && course.getCreatedAt().isAfter(sinceDateTime))
            .sorted((c1, c2) -> c2.getCreatedAt().compareTo(c1.getCreatedAt()))
            .collect(Collectors.toList());
    }

    public List<Course> findOlderCourses(LocalDate before) {
        // Convert LocalDate to LocalDateTime (end of day)
        LocalDateTime beforeDateTime = before.atTime(LocalTime.MAX);
        return courseRepository.findAll().stream()
            .filter(course -> course.getCreatedAt() == null 
                || course.getCreatedAt().isBefore(beforeDateTime))
            .sorted((c1, c2) -> {
                if (c1.getCreatedAt() == null) return 1;
                if (c2.getCreatedAt() == null) return -1;
                return c2.getCreatedAt().compareTo(c1.getCreatedAt());
            })
            .collect(Collectors.toList());
    }    public Map<String, Integer> getStatistics(Long courseId) {
        Course course = courseRepository.findById(courseId)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy học phần"));

        List<CourseClass> classes = courseClassService.findByCourse(course);
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalClasses", classes.size());
        
        // Calculate total students only if there are classes
        if (!classes.isEmpty()) {
            int totalStudents = classes.stream()
                .mapToInt(CourseClass::getMaxStudents)
                .sum();
            stats.put("totalStudents", totalStudents);
        }
        
        return stats;
    }
}
