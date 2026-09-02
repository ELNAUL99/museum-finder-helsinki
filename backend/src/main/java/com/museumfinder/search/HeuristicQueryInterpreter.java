package com.museumfinder.search;

import com.museumfinder.domain.Theme;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule-based interpreter. It is the fallback whenever Claude is unavailable, and it is
 * also what makes the application usable with no API key at all - the search box still
 * understands "free art museums open on Sunday", just less flexibly.
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class HeuristicQueryInterpreter implements QueryInterpreter {

    private static final Map<String, Theme> THEME_WORDS = new LinkedHashMap<>();
    /**
     * Words that never make good search keywords: either they are grammar, or another
     * filter has already consumed their meaning (a day name becomes {@code openOn},
     * "free" becomes {@code freeOnly}). Leaving them in used to empty the result set.
     */
    private static final Set<String> STOP_WORDS = Set.of(
            "a", "about", "afternoon", "an", "and", "another", "any", "anything", "are", "around", "at", "be",
            "best", "bring", "budget", "can", "cafe", "cheap", "child", "children", "close", "coffee", "cool",
            "could", "day", "days", "distance", "do", "entrance", "euro", "euros", "evening", "family", "favorite",
            "favourite", "find", "for", "free", "friday", "from", "go", "going", "good", "great", "helsinki",
            "hour", "hours", "i", "idea", "ideas", "in", "indoor", "indoors", "is", "it", "kid", "kids", "less",
            "looking", "lunch", "maximum", "me", "monday", "morning", "much", "museum", "museums", "my", "near",
            "nearby", "nice", "night", "now", "of", "old", "on", "open", "or", "place", "places", "please",
            "popular", "price", "prices", "rainy", "right", "recommend", "recommendation", "recommendations", "saturday",
            "show", "some", "something", "somewhere", "sunday", "suggest", "suggestion", "suggestions", "take",
            "taking", "that", "the", "there", "this", "thursday", "ticket", "tickets", "time", "tips", "to",
            "today", "tomorrow", "tuesday", "under", "vaikka", "visit", "visiting", "want", "we", "weather",
            "weekend", "wednesday", "what", "when", "where", "wheelchair", "which", "while", "with", "worth",
            "would", "year",
            "years", "you", "your");

    static {
        THEME_WORDS.put("art", Theme.ART);
        THEME_WORDS.put("painting", Theme.ART);
        THEME_WORDS.put("paintings", Theme.ART);
        THEME_WORDS.put("gallery", Theme.ART);
        THEME_WORDS.put("contemporary", Theme.MODERN_ART);
        THEME_WORDS.put("modern", Theme.MODERN_ART);
        THEME_WORDS.put("classical", Theme.CLASSICAL_ART);
        THEME_WORDS.put("old master", Theme.CLASSICAL_ART);
        THEME_WORDS.put("design", Theme.DESIGN);
        THEME_WORDS.put("marimekko", Theme.DESIGN);
        THEME_WORDS.put("architecture", Theme.ARCHITECTURE);
        THEME_WORDS.put("aalto", Theme.ARCHITECTURE);
        THEME_WORDS.put("photo", Theme.PHOTOGRAPHY);
        THEME_WORDS.put("photography", Theme.PHOTOGRAPHY);
        THEME_WORDS.put("history", Theme.HISTORY);
        THEME_WORDS.put("historical", Theme.HISTORY);
        THEME_WORDS.put("nature", Theme.NATURAL_HISTORY);
        THEME_WORDS.put("dinosaur", Theme.NATURAL_HISTORY);
        THEME_WORDS.put("dinosaurs", Theme.NATURAL_HISTORY);
        THEME_WORDS.put("animals", Theme.NATURAL_HISTORY);
        THEME_WORDS.put("science", Theme.SCIENCE);
        THEME_WORDS.put("technology", Theme.TECHNOLOGY);
        THEME_WORDS.put("tech", Theme.TECHNOLOGY);
        THEME_WORDS.put("tram", Theme.TECHNOLOGY);
        THEME_WORDS.put("trams", Theme.TECHNOLOGY);
        THEME_WORDS.put("sea", Theme.MARITIME);
        THEME_WORDS.put("maritime", Theme.MARITIME);
        THEME_WORDS.put("ship", Theme.MARITIME);
        THEME_WORDS.put("war", Theme.MILITARY);
        THEME_WORDS.put("military", Theme.MILITARY);
        THEME_WORDS.put("army", Theme.MILITARY);
        THEME_WORDS.put("outdoor", Theme.OPEN_AIR);
        THEME_WORDS.put("outdoors", Theme.OPEN_AIR);
        THEME_WORDS.put("open air", Theme.OPEN_AIR);
        THEME_WORDS.put("sport", Theme.SPORTS);
        THEME_WORDS.put("sports", Theme.SPORTS);
        THEME_WORDS.put("olympic", Theme.SPORTS);
        THEME_WORDS.put("music", Theme.MUSIC);
        THEME_WORDS.put("theatre", Theme.THEATRE);
        THEME_WORDS.put("theater", Theme.THEATRE);
        THEME_WORDS.put("stars", Theme.ASTRONOMY);
        THEME_WORDS.put("astronomy", Theme.ASTRONOMY);
        THEME_WORDS.put("space", Theme.ASTRONOMY);
        THEME_WORDS.put("plants", Theme.BOTANY);
        THEME_WORDS.put("botanic", Theme.BOTANY);
        THEME_WORDS.put("garden", Theme.BOTANY);
        THEME_WORDS.put("city", Theme.CITY_HISTORY);
    }

    private static final Pattern PRICE = Pattern.compile(
            "(?:under|below|less than|max|maximum|up to|cheaper than)\\s*(?:€|eur|euros?)?\\s*(\\d{1,3})"
                    + "|(\\d{1,3})\\s*(?:€|eur|euros?)\\s*(?:or less|max|maximum)?");

    @Override
    public String id() {
        return "keyword";
    }

    @Override
    public SearchFilters interpret(String query) {
        String q = query == null ? "" : query.toLowerCase(Locale.ROOT).trim();

        boolean free = q.contains("free") || q.contains("no entrance fee") || q.contains("ilmainen") || q.contains("gratis");
        boolean museumCard = q.contains("museum card") || q.contains("museokortti") || q.contains("museumcard");
        boolean openNow = q.contains("open now") || q.contains("right now") || q.contains("at the moment") || q.contains("open at this hour");
        boolean accessible = q.contains("wheelchair") || q.contains("accessible") || q.contains("step-free") || q.contains("step free");
        boolean family = q.contains("kid") || q.contains("child") || q.contains("family") || q.contains("toddler") || q.contains("with my son") || q.contains("with my daughter");
        boolean cafe = q.contains("cafe") || q.contains("café") || q.contains("coffee") || q.contains("lunch");

        double maxPrice = -1;
        Matcher priceMatcher = PRICE.matcher(q);
        if (priceMatcher.find()) {
            String value = priceMatcher.group(1) != null ? priceMatcher.group(1) : priceMatcher.group(2);
            maxPrice = Double.parseDouble(value);
        } else if (q.contains("cheap") || q.contains("budget")) {
            maxPrice = 12;
        }

        DayFilter day = DayFilter.ANY;
        if (q.contains("weekend")) {
            day = DayFilter.WEEKEND;
        } else if (q.contains("tomorrow")) {
            day = DayFilter.TOMORROW;
        } else if (q.contains("today") || openNow) {
            day = DayFilter.TODAY;
        } else {
            for (DayFilter candidate : List.of(DayFilter.MONDAY, DayFilter.TUESDAY, DayFilter.WEDNESDAY,
                    DayFilter.THURSDAY, DayFilter.FRIDAY, DayFilter.SATURDAY, DayFilter.SUNDAY)) {
                if (q.contains(candidate.name().toLowerCase(Locale.ROOT))) {
                    day = candidate;
                    break;
                }
            }
        }

        String place = HelsinkiPlaces.findIn(q).map(HelsinkiPlaces.Place::name).orElse("");
        if (place.isEmpty()) {
            Matcher near = Pattern.compile("(?:near|around|close to|next to|by)\\s+(?:the\\s+)?([a-zä-öå\\-]+)").matcher(q);
            while (near.find() && place.isEmpty()) {
                place = HelsinkiPlaces.resolve(near.group(1)).map(HelsinkiPlaces.Place::name).orElse("");
            }
        }

        Set<Theme> themes = new LinkedHashSet<>();
        for (Map.Entry<String, Theme> entry : THEME_WORDS.entrySet()) {
            if (q.contains(entry.getKey())) {
                themes.add(entry.getValue());
            }
        }

        // Only words no other filter has already claimed are worth matching as text.
        Set<String> consumed = new LinkedHashSet<>(STOP_WORDS);
        consumed.addAll(THEME_WORDS.keySet());
        consumed.addAll(HelsinkiPlaces.allPlaceWords());
        List<String> keywords = new ArrayList<>();
        for (String word : q.split("[^a-zä-öå0-9]+")) {
            if (word.length() >= 4 && word.chars().noneMatch(Character::isDigit)
                    && !consumed.contains(word) && !keywords.contains(word) && keywords.size() < 3) {
                keywords.add(word);
            }
        }

        SearchFilters filters = new SearchFilters(List.copyOf(themes), free, museumCard, maxPrice, day, openNow,
                place, 0, accessible, family, cafe, List.copyOf(keywords), SortOrder.RELEVANCE, "")
                .normalized();
        return filters.withInterpretation(describe(filters));
    }

    /** Builds the "understood as ..." sentence the UI shows above the results. */
    static String describe(SearchFilters f) {
        List<String> parts = new ArrayList<>();
        if (f.freeOnly()) {
            parts.add("free entry");
        }
        if (f.museumCardOnly()) {
            parts.add("Museum Card accepted");
        }
        if (f.hasPriceCap()) {
            parts.add("under €" + (long) f.maxPriceEur());
        }
        if (!f.themes().isEmpty()) {
            parts.add(String.join(" or ", f.themes().stream().map(Theme::label).toList()).toLowerCase(Locale.ROOT));
        }
        if (f.openNow()) {
            parts.add("open right now");
        } else if (f.openOn() != DayFilter.ANY) {
            parts.add("open " + f.openOn().name().toLowerCase(Locale.ROOT));
        }
        if (f.hasPlace()) {
            parts.add("near " + f.nearPlace());
        }
        if (f.familyFriendly()) {
            parts.add("good with children");
        }
        if (f.wheelchairAccessible()) {
            parts.add("step-free access");
        }
        if (f.hasCafe()) {
            parts.add("has a cafe");
        }
        if (!f.keywords().isEmpty()) {
            parts.add("matching " + String.join(", ", f.keywords()));
        }
        return parts.isEmpty() ? "Showing every museum in Helsinki." : "Showing museums: " + String.join(" · ", parts) + ".";
    }
}
