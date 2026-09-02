package com.museumfinder.web;

import com.museumfinder.domain.Theme;
import com.museumfinder.search.ClaudeQueryInterpreter;
import com.museumfinder.search.HelsinkiPlaces;
import com.museumfinder.web.dto.ThemeDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/** Vocabulary the frontend needs to render filter controls without hardcoding it. */
@RestController
@RequestMapping("/api")
public class MetaController {

    private final ClaudeQueryInterpreter claude;

    public MetaController(ClaudeQueryInterpreter claude) {
        this.claude = claude;
    }

    public record Meta(List<ThemeDto> themes, List<String> places, boolean aiSearchEnabled, List<String> examples) {
    }

    @GetMapping("/meta")
    public Meta meta() {
        return new Meta(
                Arrays.stream(Theme.values()).map(ThemeDto::of).toList(),
                HelsinkiPlaces.names(),
                claude.isAvailable(),
                List.of(
                        "free art museums open on Sunday near Kamppi",
                        "somewhere with dinosaurs for a 6-year-old",
                        "design and architecture, under 15 euros",
                        "what's open right now near the railway station",
                        "Alvar Aalto buildings I can actually go inside"));
    }
}
