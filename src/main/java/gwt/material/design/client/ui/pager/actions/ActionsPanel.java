/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client.ui.pager.actions;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.accessibility.DataTableAccessibilityControls;
import gwt.material.design.client.accessibility.TriggerCallback;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.MaterialDataTable;

/**
 * Widget for building the action panel - contains a page detail including the arrow next / previous icons.
 *
 * @author kevzlou7979
 */
public class ActionsPanel extends MaterialWidget {

    private final MaterialDataPager pager;
    protected Span actionLabel = new Span();
    protected MaterialIcon iconNext = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
    protected MaterialIcon iconPrev = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);

    public ActionsPanel(MaterialDataPager pager) {
        super(Document.get().createDivElement(), TableCssName.ACTION_PAGE_PANEL);
        this.pager = pager;

        iconNext.addStyleName(TableCssName.ARROW_NEXT);
        iconPrev.addStyleName(TableCssName.ARROW_PREV);
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // Add the previous button
        iconPrev.setTabIndex(0);
        iconPrev.setWaves(WavesType.DEFAULT);
        iconPrev.setCircle(true);
        add(iconPrev);

        // Add the action label
        add(actionLabel);

        // Add the next button
        iconNext.setTabIndex(0);
        iconNext.setWaves(WavesType.DEFAULT);
        iconNext.setCircle(true);
        add(iconNext);

        // Register the handlers
        registerHandler(iconNext.addClickHandler(clickEvent -> pager.next()));
        registerHandler(iconPrev.addClickHandler(clickEvent -> pager.previous()));

        // Register Accessibility Controls
        registerAccessibility(iconNext, event -> pager.next());
        registerAccessibility(iconPrev, event -> pager.previous());
    }

    protected void registerAccessibility(MaterialIcon icon, TriggerCallback callback) {
        MaterialDataTable table = pager.getTable();
        if (table != null) {
            DataTableAccessibilityControls accessibilityControl = table.getView().getAccessibilityControl();
            if (accessibilityControl != null) {
                accessibilityControl.registerWidget(icon, KeyCodes.KEY_ENTER, callback);
            }
        }
    }

    public Span getActionLabel() {
        return actionLabel;
    }

    public MaterialIcon getIconNext() {
        return iconNext;
    }

    public MaterialIcon getIconPrev() {
        return iconPrev;
    }
}
