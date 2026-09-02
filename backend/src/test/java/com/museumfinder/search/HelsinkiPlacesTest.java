package com.museumfinder.search;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelsinkiPlacesTest {

    @Test
    void resolvesCanonicalNamesAndAliases() {
        assertThat(HelsinkiPlaces.resolve("Kamppi")).isPresent();
        assertThat(HelsinkiPlaces.resolve("downtown").orElseThrow().name()).isEqualTo("Kluuvi");
        assertThat(HelsinkiPlaces.resolve("Toolo").orElseThrow().name()).startsWith("Etu-T");
        assertThat(HelsinkiPlaces.resolve("Barcelona")).isEmpty();
    }

    @Test
    void findsTheLongestMatchInFreeText() {
        assertThat(HelsinkiPlaces.findIn("what is open near the central railway station tonight")
                .orElseThrow().name()).isEqualTo("Central Railway Station");
        assertThat(HelsinkiPlaces.findIn("anything good in Kallio").orElseThrow().name()).isEqualTo("Kallio");
    }

    @Test
    void doesNotMatchPlaceNamesInsideOtherWords() {
        assertThat(HelsinkiPlaces.findIn("stationery shops")).isEmpty();
    }

    @Test
    void everyPlaceHasPlausibleHelsinkiCoordinates() {
        HelsinkiPlaces.names().forEach(name -> {
            HelsinkiPlaces.Place place = HelsinkiPlaces.resolve(name).orElseThrow();
            assertThat(place.latitude()).isBetween(60.10, 60.30);
            assertThat(place.longitude()).isBetween(24.80, 25.20);
        });
    }
}
