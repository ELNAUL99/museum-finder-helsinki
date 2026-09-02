package com.museumfinder.repo;

import com.museumfinder.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Favorite.Key> {
    List<Favorite> findByUserId(Long userId);
    boolean existsByUserIdAndMuseumId(Long userId, Long museumId);
    void deleteByUserIdAndMuseumId(Long userId, Long museumId);
}
