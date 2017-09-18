package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.RowExpansion;

public class RowCollapsingEvent<T> extends GwtEvent<RowCollapsingHandler<T>> {

    public static final Type<RowCollapsingHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, RowExpansion<T> expansion) {
        source.fireEvent(new RowCollapsingEvent<>(expansion));
    }

    private final RowExpansion<T> expansion;

    public RowCollapsingEvent(RowExpansion<T> expansion) {
        this.expansion = expansion;
    }

    public RowExpansion<T> getExpansion() {
        return expansion;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<RowCollapsingHandler<T>> getAssociatedType() {
        return (Type)TYPE;
    }

    @Override
    protected void dispatch(RowCollapsingHandler<T> handler) {
        handler.onRowCollapsing(this);
    }
}
