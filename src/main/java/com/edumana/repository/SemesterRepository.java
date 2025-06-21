package com.edumana.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edumana.model.Semester;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    // Kiểm tra trùng lặp tên kỳ học trong cùng năm học
    boolean existsByNameAndAcademicYear(String name, String academicYear);
    
    // Tìm các kỳ học có thời gian trùng lặp
    List<Semester> findByStartDateLessThanEqualAndEndDateGreaterThanEqual(LocalDate endDate, LocalDate startDate);
    
    // Lấy danh sách kỳ học theo năm học
    List<Semester> findByAcademicYearOrderByStartDateAsc(String academicYear);
    
    // Lấy danh sách kỳ học theo năm
    @Query("SELECT s FROM Semester s WHERE YEAR(s.startDate) = :year OR YEAR(s.endDate) = :year")
    List<Semester> findByYear(@Param("year") int year);
}
