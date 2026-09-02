package com.museumfinder.web.dto;

import com.museumfinder.domain.OpeningHour;

import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

public record DayHoursDto(int day, String dayName, boolean closed, LocalTime opensAt, LocalTime closesAt) {
    public static DayHoursDto of(OpeningHour hour) {
        return new DayHoursDto(
                hour.getDayOfWeek(),
                hour.day().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                hour.isClosed(),
                hour.getOpensAt(),
                hour.getClosesAt());
    }
}
