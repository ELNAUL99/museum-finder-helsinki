package com.museumfinder.search;

import com.museumfinder.domain.Theme;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ProviderSelectionTest {

    private static AiProperties props(boolean enabled, String provider) {
        return new AiProperties(enabled, provider, "", "", "", "", 0);
    }

    @Test
    void autoAllowsEveryProvider() {
        AiProperties auto = props(true, "auto");
        assertThat(auto.allows("claude")).isTrue();
        assertThat(auto.allows("mistral")).isTrue();
    }

    @Test
    void pinningAProviderExcludesTheOthers() {
        AiProperties mistralOnly = props(true, "mistral");
        assertThat(mistralOnly.allows("mistral")).isTrue();
        assertThat(mistralOnly.allows("claude")).isFalse();
    }

    @Test
    void disablingAiShutsEveryProviderOff() {
        AiProperties off = props(false, "auto");
        assertThat(off.allows("claude")).isFalse();
        assertThat(off.allows("mistral")).isFalse();
    }

    @Test
    void defaultsFillInWhenConfigurationIsBlank() {
        AiProperties blank = new AiProperties(true, null, null, null, null, null, 0);
        assertThat(blank.provider()).isEqualTo("auto");
        assertThat(blank.model()).isEqualTo("claude-opus-5");
        assertThat(blank.mistralModel()).isEqualTo("mistral-small-latest");
        assertThat(blank.maxTokens()).isEqualTo(2048);
    }

    /** The schema handed to providers must offer exactly the values the query layer implements. */
    @Test
    @SuppressWarnings("unchecked")
    void schemaEnumsAreDerivedFromTheJavaEnums() {
        Map<String, Object> schema = FilterSchema.json();
        Map<String, Object> properties = (Map<String, Object>) schema.get("properties");

        Map<String, Object> themes = (Map<String, Object>) properties.get("themes");
        Map<String, Object> themeItems = (Map<String, Object>) themes.get("items");
        assertThat((List<String>) themeItems.get("enum"))
                .containsExactly(java.util.Arrays.stream(Theme.values()).map(Enum::name).toArray(String[]::new));

        Map<String, Object> openOn = (Map<String, Object>) properties.get("openOn");
        assertThat((List<String>) openOn.get("enum")).contains("ANY", "WEEKEND", "SUNDAY");

        assertThat((List<String>) schema.get("required")).hasSize(properties.size());
        assertThat(schema).containsEntry("additionalProperties", false);
    }
}
