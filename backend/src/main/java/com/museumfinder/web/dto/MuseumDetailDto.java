package com.museumfinder.web.dto;

import java.util.List;

public record MuseumDetailDto(
        MuseumSummaryDto summary,
        String description,
        String postalCode,
        String website,
        String phone,
        String email,
        List<DayHoursDto> openingHours,
        List<ExhibitionDto> exhibitions) {
}
