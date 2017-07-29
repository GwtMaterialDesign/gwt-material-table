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
package gwt.material.design.client.ui.table.events;

import gwt.material.design.jquery.client.api.JQueryElement;

/**
 * Row expansion block.
 *
 * @author Ben Dol
 */
public class RowExpansion<T> {
    final T model;
    final JQueryElement row;
    final JQueryElement overlay;

    public RowExpansion(T model, JQueryElement row) {
        this.model = model;
        this.row = row;
        this.overlay = row.find("section.overlay");
    }

    public T getModel() {
        return model;
    }

    public JQueryElement getRow() {
        return row;
    }

    public JQueryElement getOverlay() {
        return overlay;
    }
}
