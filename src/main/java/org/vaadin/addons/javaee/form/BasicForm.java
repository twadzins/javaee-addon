package org.vaadin.addons.javaee.form;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.vaadin.addons.javaee.fields.FieldFactory;
import org.vaadin.addons.javaee.i18n.TranslationService;
import org.vaadin.addons.javaee.jpa.EntityContainer;

import com.googlecode.javaeeutils.jpa.PersistentEntity;
import com.vaadin.data.Container.Filter;
import com.vaadin.ui.Field;
import com.vaadin.ui.VerticalLayout;

public abstract class BasicForm<ENTITY extends PersistentEntity> extends VerticalLayout {

    protected EntityFieldGroup<ENTITY> fieldGroup;

    @Inject
    protected FieldFactory fieldFactory;

    @Inject
    protected TranslationService translationService;

    protected EntityContainer<ENTITY> entityContainer;

    protected Class<ENTITY> entityClass;

    private Map<String, FormSection> sections = new HashMap<String, FormSection>();

    public BasicForm(Class<ENTITY> entityClass) {
        this.entityClass = entityClass;
        fieldGroup = new EntityFieldGroup<ENTITY>();
    }

    protected abstract EntityContainer<ENTITY> getContainer();

    public Filter getValuesAsFilter() {
        return fieldGroup.getValuesAsFilter();
    }

    protected ENTITY getDefaultValue() throws InstantiationException, IllegalAccessException {
        return entityClass.newInstance();
    }

    @PostConstruct
    protected void init() {
        entityContainer = getContainer();
        initFields();
    }

    /**
     * Can be overwritten
     */
    protected void initFields() {
        List<String> fieldNames = entityContainer.getPropertyNames();
        initFields(fieldNames);
    }

    /**
     * Can be overwritten
     */
    protected void initFields(List<String> fieldNames) {
        FormSection section = getFormSection(entityClass.getSimpleName());
        initFields(section, fieldNames);
    }

    /**
     * Can be overwritten
     */
    protected void initFields(FormSection section, List<String> fieldNames) {
        for (String fieldName : fieldNames) {
            addField(section, fieldName);
        }
        fieldGroup.getFirstField().focus();
    }

    protected void addField(FormSection section, String fieldName) {
        Field<?> field = fieldFactory.createField(entityContainer, fieldName);
        addField(section, fieldName, field);
    }

    protected void addField(FormSection section, String fieldName, Field<?> field) {
        fieldGroup.bind(field, fieldName);
        section.addComponent(field);
    }

    public boolean isValid() {
        return fieldGroup.isValid();
    }

    public Field<?> getField(String name) {
        return fieldGroup.getField(name);
    }

    protected FormSection getDefaultSection() {
        return sections.entrySet().iterator().next().getValue();
    }

    protected FormSection getFormSection(String name) {
        FormSection section = sections.get(name);
        if (section == null) {
            section = new FormSection(translationService.get(name));
            sections.put(name, section);
            addComponent(section);
        }
        return section;
    }

}