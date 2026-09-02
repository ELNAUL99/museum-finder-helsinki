package com.museumfinder.search;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A small offline gazetteer of Helsinki districts and landmarks, so "near Kamppi"
 * can be resolved to coordinates without calling a geocoding service.
 */
public final class HelsinkiPlaces {

    public record Place(String name, double latitude, double longitude) {}

    private static final Map<String, Place> PLACES = new LinkedHashMap<>();
    private static final Map<String, String> ALIASES = new LinkedHashMap<>();

    private static void put(String name, double lat, double lon, String... aliases) {
        Place place = new Place(name, lat, lon);
        PLACES.put(key(name), place);
        for (String alias : aliases) {
            ALIASES.put(key(alias), key(name));
        }
    }

    static {
        // Districts
        put("Kluuvi", 60.1710, 24.9450, "city centre", "city center", "downtown", "keskusta", "central helsinki", "helsinki centre");
        put("Kamppi", 60.1690, 24.9310, "kamppi centre", "narinkkatori");
        put("Kruununhaka", 60.1710, 24.9540, "senate square", "senaatintori", "helsinki cathedral", "tuomiokirkko");
        put("Katajanokka", 60.1670, 24.9700, "uspenski cathedral");
        put("Kaartinkaupunki", 60.1640, 24.9490, "esplanadi", "esplanade", "market square", "kauppatori");
        put("Punavuori", 60.1610, 24.9370, "bulevardi", "design district");
        put("Eira", 60.1560, 24.9410);
        put("Ullanlinna", 60.1590, 24.9490, "tahtitorninmaki", "observatory hill");
        put("Kaivopuisto", 60.1560, 24.9560);
        put("Etu-Töölö", 60.1750, 24.9280, "etu-toolo", "front toolo", "finlandia hall", "oodi", "central library");
        put("Taka-Töölö", 60.1850, 24.9230, "taka-toolo", "olympic stadium", "olympiastadion");
        put("Meilahti", 60.1900, 24.9060);
        put("Ruoholahti", 60.1620, 24.9080, "cable factory", "kaapelitehdas", "salmisaari");
        put("Jätkäsaari", 60.1560, 24.9160, "jatkasaari", "west harbour");
        put("Lauttasaari", 60.1600, 24.8790);
        put("Munkkiniemi", 60.1970, 24.8730);
        put("Kuusisaari", 60.1880, 24.8630);
        put("Seurasaari", 60.1850, 24.8830);
        put("Suomenlinna", 60.1450, 24.9880, "sveaborg", "sea fortress", "fortress island");
        put("Kallio", 60.1840, 24.9500, "torkkelinmaki");
        put("Sörnäinen", 60.1880, 24.9620, "sornainen", "kalasatama");
        put("Hakaniemi", 60.1790, 24.9500, "hakaniemi market");
        put("Vallila", 60.1930, 24.9560);
        put("Pasila", 60.1990, 24.9330, "mall of tripla", "messukeskus");
        put("Viikki", 60.2110, 25.0090, "vanhankaupunginkoski", "old town rapids");
        put("Arabianranta", 60.2050, 24.9800, "arabia");
        put("Herttoniemi", 60.1950, 25.0300);
        put("Vuosaari", 60.2100, 25.1440);
        put("Oulunkylä", 60.2280, 24.9680, "oulunkyla");
        put("Malmi", 60.2510, 25.0100);
        put("Töölönlahti", 60.1780, 24.9370, "toolonlahti", "toolo bay");

        // Landmarks that people actually name
        put("Central Railway Station", 60.1719, 24.9414, "central station", "rautatieasema", "helsinki central", "railway station", "train station");
        put("Linnanmäki", 60.1870, 24.9420, "linnanmaki", "amusement park");
        put("Kamppi Chapel", 60.1690, 24.9330, "chapel of silence");
        put("Hietaniemi Beach", 60.1730, 24.9080, "hietaranta", "hietsu");
    }

    private HelsinkiPlaces() {
    }

    private static String key(String value) {
        return value.toLowerCase(Locale.ROOT)
                .replace("ä", "a").replace("ö", "o").replace("å", "a")
                .replaceAll("[^a-z0-9 ]", " ")
                .replaceAll("\\s+", " ")
                .trim();
    }

    /** Resolve a free-text place name. Falls back to a contains-match so "near Kallio area" still works. */
    public static Optional<Place> resolve(String input) {
        if (input == null || input.isBlank()) {
            return Optional.empty();
        }
        String k = key(input);
        if (PLACES.containsKey(k)) {
            return Optional.of(PLACES.get(k));
        }
        if (ALIASES.containsKey(k)) {
            return Optional.of(PLACES.get(ALIASES.get(k)));
        }
        for (Map.Entry<String, Place> entry : PLACES.entrySet()) {
            if (k.contains(entry.getKey()) || entry.getKey().contains(k)) {
                return Optional.of(entry.getValue());
            }
        }
        for (Map.Entry<String, String> alias : ALIASES.entrySet()) {
            if (k.contains(alias.getKey())) {
                return Optional.of(PLACES.get(alias.getValue()));
            }
        }
        return Optional.empty();
    }

    /**
     * Scan free text for any known place name or alias and return the longest match,
     * so "near the central railway station" beats a stray match on "station".
     */
    public static Optional<Place> findIn(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }
        String haystack = key(text);
        String bestKey = null;
        Place best = null;
        for (Map.Entry<String, Place> entry : PLACES.entrySet()) {
            if (containsWord(haystack, entry.getKey())
                    && (bestKey == null || entry.getKey().length() > bestKey.length())) {
                bestKey = entry.getKey();
                best = entry.getValue();
            }
        }
        for (Map.Entry<String, String> alias : ALIASES.entrySet()) {
            if (containsWord(haystack, alias.getKey())
                    && (bestKey == null || alias.getKey().length() > bestKey.length())) {
                bestKey = alias.getKey();
                best = PLACES.get(alias.getValue());
            }
        }
        return Optional.ofNullable(best);
    }

    private static boolean containsWord(String haystack, String needle) {
        int index = haystack.indexOf(needle);
        while (index >= 0) {
            boolean startOk = index == 0 || haystack.charAt(index - 1) == ' ';
            int end = index + needle.length();
            boolean endOk = end == haystack.length() || haystack.charAt(end) == ' ';
            if (startOk && endOk) {
                return true;
            }
            index = haystack.indexOf(needle, index + 1);
        }
        return false;
    }

    /** All words that make up any known place name or alias - never useful as search keywords. */
    public static Set<String> allPlaceWords() {
        Set<String> words = new java.util.HashSet<>();
        Stream.concat(PLACES.keySet().stream(), ALIASES.keySet().stream())
                .forEach(k -> words.addAll(Arrays.asList(k.split(" "))));
        return words;
    }

    /** Every canonical place name, in declaration order. Used to prompt the model and to seed the UI. */
    public static java.util.List<String> names() {
        return PLACES.values().stream().map(Place::name).toList();
    }
}
