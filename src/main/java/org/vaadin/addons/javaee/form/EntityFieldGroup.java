package org.vaadin.addons.javaee.form;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.addons.javaee.jpa.EntityItem;

import com.googlecode.javaeeutils.jpa.PersistentEntity;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.filter.And;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.ui.Field;

public class EntityFieldGroup<ENTITY extends PersistentEntity> extends FieldGroup {

    private EntityItem<ENTITY> item;

    public EntityFieldGroup() {
    }

    public void setItem(EntityItem<ENTITY> item) {
        this.item = item;
        setItemDataSource(item);
    }

    public EntityItem<ENTITY> getItem() {
        return item;
    }

    public Field<?> getFirstField() {
        return getFields().iterator().next();
    }

    public Filter getValuesAsFilter() {
        List<Filter> filters = new ArrayList<>();
        for (Field<?> field : getFields()) {
            if (field.isModified()) {
                filters.add(new SimpleStringFilter(getPropertyId(field), getStringValue(field), false, false));
            }
        }
        Filter filter = new And(filters.toArray(new Filter[] {}));
        return filter;
    }

    @Override
    public void commit() throws CommitException {
        super.commit();
        item.commit();
    }

    private String getStringValue(Field<?> field) {
        if (field.getValue() != null)
            return field.getValue().toString();
        else
            return null;
    }

}
