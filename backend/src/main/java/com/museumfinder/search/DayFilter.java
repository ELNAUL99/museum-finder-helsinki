package com.museumfinder.search;

import java.time.DayOfWeek;
import java.util.List;

/** Which day the visitor wants the museum to be open. */
public enum DayFilter {
    ANY,
    TODAY,
    TOMORROW,
    WEEKEND,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    /** The concrete ISO day numbers this filter requires, relative to {@code today}. Empty means "no constraint". */
    public List<Integer> isoDays(DayOfWeek today) {
        return switch (this) {
            case ANY -> List.of();
            case TODAY -> List.of(today.getValue());
            case TOMORROW -> List.of(today.plus(1).getValue());
            case WEEKEND -> List.of(DayOfWeek.SATURDAY.getValue(), DayOfWeek.SUNDAY.getValue());
            case MONDAY -> List.of(DayOfWeek.MONDAY.getValue());
            case TUESDAY -> List.of(DayOfWeek.TUESDAY.getValue());
            case WEDNESDAY -> List.of(DayOfWeek.WEDNESDAY.getValue());
            case THURSDAY -> List.of(DayOfWeek.THURSDAY.getValue());
            case FRIDAY -> List.of(DayOfWeek.FRIDAY.getValue());
            case SATURDAY -> List.of(DayOfWeek.SATURDAY.getValue());
            case SUNDAY -> List.of(DayOfWeek.SUNDAY.getValue());
        };
    }

    public String label(DayOfWeek today) {
        return switch (this) {
            case ANY -> "any day";
            case TODAY -> "today";
            case TOMORROW -> "tomorrow";
            case WEEKEND -> "the weekend";
            default -> name().charAt(0) + name().substring(1).toLowerCase();
        };
    }
}
