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
import gwt.material.design.client.data.DataView;
import gwt.material.design.jquery.client.api.Event;

public class RowSelectEvent<T> extends RowEvent<T, RowSelectHandler<T>> {

    public static final Type<RowSelectHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, Event event, T model, Element row, boolean selected) {
        source.fireEvent(new RowSelectEvent<>(event, model, row, selected));
    }

    private final boolean selected;
    private final Event nativeEvent;

    public RowSelectEvent(Event event, T model, Element row, boolean selected) {
        super(event, model, row);

        this.selected = selected;
        this.nativeEvent = event;
    }

    public boolean isSelected() {
        return selected;
    }

    public Event getNativeEvent() {
        return nativeEvent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowSelectHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowSelectHandler<T> handler) {
        handler.onRowSelect(this);
    }
}
