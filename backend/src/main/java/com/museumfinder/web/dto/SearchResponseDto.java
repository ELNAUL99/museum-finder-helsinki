package com.museumfinder.web.dto;

import com.museumfinder.search.SearchFilters;

import java.util.List;

/**
 * @param interpretedBy {@code claude}, {@code keyword} or {@code filters} - so the UI can
 *                      say honestly how the query was understood
 * @param note          set when the search had to be relaxed to return anything at all
 */
public record SearchResponseDto(String query,
                                SearchFilters filters,
                                String interpretedBy,
                                int total,
                                List<MuseumSummaryDto> results,
                                String note) {
}
