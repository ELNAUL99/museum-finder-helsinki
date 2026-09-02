package com.museumfinder.search;

/** Turns a natural-language question into {@link SearchFilters}. */
public interface QueryInterpreter {

    SearchFilters interpret(String naturalLanguageQuery);

    /** Short identifier reported to the client, e.g. {@code claude} or {@code keyword}. */
    String id();
}
