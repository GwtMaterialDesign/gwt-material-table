package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.RowExpansion;

public class RowCollapsedEvent<T> extends GwtEvent<RowCollapsedHandler<T>> {

    public static final Type<RowCollapsedHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, RowExpansion<T> expansion) {
        source.fireEvent(new RowCollapsedEvent<>(expansion));
    }

    private final RowExpansion<T> expansion;

    public RowCollapsedEvent(RowExpansion<T> expansion) {
        this.expansion = expansion;
    }

    public RowExpansion<T> getExpansion() {
        return expansion;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowCollapsedHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowCollapsedHandler<T> handler) {
        handler.onRowCollapsed(this);
    }
}
