package com.museumfinder.web;

import com.museumfinder.domain.Theme;
import com.museumfinder.search.HelsinkiPlaces;
import com.museumfinder.search.QueryInterpreter;
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

    private final List<QueryInterpreter> interpreters;

    public MetaController(List<QueryInterpreter> interpreters) {
        this.interpreters = interpreters;
    }

    /**
     * @param aiProvider which interpreter will read the next question - {@code claude},
     *                   {@code mistral} or {@code keyword} - so the UI can say so honestly
     */
    public record Meta(List<ThemeDto> themes, List<String> places, boolean aiSearchEnabled,
                       String aiProvider, List<String> examples) {
    }

    @GetMapping("/meta")
    public Meta meta() {
        String provider = interpreters.stream()
                .filter(QueryInterpreter::isAvailable)
                .map(QueryInterpreter::id)
                .findFirst()
                .orElse("keyword");

        return new Meta(
                Arrays.stream(Theme.values()).map(ThemeDto::of).toList(),
                HelsinkiPlaces.names(),
                !provider.equals("keyword"),
                provider,
                List.of(
                        "free art museums open on Sunday near Kamppi",
                        "somewhere with dinosaurs for a 6-year-old",
                        "design and architecture, under 15 euros",
                        "what's open right now near the railway station",
                        "Alvar Aalto buildings I can actually go inside"));
    }
}
