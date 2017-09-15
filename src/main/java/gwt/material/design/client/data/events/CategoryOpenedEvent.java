package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CategoryOpenedEvent extends GwtEvent<CategoryOpenedHandler> {

    public static final Type<CategoryOpenedHandler> TYPE = new Type<>();

    public static void fire(HasHandlers source, String name) {
        source.fireEvent(new CategoryOpenedEvent(name));
    }

    private final String name;

    public CategoryOpenedEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Type<CategoryOpenedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CategoryOpenedHandler handler) {
        handler.onCategoryOpened(this);
    }
}
