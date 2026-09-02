package com.museumfinder.web;

import com.museumfinder.search.SearchService;
import com.museumfinder.security.CurrentUser;
import com.museumfinder.web.dto.SearchRequest;
import com.museumfinder.web.dto.SearchResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SearchController {

    private final SearchService searchService;
    private final CurrentUser currentUser;

    public SearchController(SearchService searchService, CurrentUser currentUser) {
        this.searchService = searchService;
        this.currentUser = currentUser;
    }

    /**
     * Structured filters take priority over the question: once the visitor edits a chip,
     * the UI posts the filters back and the model is not consulted again.
     */
    @PostMapping("/search")
    public SearchResponseDto search(@RequestBody SearchRequest request) {
        var favorites = currentUser.favoriteIds();
        if (request.filters() != null) {
            return searchService.search(request.filters(), request.q() == null ? "" : request.q(),
                    "filters", favorites);
        }
        return searchService.searchByQuestion(request.q(), favorites);
    }
}
