package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CategoryClosedEvent extends GwtEvent<CategoryClosedHandler> {

    public static final Type<CategoryClosedHandler> TYPE = new Type<>();

    public static void fire(HasHandlers source, String name) {
        source.fireEvent(new CategoryClosedEvent(name));
    }

    private final String name;

    public CategoryClosedEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Type<CategoryClosedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CategoryClosedHandler handler) {
        handler.onCategoryClosed(this);
    }
}
