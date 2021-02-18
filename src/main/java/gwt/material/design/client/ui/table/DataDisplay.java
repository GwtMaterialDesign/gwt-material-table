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
package gwt.material.design.client.ui.table;

import com.google.gwt.user.client.ui.IsWidget;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.HasCategories;
import gwt.material.design.client.data.HasColumns;
import gwt.material.design.client.data.HasDataSource;
import gwt.material.design.client.data.HasRenderer;
import gwt.material.design.client.data.HasRows;
import gwt.material.design.client.data.ViewSettings;
import gwt.material.design.client.data.EventHandlers;
import gwt.material.design.client.events.HandlerRegistry;

/**
 * Data display interface defining the {@link com.google.gwt.user.client.ui.Widget}
 * aspects of the DataTable.
 */
public interface DataDisplay<T> extends IsWidget, EventHandlers<T>, HasRows<T>, HasColumns<T>,
        HasDataSource<T>, HasRenderer<T>, HasCategories<T>, ViewSettings, HandlerRegistry {

    /**
     * Get the displays {@link DataView}.
     */
    DataView<T> getView();

    /**
     * Get the displays scaffolding.
     */
    TableScaffolding getScaffolding();
}
