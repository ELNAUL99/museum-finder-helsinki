package com.museumfinder.search;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.museumfinder.domain.Theme;

import java.util.ArrayList;
import java.util.List;

/**
 * The structured form of a search. This is both the schema Claude fills in from a
 * natural-language question and the payload the frontend posts back when the user
 * edits a filter chip by hand - which is what keeps the AI layer transparent rather
 * than magic: whatever the model decided is visible and editable.
 *
 * <p>Every field is non-null with a neutral default so that the JSON schema handed to
 * the model has no optional fields to reason about.
 */
@JsonClassDescription("Structured museum search filters extracted from a visitor's question about museums in Helsinki.")
public record SearchFilters(

        @JsonPropertyDescription("Subject areas the visitor is interested in. Empty list means no subject constraint. Only use values from the enum.")
        List<Theme> themes,

        @JsonPropertyDescription("True only if the visitor explicitly wants museums that are free to enter.")
        boolean freeOnly,

        @JsonPropertyDescription("True only if the visitor mentions the Finnish Museum Card (Museokortti).")
        boolean museumCardOnly,

        @JsonPropertyDescription("Maximum adult ticket price in euros. Use -1 when the visitor gave no budget.")
        double maxPriceEur,

        @JsonPropertyDescription("Day the museum must be open. Use ANY unless the visitor named a day, 'today', 'tomorrow' or 'the weekend'.")
        DayFilter openOn,

        @JsonPropertyDescription("True only if the visitor wants somewhere open right now, at this moment.")
        boolean openNow,

        @JsonPropertyDescription("Helsinki district or landmark the visitor wants to be near, e.g. 'Kamppi', 'Kallio', 'Suomenlinna', 'Central Railway Station'. Empty string when no place was mentioned.")
        String nearPlace,

        @JsonPropertyDescription("Search radius in kilometres around nearPlace. Use 0 to accept the default of 2 km.")
        double radiusKm,

        @JsonPropertyDescription("True only if the visitor needs step-free or wheelchair access.")
        boolean wheelchairAccessible,

        @JsonPropertyDescription("True only if the visitor is visiting with children.")
        boolean familyFriendly,

        @JsonPropertyDescription("True only if the visitor asks for a cafe on site.")
        boolean hasCafe,

        @JsonPropertyDescription("Distinctive free-text words worth matching against museum names and descriptions, e.g. 'dinosaur', 'Aalto', 'tram'. Leave empty when the themes already cover the request. Never include stop words or the word 'museum'.")
        List<String> keywords,

        @JsonPropertyDescription("How to order the results.")
        SortOrder sort,

        @JsonPropertyDescription("One short sentence, addressed to the visitor, saying how the question was understood. No more than 20 words.")
        String interpretation
) {

    public static SearchFilters empty() {
        return new SearchFilters(List.of(), false, false, -1, DayFilter.ANY, false, "", 0,
                false, false, false, List.of(), SortOrder.RELEVANCE, "");
    }

    /** Null-safe copy; used on anything arriving from the model or the browser. */
    public SearchFilters normalized() {
        List<Theme> safeThemes = themes == null ? List.of() : themes.stream().filter(java.util.Objects::nonNull).distinct().toList();
        List<String> safeKeywords = new ArrayList<>();
        if (keywords != null) {
            for (String k : keywords) {
                if (k != null && !k.isBlank()) {
                    safeKeywords.add(k.trim());
                }
            }
        }
        return new SearchFilters(
                safeThemes,
                freeOnly,
                museumCardOnly,
                maxPriceEur <= 0 ? -1 : maxPriceEur,
                openOn == null ? DayFilter.ANY : openOn,
                openNow,
                nearPlace == null ? "" : nearPlace.trim(),
                radiusKm <= 0 ? 0 : Math.min(radiusKm, 30),
                wheelchairAccessible,
                familyFriendly,
                hasCafe,
                List.copyOf(safeKeywords),
                sort == null ? SortOrder.RELEVANCE : sort,
                interpretation == null ? "" : interpretation.trim());
    }

    public boolean hasPlace() {
        return nearPlace != null && !nearPlace.isBlank();
    }

    public boolean hasPriceCap() {
        return maxPriceEur >= 0;
    }

    public SearchFilters withInterpretation(String text) {
        return new SearchFilters(themes, freeOnly, museumCardOnly, maxPriceEur, openOn, openNow, nearPlace,
                radiusKm, wheelchairAccessible, familyFriendly, hasCafe, keywords, sort, text);
    }
}
