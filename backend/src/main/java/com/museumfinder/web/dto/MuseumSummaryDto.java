package com.museumfinder.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record MuseumSummaryDto(
        Long id,
        String slug,
        String name,
        String shortDescription,
        String district,
        String address,
        String imageUrl,
        String imageUrlLarge,
        ImageCreditDto imageCredit,
        BigDecimal adultPriceEur,
        boolean freeEntry,
        String freeEntryNote,
        boolean museumCard,
        boolean wheelchairAccessible,
        boolean familyFriendly,
        boolean hasCafe,
        boolean hasShop,
        double latitude,
        double longitude,
        List<ThemeDto> themes,
        boolean openToday,
        String todayHours,
        Double distanceKm,
        List<String> matchedKeywords,
        boolean favorite) {
}
