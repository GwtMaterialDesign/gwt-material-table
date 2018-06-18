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
import gwt.material.design.jquery.Event;
import gwt.material.design.jquery.MouseEvent;

public class RowContextMenuEvent<T> extends RowEvent<T, RowContextMenuHandler<T>> {

    public static final Type<RowContextMenuHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, MouseEvent event, T model, Element row) {
        source.fireEvent(new RowContextMenuEvent<>(event, model, row));
    }

    public RowContextMenuEvent(MouseEvent event, T model, Element row) {
        super(event, model, row);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowContextMenuHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowContextMenuHandler<T> handler) {
        handler.onRowContextMenu(this);
    }
}
