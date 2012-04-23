package com.optible.vaadin.utils.fields;

import java.util.Locale;

import com.optible.vaadin.utils.i18n.TranslationService;
import com.vaadin.data.util.converter.Converter;

public class StringToEnumConverter implements Converter<String, Enum> {

    private TranslationService translationService;

    public StringToEnumConverter(TranslationService translationService) {
        this.translationService = translationService;
    }

    @Override
    public Enum convertToModel(String value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
        return null;
    }

    @Override
    public String convertToPresentation(Enum value, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (value == null)
            return null;
        return translationService.get(value.getClass().getSimpleName() + "." + value.name());
    }

    @Override
    public Class<Enum> getModelType() {
        return Enum.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
