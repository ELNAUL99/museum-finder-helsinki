package com.museumfinder.web;

import com.museumfinder.domain.Museum;
import com.museumfinder.error.NotFoundException;
import com.museumfinder.repo.ExhibitionRepository;
import com.museumfinder.repo.MuseumRepository;
import com.museumfinder.search.MuseumMapper;
import com.museumfinder.security.CurrentUser;
import com.museumfinder.web.dto.ExhibitionDto;
import com.museumfinder.web.dto.MuseumDetailDto;
import com.museumfinder.web.dto.MuseumSummaryDto;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class MuseumController {

    private final MuseumRepository museums;
    private final ExhibitionRepository exhibitions;
    private final MuseumMapper mapper;
    private final CurrentUser currentUser;
    private final Clock clock;

    public MuseumController(MuseumRepository museums, ExhibitionRepository exhibitions, MuseumMapper mapper,
                            CurrentUser currentUser, Clock clock) {
        this.museums = museums;
        this.exhibitions = exhibitions;
        this.mapper = mapper;
        this.currentUser = currentUser;
        this.clock = clock;
    }

    @GetMapping("/museums")
    @Transactional(readOnly = true)
    public List<MuseumSummaryDto> all() {
        LocalDate today = LocalDate.now(clock);
        Set<Long> favorites = currentUser.favoriteIds();
        List<Long> ids = museums.findAll(Sort.by("name")).stream().map(Museum::getId).toList();
        return museums.findAllByIdIn(ids).stream()
                .map(m -> mapper.toSummary(m, today.getDayOfWeek(), null, List.of(), favorites))
                .sorted((a, b) -> a.name().compareToIgnoreCase(b.name()))
                .toList();
    }

    @GetMapping("/museums/{slug}")
    @Transactional(readOnly = true)
    public MuseumDetailDto bySlug(@PathVariable String slug) {
        Museum museum = museums.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("No museum with slug '" + slug + "'."));
        return mapper.toDetail(museum,
                exhibitions.findByMuseumIdOrderByStartDateDesc(museum.getId()),
                LocalDate.now(clock),
                currentUser.favoriteIds());
    }

    /** Everything on show across the city today - the "what's on" view. */
    @GetMapping("/exhibitions/current")
    @Transactional(readOnly = true)
    public List<ExhibitionDto> currentExhibitions() {
        LocalDate today = LocalDate.now(clock);
        return exhibitions.findRunningOn(today).stream()
                .map(e -> ExhibitionDto.of(e, today))
                .toList();
    }
}
