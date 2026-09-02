package com.museumfinder.search;

import com.museumfinder.domain.Theme;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HeuristicQueryInterpreterTest {

    private final HeuristicQueryInterpreter interpreter = new HeuristicQueryInterpreter();

    @Test
    void picksUpFreeEntryThemeDayAndPlace() {
        SearchFilters filters = interpreter.interpret("free art museums open on Sunday near Kamppi");

        assertThat(filters.freeOnly()).isTrue();
        assertThat(filters.themes()).contains(Theme.ART);
        assertThat(filters.openOn()).isEqualTo(DayFilter.SUNDAY);
        assertThat(filters.nearPlace()).isEqualTo("Kamppi");
    }

    @Test
    void resolvesLandmarkAliasesToTheirDistrict() {
        SearchFilters filters = interpreter.interpret("what is open right now near the central railway station");

        assertThat(filters.nearPlace()).isEqualTo("Central Railway Station");
        assertThat(filters.openNow()).isTrue();
        assertThat(filters.openOn()).isEqualTo(DayFilter.TODAY);
    }

    @Test
    void readsAPriceCeiling() {
        assertThat(interpreter.interpret("design museums under 15 euros").maxPriceEur()).isEqualTo(15);
        assertThat(interpreter.interpret("something cheap for the afternoon").maxPriceEur()).isEqualTo(12);
        assertThat(interpreter.interpret("design museums").maxPriceEur()).isEqualTo(-1);
    }

    /**
     * Regression: "6 year old" used to leak "year" into the keyword list, which then
     * matched every museum blurb containing "years" and emptied the results.
     */
    @Test
    void doesNotTurnFilterWordsIntoKeywords() {
        SearchFilters filters = interpreter.interpret("somewhere with dinosaurs for my 6 year old");

        assertThat(filters.themes()).contains(Theme.NATURAL_HISTORY);
        assertThat(filters.keywords()).doesNotContain("year", "old", "somewhere");
    }

    @Test
    void detectsAccessibilityAndFamilyNeeds() {
        SearchFilters filters = interpreter.interpret("wheelchair accessible museums with a cafe for kids");

        assertThat(filters.wheelchairAccessible()).isTrue();
        assertThat(filters.familyFriendly()).isTrue();
        assertThat(filters.hasCafe()).isTrue();
    }

    @Test
    void writesAnInterpretationForTheUi() {
        assertThat(interpreter.interpret("free museums").interpretation())
                .contains("free entry");
        assertThat(interpreter.interpret("").interpretation())
                .isEqualTo("Showing every museum in Helsinki.");
    }
}
