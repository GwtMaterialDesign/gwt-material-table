package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.SortContext;

public class ColumnSortEvent<T> extends GwtEvent<ColumnSortHandler<T>> {

    public static final Type<ColumnSortHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, SortContext<T> sortContext, int columnIndex) {
        source.fireEvent(new ColumnSortEvent<>(sortContext, columnIndex));
    }

    private final SortContext<T> sortContext;
    private final int columnIndex;

    public ColumnSortEvent(SortContext<T> sortContext, int columnIndex) {
        this.sortContext = sortContext;
        this.columnIndex = columnIndex;
    }

    public SortContext<T> getSortContext() {
        return sortContext;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<ColumnSortHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(ColumnSortHandler<T> handler) {
        handler.onColumnSort(this);
    }
}
