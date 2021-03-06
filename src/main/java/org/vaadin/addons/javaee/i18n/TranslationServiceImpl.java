/*******************************************************************************
 * Copyright 2012 Thomas Letsch
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *******************************************************************************/
package org.vaadin.addons.javaee.i18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SessionScoped
public class TranslationServiceImpl implements TranslationService {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(TranslationServiceImpl.class);

    @Inject
    Instance<TranslationSPI> providers;

    private Locale locale = Locale.getDefault();

    public TranslationServiceImpl() {
        log.debug("Cosntructor called");
    }

    @Override
    public String get(String key) {
        List<String> variations = getPossibleKeyVariations(key);
        for (String variation : variations) {
            String translation = searchInAllProviders(variation);
            if (isTranslated(variation, translation)) {
                return translation;
            }
        }
        return key;
    }

    @Override
    public String get(String key, Object... params) {
        List<String> variations = getPossibleKeyVariations(key);
        for (String variation : variations) {
            String translation = searchInAllProviders(variation, params);
            if (isTranslated(variation, translation)) {
                return translation;
            }
        }
        return key;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    boolean isTranslated(String key, String translation) {
        return !key.equals(translation);
    }

    private String searchInAllProviders(String key) {
        for (TranslationSPI spi : providers) {
            String translation = spi.get(key, locale);
            if (isTranslated(key, translation)) {
                return translation;
            }
        }
        return key;
    }

    private String searchInAllProviders(String key, Object[] params) {
        for (TranslationSPI spi : providers) {
            String translation = spi.get(key, locale, params);
            if (isTranslated(key, translation)) {
                return translation;
            }
        }
        return key;
    }

    boolean checkKey(String key, Locale locale, TranslationSPI spi) {
        String translation = spi.get(key, locale);
        return !key.equals(translation);
    }

    List<String> getPossibleKeyVariations(String key) {
        List<String> result = new ArrayList<String>();
        result.add(key);
        int pos = key.indexOf(".");
        while (pos > 0) {
            key = key.substring(pos + 1);
            result.add(key);
            pos = key.indexOf(".");
        }
        return result;
    }

}
