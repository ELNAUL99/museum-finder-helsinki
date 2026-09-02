package com.museumfinder.search;

/**
 * Turns a natural-language question into {@link SearchFilters}.
 *
 * <p>Implementations are ranked by {@code @Order}; {@link SearchService} takes the first
 * available one and falls through to the next if a call fails, so the keyword
 * interpreter is always the floor and the search box never returns an error.
 */
public interface QueryInterpreter {

    SearchFilters interpret(String naturalLanguageQuery);

    /** Short identifier reported to the client, e.g. {@code claude}, {@code mistral}, {@code keyword}. */
    String id();

    /** False when the interpreter has no credentials configured. */
    default boolean isAvailable() {
        return true;
    }
}
