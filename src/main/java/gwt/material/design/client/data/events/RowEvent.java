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
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.jquery.Event;
import gwt.material.design.jquery.MouseEvent;

public abstract class RowEvent<T, H extends EventHandler> extends GwtEvent<H> {

    private final Event event;
    private final T model;
    private final Element row;

    public RowEvent(Event event, T model, Element row) {
        this.event = event;
        this.model = model;
        this.row = row;
    }

    /**
     * Get the event source. This can be null if no source event triggered the {@link RowEvent}.
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Attempt to cast the event as a {@link MouseEvent}.
     */
    public MouseEvent getMouseEvent() {
        return (MouseEvent) getEvent();
    }

    /**
     * Model of the row.
     */
    public T getModel() {
        return model;
    }

    /**
     * The rows element.
     */
    public Element getRow() {
        return row;
    }
}
