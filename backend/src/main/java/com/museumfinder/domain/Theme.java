package com.museumfinder.domain;

import java.util.Arrays;
import java.util.Optional;

/**
 * Controlled vocabulary for what a museum is about. The database stores the
 * lowercase form (see {@link ThemeConverter}); the AI query interpreter is given
 * the same list so it can only ever produce themes that exist.
 */
public enum Theme {
    ART,
    MODERN_ART,
    CLASSICAL_ART,
    DESIGN,
    ARCHITECTURE,
    PHOTOGRAPHY,
    HISTORY,
    CITY_HISTORY,
    NATURAL_HISTORY,
    SCIENCE,
    TECHNOLOGY,
    MARITIME,
    MILITARY,
    OPEN_AIR,
    CHILDREN,
    SPORTS,
    MUSIC,
    THEATRE,
    CULTURE,
    ASTRONOMY,
    BOTANY;

    public String dbValue() {
        return name().toLowerCase();
    }

    /** Human-readable label, e.g. {@code MODERN_ART -> "Modern art"}. */
    public String label() {
        String words = name().toLowerCase().replace('_', ' ');
        return Character.toUpperCase(words.charAt(0)) + words.substring(1);
    }

    public static Optional<Theme> fromDbValue(String value) {
        if (value == null) {
            return Optional.empty();
        }
        return Arrays.stream(values())
                .filter(t -> t.dbValue().equalsIgnoreCase(value.trim()))
                .findFirst();
    }
}
