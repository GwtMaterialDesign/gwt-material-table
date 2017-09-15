package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import gwt.material.design.client.data.DataView;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.MouseEvent;

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
