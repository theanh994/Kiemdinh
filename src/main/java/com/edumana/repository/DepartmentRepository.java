package com.edumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edumana.model.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Kiểm tra tên đầy đủ đã tồn tại
    boolean existsByFullNameIgnoreCase(String fullName);
    
    // Kiểm tra tên viết tắt đã tồn tại
    boolean existsByAbbreviationIgnoreCase(String abbreviation);
}
