package com.museumfinder.search;

import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.StructuredMessageCreateParams;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The Anthropic SDK derives the JSON schema for structured output from
 * {@link SearchFilters} when the request is built - before any network call. This test
 * therefore catches a field type the schema generator cannot express, without an API key
 * and without spending a token.
 */
class ClaudeSchemaTest {

    @Test
    void searchFiltersCanBeExpressedAsAJsonSchema() {
        StructuredMessageCreateParams<SearchFilters> params = MessageCreateParams.builder()
                .model("claude-opus-5")
                .maxTokens(1024L)
                .system("test")
                .outputConfig(SearchFilters.class)
                .addUserMessage("free art museums open on Sunday near Kamppi")
                .build();

        assertThat(params).isNotNull();
        assertThat(params.rawParams().outputConfig()).isPresent();
    }
}
