package com.museumfinder.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ThemeConverter implements AttributeConverter<Theme, String> {

    @Override
    public String convertToDatabaseColumn(Theme theme) {
        return theme == null ? null : theme.dbValue();
    }

    @Override
    public Theme convertToEntityAttribute(String value) {
        return Theme.fromDbValue(value).orElse(null);
    }
}
