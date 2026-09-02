package com.museumfinder.search;

public final class GeoUtil {

    private static final double EARTH_RADIUS_KM = 6371.0088;

    private GeoUtil() {
    }

    /** Great-circle distance in kilometres. */
    public static double distanceKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.min(1, Math.sqrt(a)));
    }

    /**
     * The bounding box is only a pre-filter - the exact great-circle check runs afterwards -
     * so the degrees-per-kilometre approximations are padded slightly. Without the padding a
     * museum sitting exactly on the radius is cut before the precise test ever sees it.
     */
    private static final double BOX_MARGIN = 1.01;

    /** Latitude degrees covering the given distance. */
    public static double latDelta(double km) {
        return BOX_MARGIN * km / 110.574;
    }

    /** Longitude degrees covering the given distance at this latitude. */
    public static double lonDelta(double km, double atLatitude) {
        return BOX_MARGIN * km / (111.320 * Math.cos(Math.toRadians(atLatitude)));
    }
}
