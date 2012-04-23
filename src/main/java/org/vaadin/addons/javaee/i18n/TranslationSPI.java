package org.vaadin.addons.javaee.i18n;

import java.util.Locale;

import javax.inject.Singleton;

@Singleton
public interface TranslationSPI {

    String get(String key, Locale locale);

    String get(String key, Locale locale, Object... params);

}