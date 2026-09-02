package com.museumfinder.repo;

import com.museumfinder.domain.Museum;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MuseumRepository extends JpaRepository<Museum, Long>, JpaSpecificationExecutor<Museum> {

    @EntityGraph(attributePaths = {"themes", "openingHours"})
    Optional<Museum> findBySlug(String slug);

    @EntityGraph(attributePaths = {"themes", "openingHours"})
    List<Museum> findAllByIdIn(List<Long> ids);

    /**
     * Full-text candidate lookup against the maintained {@code search_vector} column.
     * {@code tsQuery} must already be a valid tsquery expression such as {@code art:* | tram:*}.
     */
    @Query(value = "SELECT id FROM museums WHERE search_vector @@ to_tsquery('simple', :tsQuery)",
           nativeQuery = true)
    List<Long> findIdsMatchingText(@Param("tsQuery") String tsQuery);
}
