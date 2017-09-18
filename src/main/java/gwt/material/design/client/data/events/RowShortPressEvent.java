package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import gwt.material.design.client.data.DataView;
import gwt.material.design.jquery.client.api.Event;

public class RowShortPressEvent<T> extends RowEvent<T, RowShortPressHandler<T>> {

    public static final Type<RowShortPressHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, Event event, T model, Element row) {
        source.fireEvent(new RowShortPressEvent<>(event, model, row));
    }

    public RowShortPressEvent(Event event, T model, Element row) {
        super(event, model, row);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowShortPressHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowShortPressHandler<T> handler) {
        handler.onRowShortPress(this);
    }
}
