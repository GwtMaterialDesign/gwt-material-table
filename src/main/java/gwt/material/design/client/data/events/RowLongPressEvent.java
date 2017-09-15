package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import gwt.material.design.client.data.DataView;
import gwt.material.design.jquery.client.api.Event;

public class RowLongPressEvent<T> extends RowEvent<T, RowLongPressHandler<T>> {

    public static final Type<RowLongPressHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, Event event, T model, Element row) {
        source.fireEvent(new RowLongPressEvent<>(event, model, row));
    }

    public RowLongPressEvent(Event event, T model, Element row) {
        super(event, model, row);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowLongPressHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowLongPressHandler<T> handler) {
        handler.onRowLongPress(this);
    }
}
