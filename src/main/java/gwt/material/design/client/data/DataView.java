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
package gwt.material.design.client.data;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasKeyProvider;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.js.JsTableSubHeaders;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.accessibility.DataTableAccessibilityControls;
import gwt.material.design.client.ui.table.DataDisplay;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableScaffolding;

import java.util.List;

/**
 * A data view is in charge of rendering the data within the data table.
 *
 * @author Ben Dol
 */
public interface DataView<T> extends HasRows<T>, HasColumns<T>, HasDataSource<T>, HasRenderer<T>, HasKeyProvider<T>,
        HasCategories, ViewSettings {

    /**
     * Render the data view components to DOM.
     */
    void render(Components<Component<?>> components);

    /**
     * Render individual data view component to DOM.
     */
    void renderComponent(Component<?> component);

    /**
     * Setup the data view.
     */
    void setup(TableScaffolding scaffolding) throws Exception;

    /**
     * Destroy the data view.
     */
    void destroy();

    /**
     * Invoke a view refresh.
     */
    void refresh();

    /**
     * Get the displays main container.
     */
    Widget getContainer();

    /**
     * Get the data views id.
     */
    String getId();

    /**
     * Set the data views display view.
     */
    void setDisplay(DataDisplay<T> display);

    /**
     * Check if a header with the given index is visible.
     */
    boolean isHeaderVisible(int colIndex);

    /**
     * Get the list of rendered header widgets.
     */
    List<TableHeader> getHeaders();

    /**
     * Get the subheader library API.
     */
    JsTableSubHeaders getSubheaderLib();

    /**
     * Is the data view setup.
     */
    boolean isSetup();

    /**
     * Is the data view rendering.
     */
    boolean isRendering();

    /**
     * Get the visible item count of the data view.
     */
    int getVisibleItemCount();

    /**
     * Get the data views current sort context, or null if no sort context is applied.
     */
    SortContext<T> getSortContext();

    /**
     * Is redrawing the data view.
     */
    boolean isRedraw();

    /**
     * Set redraw data view elements.
     */
    void setRedraw(boolean redraw);

    /**
     * Set the data views loading mask.
     */
    void setLoadMask(boolean loadMask);

    /**
     * Is the data views loading mask applied.
     */
    boolean isLoadMask();

    /**
     * Get the data views progress widget.
     */
    MaterialProgress getProgressWidget();

    /**
     * Controls the datatable's accessibility features including it's component focused states.
     */
    void setAccessibilityControl(DataTableAccessibilityControls accessibilityControl);

    DataTableAccessibilityControls getAccessibilityControl();
}
