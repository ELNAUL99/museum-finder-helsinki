package com.museumfinder.search;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;

/**
 * @param enabled       turn AI interpretation off entirely and always use the keyword fallback
 * @param provider      {@code auto} (best available), {@code claude}, {@code mistral} or {@code none}
 * @param apiKey        Anthropic API key; blank falls back to the ANTHROPIC_API_KEY environment variable
 * @param model         Claude model used for query interpretation
 * @param mistralApiKey Mistral API key; blank falls back to the MISTRAL_API_KEY environment variable
 * @param mistralModel  Mistral model used for query interpretation
 * @param maxTokens     response budget for the extraction call
 */
@ConfigurationProperties(prefix = "museumfinder.ai")
public record AiProperties(boolean enabled,
                           String provider,
                           String apiKey,
                           String model,
                           String mistralApiKey,
                           String mistralModel,
                           long maxTokens) {

    public AiProperties {
        provider = provider == null || provider.isBlank() ? "auto" : provider.trim().toLowerCase(Locale.ROOT);
        model = model == null || model.isBlank() ? "claude-opus-5" : model;
        mistralModel = mistralModel == null || mistralModel.isBlank() ? "mistral-small-latest" : mistralModel;
        maxTokens = maxTokens <= 0 ? 2048 : maxTokens;
    }

    /** True when this provider id is allowed to run under the current setting. */
    public boolean allows(String interpreterId) {
        return enabled && (provider.equals("auto") || provider.equals(interpreterId));
    }
}
