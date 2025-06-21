package com.edumana.repository;

import com.edumana.model.ClassCoefficient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ClassCoefficientRepository extends JpaRepository<ClassCoefficient, Long> {
    
    @Query("SELECT cc FROM ClassCoefficient cc WHERE cc.active = true " +
           "AND cc.minStudents <= :studentCount AND cc.maxStudents >= :studentCount")
    ClassCoefficient findCoefficientForStudentCount(@Param("studentCount") int studentCount);

    List<ClassCoefficient> findByActiveTrue();
}
