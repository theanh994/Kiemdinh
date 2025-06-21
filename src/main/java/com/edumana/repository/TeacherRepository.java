package com.edumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.edumana.model.Teacher;
import com.edumana.model.Department;
import com.edumana.model.Degree;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByTeacherCode(String teacherCode);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Long countBy(); // Đếm số lượng giáo viên để tạo mã số

    // Thống kê theo khoa
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department.id = :departmentId")
    Long countByDepartmentId(Long departmentId);

    // Thống kê theo bằng cấp
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.degree.id = :degreeId")
    Long countByDegreeId(Long degreeId);

    // Thống kê theo khoa và bằng cấp
    @Query("SELECT COUNT(t) FROM Teacher t WHERE t.department.id = :departmentId AND t.degree.id = :degreeId")
    Long countByDepartmentIdAndDegreeId(Long departmentId, Long degreeId);

    List<Teacher> findByDepartment(Department department);
    List<Teacher> findByDegree(Degree degree);
}
