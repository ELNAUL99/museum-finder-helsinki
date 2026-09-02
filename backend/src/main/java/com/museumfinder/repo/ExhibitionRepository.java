package com.museumfinder.repo;

import com.museumfinder.domain.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    List<Exhibition> findByMuseumIdOrderByStartDateDesc(Long museumId);

    @Query("""
            SELECT e FROM Exhibition e
            JOIN FETCH e.museum
            WHERE e.startDate <= :date AND (e.endDate IS NULL OR e.endDate >= :date)
            ORDER BY e.permanent ASC, e.endDate ASC NULLS LAST
            """)
    List<Exhibition> findRunningOn(@Param("date") LocalDate date);
}
