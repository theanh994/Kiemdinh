package com.edumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edumana.model.Degree;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Long> {
    // Tìm kiếm bằng cấp theo tên đầy đủ (không phân biệt hoa thường)
    boolean existsByFullNameIgnoreCase(String fullName);
    
    // Tìm kiếm bằng cấp theo tên viết tắt (không phân biệt hoa thường)
    boolean existsByAbbreviationIgnoreCase(String abbreviation);
    
    // Tìm kiếm bằng cấp theo tên viết tắt (phân biệt hoa thường)
    boolean existsByAbbreviation(String abbreviation);
}
