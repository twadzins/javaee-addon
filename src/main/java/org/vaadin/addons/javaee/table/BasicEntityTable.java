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
package org.vaadin.addons.javaee.table;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.vaadin.addons.javaee.i18n.TranslationService;
import org.vaadin.addons.javaee.jpa.EntityContainer;
import org.vaadin.addons.javaee.jpa.EntityItem;

import com.googlecode.javaeeutils.jpa.PersistentEntity;
import com.vaadin.data.Container;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.ui.Table;

@Dependent
public abstract class BasicEntityTable<ENTITY extends PersistentEntity> extends Table implements Container.Filterable {

    private static final int BATCH_SIZE = 10;

    @Inject
    private TranslationService translationService;

    private EntityContainer<ENTITY> jpaContainer;

    public BasicEntityTable(Class<ENTITY> entityClass) {
        setDebugId(entityClass.getSimpleName() + "Table");
    }

    public abstract EntityContainer<ENTITY> getContainer();

    /**
     * Can be overwritten
     */
    protected void initColumns() {
        List<String> columnNames = getContainer().getPropertyNames();
        initColumns(columnNames);
    }

    /**
     * Can be overwritten
     */
    protected void initColumns(List<String> columnNames) {
        for (String columnName : columnNames) {
            addColumn(columnName);
        }
    }

    @PostConstruct
    protected void init() {
        this.jpaContainer = getContainer();
        init(jpaContainer);
    }

    protected void init(EntityContainer<ENTITY> jpaContainer) {
        this.jpaContainer = jpaContainer;
        setImmediate(true);
        setEditable(false);
        setMultiSelect(false);
        setMultiSelectMode(MultiSelectMode.DEFAULT);
        setSelectable(true);
        setBuffered(true);
        setPageLength(BATCH_SIZE);

        setContainerDataSource(jpaContainer);
        setVisibleColumns(new Object[] {});
        initColumns();
    }

    protected void addColumn(String name) {
        Class<?> type = jpaContainer.getType(name);
        addColumn(name, type);
    }

    protected void addColumn(String name, Class<?> type) {
        addContainerProperty(name, type, null, translationService.get(name), null, null);
    }

    protected void addColumn(String name, Converter<String, ?> converter) {
        addColumn(name);
        setConverter(name, converter);
    }

    protected void addColumn(String name, ColumnGenerator columnGenerator) {
        addColumn(name);
        addGeneratedColumn(name, columnGenerator);
    }

    public void enableRefresh() {
        jpaContainer.enable();
    }

    @Override
    public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
        jpaContainer.addContainerFilter(filter);
    }

    @Override
    public void removeContainerFilter(Filter filter) {
        jpaContainer.removeContainerFilter(filter);
    }

    @Override
    public void removeAllContainerFilters() {
        jpaContainer.removeAllContainerFilters();
    }

    @SuppressWarnings("unchecked")
    public ENTITY getSelectedEntity() {
        Long id = (Long) getValue();
        EntityItem<ENTITY> item = (EntityItem<ENTITY>) getItem(id);
        ENTITY entity = item.getEntity();
        return entity;
    }
}
