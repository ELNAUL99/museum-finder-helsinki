package com.museumfinder.search;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @param enabled  turn the AI interpreter off entirely and always use the keyword fallback
 * @param apiKey   Anthropic API key; when blank the ANTHROPIC_API_KEY environment variable is used
 * @param model    model id used for query interpretation
 * @param maxTokens response budget for the extraction call
 */
@ConfigurationProperties(prefix = "museumfinder.ai")
public record AiProperties(boolean enabled, String apiKey, String model, long maxTokens) {

    public AiProperties {
        if (model == null || model.isBlank()) {
            model = "claude-opus-5";
        }
        if (maxTokens <= 0) {
            maxTokens = 2048;
        }
    }
}
