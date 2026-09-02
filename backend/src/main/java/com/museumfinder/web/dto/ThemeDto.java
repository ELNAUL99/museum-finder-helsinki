package com.museumfinder.web.dto;

import com.museumfinder.domain.Theme;

public record ThemeDto(String value, String label) {
    public static ThemeDto of(Theme theme) {
        return new ThemeDto(theme.name(), theme.label());
    }
}
