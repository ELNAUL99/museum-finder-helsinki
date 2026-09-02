package com.museumfinder.search;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.StructuredMessageCreateParams;
import com.museumfinder.domain.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Reads a visitor's sentence and returns the structured filters behind it, using the
 * Claude Messages API with a JSON schema derived from {@link SearchFilters}. The schema
 * is what keeps this honest: the model cannot invent a theme or a sort order that the
 * query layer does not implement.
 *
 * <p>If no API key is configured the bean stays present but reports itself unavailable,
 * and {@link SearchService} silently uses the keyword interpreter instead.
 */
@Component
public class ClaudeQueryInterpreter implements QueryInterpreter {

    private static final Logger log = LoggerFactory.getLogger(ClaudeQueryInterpreter.class);

    private final AiProperties properties;
    private final AnthropicClient client;
    private final String systemPrompt;

    public ClaudeQueryInterpreter(AiProperties properties) {
        this.properties = properties;
        this.client = createClient(properties);
        this.systemPrompt = buildSystemPrompt();
        if (client == null) {
            log.info("Claude query interpretation is off (no API key found). Falling back to keyword search.");
        } else {
            log.info("Claude query interpretation enabled using model {}.", properties.model());
        }
    }

    private static AnthropicClient createClient(AiProperties properties) {
        if (!properties.enabled()) {
            return null;
        }
        try {
            if (properties.apiKey() != null && !properties.apiKey().isBlank()) {
                return AnthropicOkHttpClient.builder().apiKey(properties.apiKey()).build();
            }
            if (System.getenv("ANTHROPIC_API_KEY") == null || System.getenv("ANTHROPIC_API_KEY").isBlank()) {
                return null;
            }
            return AnthropicOkHttpClient.fromEnv();
        } catch (RuntimeException e) {
            log.warn("Could not initialise the Anthropic client: {}", e.getMessage());
            return null;
        }
    }

    public boolean isAvailable() {
        return client != null;
    }

    @Override
    public String id() {
        return "claude";
    }

    @Override
    @Cacheable(cacheNames = "nlQueries", unless = "#result == null")
    public SearchFilters interpret(String naturalLanguageQuery) {
        if (client == null) {
            throw new IllegalStateException("Claude interpreter is not configured");
        }

        StructuredMessageCreateParams<SearchFilters> params = MessageCreateParams.builder()
                .model(properties.model())
                .maxTokens(properties.maxTokens())
                .system(systemPrompt)
                .outputConfig(SearchFilters.class)
                .addUserMessage(naturalLanguageQuery)
                .build();

        SearchFilters filters = client.messages().create(params).content().stream()
                .flatMap(block -> block.text().stream())
                .map(text -> text.text())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Claude returned no structured content"));

        SearchFilters normalized = filters.normalized();
        return normalized.interpretation().isBlank()
                ? normalized.withInterpretation(HeuristicQueryInterpreter.describe(normalized))
                : normalized;
    }

    private static String buildSystemPrompt() {
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
                - Write interpretation as one short sentence addressed to the visitor, describing the filters you set.
                """.formatted(themes, places);
    }
}
