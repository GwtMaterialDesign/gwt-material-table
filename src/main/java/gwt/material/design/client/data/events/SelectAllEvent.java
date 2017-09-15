package gwt.material.design.client.data.events;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;

import java.util.List;

public class SelectAllEvent<T> extends GwtEvent<SelectAllHandler<T>> {

    public final static Type<SelectAllHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source, List<T> models, List<Element> rows, boolean selected) {
        source.fireEvent(new SelectAllEvent<>(models, rows, selected));
    }

    private final List<T> models;
    private final List<Element> rows;
    private final boolean selected;

    public SelectAllEvent(List<T> models, List<Element> rows, boolean selected) {
        this.models = models;
        this.rows = rows;
        this.selected = selected;
    }

    public List<T> getModels() {
        return models;
    }

    public List<Element> getRows() {
        return rows;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<SelectAllHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(SelectAllHandler<T> handler) {
        handler.onSelectAll(this);
    }
}
