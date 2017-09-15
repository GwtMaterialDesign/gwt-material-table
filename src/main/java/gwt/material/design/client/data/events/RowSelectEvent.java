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

    public RowSelectEvent(Event event, T model, Element row, boolean selected) {
        super(event, model, row);

        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
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
