package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.RowExpansion;

public class RowExpandingEvent<T> extends GwtEvent<RowExpandingHandler<T>> {

    public static final Type<RowExpandingHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, RowExpansion<T> expansion) {
        source.fireEvent(new RowExpandingEvent<>(expansion));
    }

    private final RowExpansion<T> expansion;

    public RowExpandingEvent(RowExpansion<T> expansion) {
        this.expansion = expansion;
    }

    public RowExpansion<T> getExpansion() {
        return expansion;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowExpandingHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(RowExpandingHandler<T> handler) {
        handler.onRowExpanding(this);
    }
}
