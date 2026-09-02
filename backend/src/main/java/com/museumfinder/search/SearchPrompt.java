package com.museumfinder.search;

import com.museumfinder.domain.Theme;

import java.util.Arrays;
import java.util.stream.Collectors;

/** The instructions every AI interpreter shares, so the providers behave the same way. */
final class SearchPrompt {

    private SearchPrompt() {
    }

    static final String SYSTEM = build();

    private static String build() {
        String themes = Arrays.stream(Theme.values()).map(Enum::name).collect(Collectors.joining(", "));
        String places = String.join(", ", HelsinkiPlaces.names());
        return """
                You convert a visitor's question into filters for a Helsinki museum search engine.
                The catalogue covers museums inside Helsinki only - about thirty of them, from the
                Ateneum and Kiasma to small free house museums and the Suomenlinna island museums.

                Available themes: %s
                Known districts and landmarks: %s

                Rules:
                - Set a filter only when the visitor's words support it. Everything else keeps its neutral default.
                - Prefer themes over keywords. Use keywords only for specific proper nouns or objects the themes
                  cannot express, such as "Aalto", "dinosaur", "tram", "Schjerfbeck".
                - nearPlace must be one of the known districts or landmarks, or an empty string.
                - Treat "cheap" as maxPriceEur 12, and "free" as freeOnly rather than a price of 0.
                - "Rainy day", "indoors" and similar do not map to any filter - leave them out rather than guessing.
                - Choose DISTANCE sort when the visitor asks for something nearby, PRICE_ASC when they lead with
                  money, otherwise RELEVANCE.
                - Use -1 for maxPriceEur and 0 for radiusKm when the visitor gave no budget or no radius.
                - Write interpretation as one short sentence addressed to the visitor, describing the filters you set.
                """.formatted(themes, places);
    }
}
