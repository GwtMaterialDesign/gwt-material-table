package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.MouseEvent;

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
