package com.museumfinder.search;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SearchFiltersTest {

    @Test
    void normalizedSurvivesNullsFromTheModelOrTheBrowser() {
        SearchFilters raw = new SearchFilters(null, false, false, 0, null, false, null, -5,
                false, false, false, Arrays.asList(null, "  ", " Aalto "), null, null);

        SearchFilters clean = raw.normalized();

        assertThat(clean.themes()).isEmpty();
        assertThat(clean.keywords()).containsExactly("Aalto");
        assertThat(clean.openOn()).isEqualTo(DayFilter.ANY);
        assertThat(clean.sort()).isEqualTo(SortOrder.RELEVANCE);
        assertThat(clean.nearPlace()).isEmpty();
        assertThat(clean.maxPriceEur()).isEqualTo(-1);
        assertThat(clean.radiusKm()).isZero();
    }

    @Test
    void radiusIsCappedSoOneBadNumberCannotSelectTheWholeCountry() {
        assertThat(SearchFilters.empty().normalized().radiusKm()).isZero();
        SearchFilters wide = new SearchFilters(List.of(), false, false, -1, DayFilter.ANY, false, "Kamppi", 900,
                false, false, false, List.of(), SortOrder.RELEVANCE, "");
        assertThat(wide.normalized().radiusKm()).isEqualTo(30);
    }

    @Test
    void keywordsBecomeASafePrefixTsQuery() {
        List<String> keywords = new ArrayList<>(List.of("Aalto", "tram;DROP TABLE", "a"));
        assertThat(SearchService.toTsQuery(keywords)).isEqualTo("aalto:* | tramdroptable:*");
        assertThat(SearchService.toTsQuery(List.of())).isEmpty();
    }
}
