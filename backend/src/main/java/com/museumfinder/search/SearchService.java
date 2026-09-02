package com.museumfinder.search;

import com.museumfinder.domain.Museum;
import com.museumfinder.repo.MuseumRepository;
import com.museumfinder.web.dto.MuseumSummaryDto;
import com.museumfinder.web.dto.SearchResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
public class SearchService {

    static final double DEFAULT_RADIUS_KM = 2.0;

    private static final Logger log = LoggerFactory.getLogger(SearchService.class);

    private final MuseumRepository museums;
    private final MuseumMapper mapper;
    private final ClaudeQueryInterpreter claude;
    private final HeuristicQueryInterpreter heuristic;
    private final Clock clock;

    public SearchService(MuseumRepository museums, MuseumMapper mapper, ClaudeQueryInterpreter claude,
                         HeuristicQueryInterpreter heuristic, Clock clock) {
        this.museums = museums;
        this.mapper = mapper;
        this.claude = claude;
        this.heuristic = heuristic;
        this.clock = clock;
    }

    /** Natural-language entry point: interpret first, then run the structured search. */
    public SearchResponseDto searchByQuestion(String question, Set<Long> favoriteIds) {
        if (question == null || question.isBlank()) {
            return search(SearchFilters.empty(), "", "filters", favoriteIds);
        }
        String interpretedBy = heuristic.id();
        SearchFilters filters;
        if (claude.isAvailable()) {
            try {
                filters = claude.interpret(question);
                interpretedBy = claude.id();
            } catch (RuntimeException e) {
                log.warn("Claude interpretation failed for \"{}\" ({}); using keyword fallback.",
                        question, e.getMessage());
                filters = heuristic.interpret(question);
            }
        } else {
            filters = heuristic.interpret(question);
        }
        return search(filters, question, interpretedBy, favoriteIds);
    }

    @Transactional(readOnly = true)
    public SearchResponseDto search(SearchFilters rawFilters, String question, String interpretedBy, Set<Long> favoriteIds) {
        SearchFilters filters = rawFilters.normalized();
        LocalDate today = LocalDate.now(clock);
        LocalTime now = LocalTime.now(clock);
        Optional<HelsinkiPlaces.Place> place = filters.hasPlace()
                ? HelsinkiPlaces.resolve(filters.nearPlace())
                : Optional.empty();

        Optional<Collection<Long>> textIds = Optional.empty();
        if (!filters.keywords().isEmpty()) {
            String tsQuery = toTsQuery(filters.keywords());
            if (!tsQuery.isBlank()) {
                List<Long> ids = museums.findIdsMatchingText(tsQuery);
                // A keyword nobody matches should narrow nothing rather than empty the page.
                if (!ids.isEmpty()) {
                    textIds = Optional.of(ids);
                }
            }
        }

        List<Scored> scored = run(filters, today, now, place, textIds);

        // Keywords are the softest signal we have. If they are what emptied the page,
        // drop them and say so, rather than showing the visitor nothing.
        String note = null;
        if (scored.isEmpty() && textIds.isPresent()) {
            scored = run(filters, today, now, place, Optional.empty());
            if (!scored.isEmpty()) {
                note = "No museum matched " + String.join(", ", filters.keywords())
                        + " together with the other filters, so that word was ignored.";
            }
        }

        scored.sort(comparator(filters));

        List<MuseumSummaryDto> results = scored.stream()
                .map(s -> mapper.toSummary(s.museum(), today.getDayOfWeek(), s.distanceKm(), s.matched(), favoriteIds))
                .toList();

        return new SearchResponseDto(question, filters, interpretedBy, results.size(), results, note);
    }

    /** Runs the SQL half of the search and scores what comes back. */
    private List<Scored> run(SearchFilters filters, LocalDate today, LocalTime now,
                             Optional<HelsinkiPlaces.Place> place, Optional<Collection<Long>> textIds) {
        List<Museum> candidates = museums.findAll(
                MuseumSpecifications.from(filters, today.getDayOfWeek(), now, place, textIds));

        // Re-read with collections attached so the mapper never triggers a query per row.
        List<Long> ids = candidates.stream().map(Museum::getId).toList();
        List<Museum> loaded = ids.isEmpty() ? List.of() : museums.findAllByIdIn(ids);

        double radius = filters.radiusKm() > 0 ? filters.radiusKm() : DEFAULT_RADIUS_KM;
        List<Scored> scored = new ArrayList<>();
        for (Museum museum : loaded) {
            Double distance = place
                    .map(p -> GeoUtil.distanceKm(p.latitude(), p.longitude(), museum.getLatitude(), museum.getLongitude()))
                    .orElse(null);
            if (distance != null && distance > radius) {
                continue;
            }
            scored.add(new Scored(museum, distance, matchedKeywords(museum, filters.keywords())));
        }
        return scored;
    }

    private Comparator<Scored> comparator(SearchFilters filters) {
        Comparator<Scored> byName = Comparator.comparing(s -> s.museum().getName(), String.CASE_INSENSITIVE_ORDER);
        Comparator<Scored> byDistance = Comparator.comparing(
                s -> s.distanceKm() == null ? Double.MAX_VALUE : s.distanceKm());
        Comparator<Scored> byPrice = Comparator.comparing(s -> s.museum().getAdultPriceEur());

        return switch (filters.sort()) {
            case DISTANCE -> byDistance.thenComparing(byName);
            case PRICE_ASC -> byPrice.thenComparing(byName);
            case NAME -> byName;
            case RELEVANCE -> Comparator
                    .comparingInt((Scored s) -> -s.matched().size())
                    .thenComparing(byDistance)
                    .thenComparing(byName);
        };
    }

    private static List<String> matchedKeywords(Museum museum, List<String> keywords) {
        if (keywords.isEmpty()) {
            return List.of();
        }
        String haystack = (museum.getName() + " " + museum.getShortDescription() + " "
                + museum.getDescription() + " " + museum.getDistrict()).toLowerCase(Locale.ROOT);
        return keywords.stream()
                .filter(k -> haystack.contains(k.toLowerCase(Locale.ROOT)))
                .toList();
    }

    /** Builds a safe prefix-matching tsquery such as {@code aalto:* | tram:*}. */
    static String toTsQuery(List<String> keywords) {
        return keywords.stream()
                .map(k -> k.replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", ""))
                .filter(k -> k.length() >= 2)
                .map(k -> k.toLowerCase(Locale.ROOT) + ":*")
                .distinct()
                .reduce((a, b) -> a + " | " + b)
                .orElse("");
    }

    private record Scored(Museum museum, Double distanceKm, List<String> matched) {
    }
}
