package com.museumfinder.web.dto;

import com.museumfinder.search.SearchFilters;

/**
 * Either half may be supplied. {@code q} is a natural-language question that the AI
 * interpreter turns into filters; {@code filters} is the structured form the UI posts
 * back when the visitor edits a chip. When both are present, {@code filters} wins -
 * that is what makes an AI result correctable.
 */
public record SearchRequest(String q, SearchFilters filters) {
}
