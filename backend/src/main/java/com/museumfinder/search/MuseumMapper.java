package com.museumfinder.search;

import com.museumfinder.domain.Exhibition;
import com.museumfinder.domain.Museum;
import com.museumfinder.domain.OpeningHour;
import com.museumfinder.web.dto.DayHoursDto;
import com.museumfinder.web.dto.ExhibitionDto;
import com.museumfinder.web.dto.MuseumDetailDto;
import com.museumfinder.web.dto.MuseumSummaryDto;
import com.museumfinder.web.dto.ThemeDto;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class MuseumMapper {

    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm");

    public MuseumSummaryDto toSummary(Museum museum, DayOfWeek today, Double distanceKm,
                                      List<String> matchedKeywords, Set<Long> favoriteIds) {
        Optional<OpeningHour> hoursToday = museum.getOpeningHours().stream()
                .filter(h -> h.getDayOfWeek() == today.getValue())
                .findFirst();

        boolean openToday = hoursToday.map(h -> !h.isClosed()).orElse(false);
        String todayHours = hoursToday
                .map(h -> h.isClosed() || h.getOpensAt() == null
                        ? "Closed today"
                        : h.getOpensAt().format(TIME) + "-" + h.getClosesAt().format(TIME))
                .orElse("Hours not listed");

        return new MuseumSummaryDto(
                museum.getId(),
                museum.getSlug(),
                museum.getName(),
                museum.getShortDescription(),
                museum.getDistrict(),
                museum.getAddress(),
                museum.getImageUrl(),
                museum.getAdultPriceEur(),
                museum.isFreeEntry(),
                museum.getFreeEntryNote(),
                museum.isMuseumCard(),
                museum.isWheelchairAccessible(),
                museum.isFamilyFriendly(),
                museum.isHasCafe(),
                museum.isHasShop(),
                museum.getLatitude(),
                museum.getLongitude(),
                museum.getThemes().stream().sorted().map(ThemeDto::of).toList(),
                openToday,
                todayHours,
                distanceKm,
                matchedKeywords,
                favoriteIds.contains(museum.getId()));
    }

    public MuseumDetailDto toDetail(Museum museum, List<Exhibition> exhibitions, LocalDate today, Set<Long> favoriteIds) {
        return new MuseumDetailDto(
                toSummary(museum, today.getDayOfWeek(), null, List.of(), favoriteIds),
                museum.getDescription(),
                museum.getPostalCode(),
                museum.getWebsite(),
                museum.getPhone(),
                museum.getEmail(),
                museum.getOpeningHours().stream()
                        .sorted(Comparator.comparingInt(OpeningHour::getDayOfWeek))
                        .map(DayHoursDto::of)
                        .toList(),
                exhibitions.stream().map(e -> ExhibitionDto.of(e, today)).toList());
    }
}
