package com.edumana.repository;

import com.edumana.model.TeachingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TeachingRateRepository extends JpaRepository<TeachingRate, Long> {
    
    @Query("SELECT tr FROM TeachingRate tr WHERE tr.active = true ORDER BY tr.startDate DESC LIMIT 1")
    Optional<TeachingRate> findCurrentRate();

    boolean existsByActive(boolean active);
}
