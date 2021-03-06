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
package org.vaadin.addons.javaee.form;

import javax.enterprise.context.Dependent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.addons.javaee.jpa.EntityItem;
import org.vaadin.addons.javaee.table.BasicEntityTable;

import com.googlecode.javaeeutils.jpa.PersistentEntity;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;

@Dependent
public abstract class BasicEntityForm<ENTITY extends PersistentEntity> extends BasicForm<ENTITY> {

    private static Log log = LogFactory.getLog(BasicSearchForm.class);

    public BasicEntityForm(Class<ENTITY> entityClass) {
        super(entityClass);
    }

    public void edit(EntityItem<ENTITY> item) {
        fieldGroup.setItem(item);
    }

    public void editNew() {
        try {
            EntityItem<ENTITY> item = new EntityItem<ENTITY>(getContainer(), getDefaultValue());
            fieldGroup.setItem(item);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("Could not instanciate " + entityClass, e);
        }
    }

    /**
     * Sets the columns of the default section (first section)
     */
    protected void setColumns(int columns) {
        getDefaultSection().setColumns(columns);
    }

    public ENTITY getEntity() {
        EntityItem<ENTITY> item = fieldGroup.getItem();
        return item.getEntity();
    }

    public void connectWith(BasicEntityTable<ENTITY> table) {
        table.addListener(new ItemClickListener() {

            @Override
            @SuppressWarnings("unchecked")
            public void itemClick(ItemClickEvent event) {
                edit((EntityItem<ENTITY>) event.getItem());
            }
        });
    }

    public void save() {
        try {
            fieldGroup.commit();
        } catch (CommitException e) {
            log.error("Could not save " + entityClass.getSimpleName(), e);
        }
    }
}
