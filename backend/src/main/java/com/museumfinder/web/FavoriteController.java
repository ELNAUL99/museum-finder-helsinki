package com.museumfinder.web;

import com.museumfinder.domain.Favorite;
import com.museumfinder.domain.Museum;
import com.museumfinder.error.NotFoundException;
import com.museumfinder.repo.FavoriteRepository;
import com.museumfinder.repo.MuseumRepository;
import com.museumfinder.search.MuseumMapper;
import com.museumfinder.security.CurrentUser;
import com.museumfinder.web.dto.MuseumSummaryDto;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    private final FavoriteRepository favorites;
    private final MuseumRepository museums;
    private final MuseumMapper mapper;
    private final CurrentUser currentUser;
    private final Clock clock;

    public FavoriteController(FavoriteRepository favorites, MuseumRepository museums, MuseumMapper mapper,
                              CurrentUser currentUser, Clock clock) {
        this.favorites = favorites;
        this.museums = museums;
        this.mapper = mapper;
        this.currentUser = currentUser;
        this.clock = clock;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public List<MuseumSummaryDto> list() {
        Long userId = currentUser.require().id();
        Set<Long> ids = favorites.findByUserId(userId).stream()
                .map(Favorite::getMuseumId)
                .collect(Collectors.toSet());
        if (ids.isEmpty()) {
            return List.of();
        }
        LocalDate today = LocalDate.now(clock);
        return museums.findAllByIdIn(List.copyOf(ids)).stream()
                .map(m -> mapper.toSummary(m, today.getDayOfWeek(), null, List.of(), ids))
                .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
                .toList();
    }

    @PutMapping("/{museumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@PathVariable Long museumId) {
        Long userId = currentUser.require().id();
        Museum museum = museums.findById(museumId)
                .orElseThrow(() -> new NotFoundException("No museum with id " + museumId + "."));
        if (!favorites.existsByUserIdAndMuseumId(userId, museum.getId())) {
            favorites.save(new Favorite(userId, museum.getId()));
        }
    }

    @DeleteMapping("/{museumId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void remove(@PathVariable Long museumId) {
        favorites.deleteByUserIdAndMuseumId(currentUser.require().id(), museumId);
    }
}
