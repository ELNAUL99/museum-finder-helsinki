package com.museumfinder.search;

public enum SortOrder {
    /** Keyword hits first, then proximity when a place was given, then name. */
    RELEVANCE,
    DISTANCE,
    PRICE_ASC,
    NAME
}
