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
package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;

import java.util.List;

public class SelectAllEvent<T> extends GwtEvent<SelectAllHandler<T>> {

    public final static Type<SelectAllHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, List<T> models, List<Element> rows, boolean selected) {
        source.fireEvent(new SelectAllEvent<>(models, rows, selected));
    }

    private final List<T> models;
    private final List<Element> rows;
    private final boolean selected;

    public SelectAllEvent(List<T> models, List<Element> rows, boolean selected) {
        this.models = models;
        this.rows = rows;
        this.selected = selected;
    }

    public List<T> getModels() {
        return models;
    }

    public List<Element> getRows() {
        return rows;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<SelectAllHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelectAllHandler<T> handler) {
        handler.onSelectAll(this);
    }
}
