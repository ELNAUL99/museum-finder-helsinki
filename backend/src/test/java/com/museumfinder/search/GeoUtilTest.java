package com.museumfinder.search;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class GeoUtilTest {

    @Test
    void measuresKnownHelsinkiDistances() {
        // Kamppi to Suomenlinna, roughly 4.3 km as the gull flies.
        double km = GeoUtil.distanceKm(60.1690, 24.9310, 60.1450, 24.9880);
        assertThat(km).isCloseTo(4.3, within(0.4));
    }

    @Test
    void distanceToItselfIsZero() {
        assertThat(GeoUtil.distanceKm(60.17, 24.94, 60.17, 24.94)).isZero();
    }

    @Test
    void boundingBoxDeltasCoverTheRequestedRadius() {
        double km = 2.0;
        double latDelta = GeoUtil.latDelta(km);
        double lonDelta = GeoUtil.lonDelta(km, 60.17);
        assertThat(GeoUtil.distanceKm(60.17, 24.94, 60.17 + latDelta, 24.94)).isGreaterThanOrEqualTo(km);
        assertThat(GeoUtil.distanceKm(60.17, 24.94, 60.17, 24.94 + lonDelta)).isGreaterThanOrEqualTo(km);
    }
}
