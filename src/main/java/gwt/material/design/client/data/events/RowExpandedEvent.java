package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.RowExpansion;

public class RowExpandedEvent<T> extends GwtEvent<RowExpandedHandler<T>> {

    public final static Type<RowExpandedHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, RowExpansion<T> expansion) {
        source.fireEvent(new RowExpandedEvent<>(expansion));
    }

    private final RowExpansion<T> expansion;

    public RowExpandedEvent(RowExpansion<T> expansion) {
        this.expansion = expansion;
    }

    public RowExpansion<T> getExpansion() {
        return expansion;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowExpandedHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowExpandedHandler<T> handler) {
        handler.onRowExpanded(this);
    }
}
