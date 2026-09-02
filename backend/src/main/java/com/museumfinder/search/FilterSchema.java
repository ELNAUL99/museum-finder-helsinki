package com.museumfinder.search;

import com.museumfinder.domain.Theme;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The JSON schema for {@link SearchFilters}, built from the Java enums so it cannot drift
 * from what the query layer implements. The Anthropic SDK derives its own schema from the
 * record; providers without that support - Mistral among them - get this one.
 */
final class FilterSchema {

    private FilterSchema() {
    }

    private static Map<String, Object> field(String type, String description) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("type", type);
        node.put("description", description);
        return node;
    }

    private static Map<String, Object> enumField(List<String> values, String description) {
        Map<String, Object> node = field("string", description);
        node.put("enum", values);
        return node;
    }

    private static Map<String, Object> arrayOf(Map<String, Object> items, String description) {
        Map<String, Object> node = new LinkedHashMap<>();
        node.put("type", "array");
        node.put("items", items);
        node.put("description", description);
        return node;
    }

    static Map<String, Object> json() {
        List<String> themes = Arrays.stream(Theme.values()).map(Enum::name).toList();
        List<String> days = Arrays.stream(DayFilter.values()).map(Enum::name).toList();
        List<String> sorts = Arrays.stream(SortOrder.values()).map(Enum::name).toList();

        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put("themes", arrayOf(enumField(themes, "A subject area"),
                "Subject areas the visitor wants. Empty means no subject constraint."));
        properties.put("freeOnly", field("boolean", "True only if the visitor explicitly wants free entry."));
        properties.put("museumCardOnly", field("boolean", "True only if the visitor mentions the Finnish Museum Card."));
        properties.put("maxPriceEur", field("number", "Maximum adult ticket price in euros, or -1 for no budget."));
        properties.put("openOn", enumField(days, "Day the museum must be open. ANY unless a day was named."));
        properties.put("openNow", field("boolean", "True only if the visitor wants somewhere open at this moment."));
        properties.put("nearPlace", field("string",
                "A Helsinki district or landmark from the list in the instructions, or an empty string."));
        properties.put("radiusKm", field("number", "Search radius in km around nearPlace, or 0 for the 2 km default."));
        properties.put("wheelchairAccessible", field("boolean", "True only if step-free access was asked for."));
        properties.put("familyFriendly", field("boolean", "True only if the visitor is bringing children."));
        properties.put("hasCafe", field("boolean", "True only if a cafe on site was asked for."));
        properties.put("keywords", arrayOf(field("string", "A distinctive word"),
                "Proper nouns or objects the themes cannot express, e.g. Aalto, dinosaur, tram. Never 'museum'."));
        properties.put("sort", enumField(sorts, "How to order the results."));
        properties.put("interpretation", field("string",
                "One short sentence to the visitor describing the filters you set. Max 20 words."));

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");
        schema.put("properties", properties);
        schema.put("required", List.copyOf(properties.keySet()));
        schema.put("additionalProperties", false);
        return schema;
    }
}
