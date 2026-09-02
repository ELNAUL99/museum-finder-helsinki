package com.museumfinder.web.dto;

import com.museumfinder.domain.Exhibition;

import java.time.LocalDate;

public record ExhibitionDto(Long id, String title, String description, LocalDate startDate, LocalDate endDate,
                            String imageUrl, boolean permanent, boolean running) {
    public static ExhibitionDto of(Exhibition e, LocalDate today) {
        return new ExhibitionDto(e.getId(), e.getTitle(), e.getDescription(), e.getStartDate(), e.getEndDate(),
                e.getImageUrl(), e.isPermanent(), e.isRunningOn(today));
    }
}
