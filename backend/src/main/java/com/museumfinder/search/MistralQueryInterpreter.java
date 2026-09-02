package com.museumfinder.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * The same structured-extraction idea as {@link ClaudeQueryInterpreter}, against Mistral's
 * chat completions API, which has a free tier. Mistral has no Java SDK, so this is a plain
 * {@link RestClient} call using their JSON-schema response format with the schema from
 * {@link FilterSchema} - the same contract the query layer implements.
 */
@Component
@Order(20)
public class MistralQueryInterpreter implements QueryInterpreter {

    private static final Logger log = LoggerFactory.getLogger(MistralQueryInterpreter.class);
    private static final String ENDPOINT = "https://api.mistral.ai/v1/chat/completions";

    private final AiProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;
    private final String apiKey;

    public MistralQueryInterpreter(AiProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.apiKey = resolveKey(properties);
        this.client = RestClient.builder()
                .baseUrl(ENDPOINT)
                .requestFactory(timeouts())
                .build();
        if (apiKey != null) {
            log.info("Mistral query interpretation available using model {}.", properties.mistralModel());
        }
    }

    private static org.springframework.http.client.ClientHttpRequestFactory timeouts() {
        var factory = new org.springframework.http.client.SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(20));
        return factory;
    }

    private static String resolveKey(AiProperties properties) {
        if (properties.mistralApiKey() != null && !properties.mistralApiKey().isBlank()) {
            return properties.mistralApiKey().trim();
        }
        String fromEnv = System.getenv("MISTRAL_API_KEY");
        return fromEnv == null || fromEnv.isBlank() ? null : fromEnv.trim();
    }

    @Override
    public String id() {
        return "mistral";
    }

    @Override
    public boolean isAvailable() {
        return apiKey != null && properties.allows(id());
    }

    @Override
    @Cacheable(cacheNames = "nlQueries", unless = "#result == null")
    public SearchFilters interpret(String naturalLanguageQuery) {
        if (!isAvailable()) {
            throw new IllegalStateException("Mistral interpreter is not configured");
        }

        Map<String, Object> body = Map.of(
                "model", properties.mistralModel(),
                "temperature", 0,
                "max_tokens", properties.maxTokens(),
                "messages", List.of(
                        Map.of("role", "system", "content", SearchPrompt.SYSTEM),
                        Map.of("role", "user", "content", naturalLanguageQuery)),
                "response_format", Map.of(
                        "type", "json_schema",
                        "json_schema", Map.of(
                                "name", "museum_search_filters",
                                "strict", true,
                                "schema", FilterSchema.json())));

        String response = client.post()
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .body(String.class);

        SearchFilters filters = readFilters(response);
        SearchFilters normalized = filters.normalized();
        return normalized.interpretation().isBlank()
                ? normalized.withInterpretation(HeuristicQueryInterpreter.describe(normalized))
                : normalized;
    }

    private SearchFilters readFilters(String response) {
        try {
            JsonNode content = objectMapper.readTree(response)
                    .path("choices").path(0).path("message").path("content");
            if (content.isMissingNode() || content.asText().isBlank()) {
                throw new IllegalStateException("Mistral returned no content");
            }
            return objectMapper.readValue(content.asText(), SearchFilters.class);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException("Could not read Mistral's response: " + e.getOriginalMessage(), e);
        }
    }
}
