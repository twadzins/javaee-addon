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
package org.vaadin.addons.javaee.page;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.addons.javaee.TranslationKeys;
import org.vaadin.addons.javaee.buttons.ButtonBar;
import org.vaadin.addons.javaee.buttons.DeleteButton;
import org.vaadin.addons.javaee.buttons.EditButton;
import org.vaadin.addons.javaee.buttons.NewButton;
import org.vaadin.addons.javaee.buttons.handler.CanHandleDeleteButton;
import org.vaadin.addons.javaee.buttons.handler.CanHandleEditButton;
import org.vaadin.addons.javaee.buttons.handler.CanHandleNewButton;
import org.vaadin.addons.javaee.form.BasicEntityForm;
import org.vaadin.addons.javaee.i18n.TranslationService;
import org.vaadin.addons.javaee.jpa.EntityContainer;
import org.vaadin.addons.javaee.table.BasicEntityTable;

import com.googlecode.javaeeutils.jpa.PersistentEntity;

public abstract class BasicCRUDPage<ENTITY extends PersistentEntity> extends PortalPagePanel implements CanHandleNewButton,
        CanHandleEditButton, CanHandleDeleteButton {

    private static Log log = LogFactory.getLog(BasicCRUDPage.class);

    @Inject
    protected TranslationService translationService;

    protected EntityContainer<ENTITY> container;

    private BasicEntityTable<ENTITY> table;

    private BasicEntityForm<ENTITY> form;

    private ButtonBar buttons = new ButtonBar();

    private NewButton addItemButton;

    private EditButton editItemButton;

    private DeleteButton removeItemButton;

    private AddEditDialog addEditDialog;

    public BasicCRUDPage(final String pageName) {
        super(pageName);
        setSpacing(true);
    }

    public abstract EntityContainer<ENTITY> getContainer();

    public abstract BasicEntityTable<ENTITY> getTable();

    protected abstract BasicEntityForm<ENTITY> getForm();

    @PostConstruct
    public void init() {
        container = getContainer();
        table = getTable();
        form = getForm();

        table.setCaption(translationService.get(container.getEntityClass().getSimpleName() + "s"));
        addComponent(table);
        initButtons();
        addComponent(buttons);

        addEditDialog = new AddEditDialog(container, form, translationService);
    }

    private void initButtons() {
        addItemButton = new NewButton(this, translationService.get(TranslationKeys.BUTTON_NEW));
        buttons.addComponent(addItemButton);
        editItemButton = new EditButton(this, translationService.get(TranslationKeys.BUTTON_EDIT));
        buttons.addComponent(editItemButton);
        removeItemButton = new DeleteButton(this, translationService.get(TranslationKeys.BUTTON_DELETE));
        buttons.addComponent(removeItemButton);
    }

    @Override
    public void onShow(String comingFrom) {
        table.enableRefresh();
    }

    @Override
    public void deleteClicked() {
        if (table.isAnySelected()) {
            table.removeSelectedItem();
        } else {
            log.error("You have to select a " + getPageName() + ".");
        }
    }

    @Override
    public void editClicked() {
        if (table.isAnySelected()) {
            addEditDialog.editSelected(table);
        } else {
            log.error("You have to select a " + getPageName() + ".");
        }
    }

    @Override
    public void newClicked() {
        addEditDialog.editNew();
    }

}
