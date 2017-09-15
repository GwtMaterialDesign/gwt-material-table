package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import gwt.material.design.client.data.DataView;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.MouseEvent;

public class RowDoubleClickEvent<T> extends RowEvent<T, RowDoubleClickHandler<T>> {

    public static final Type<RowDoubleClickHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, Event event, T model, Element row) {
        source.fireEvent(new RowDoubleClickEvent<>(event, model, row));
    }

    public RowDoubleClickEvent(Event event, T model, Element row) {
        super(event, model, row);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowDoubleClickHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowDoubleClickHandler<T> handler) {
        handler.onRowDoubleClick(this);
    }
}
