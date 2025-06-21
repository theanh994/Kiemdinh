package com.edumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edumana.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    boolean existsByCourseCode(String courseCode);
    boolean existsByName(String name);
    Long countBy(); // Đếm số lượng học phần để tạo mã số
}
