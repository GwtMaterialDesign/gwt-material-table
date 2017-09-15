package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ComponentsRenderedEvent extends GwtEvent<ComponentsRenderedHandler> {

    public static final Type<ComponentsRenderedHandler> TYPE = new Type<>();

    public static void fire(HasHandlers source) {
        source.fireEvent(new ComponentsRenderedEvent());
    }

    @Override
    public Type<ComponentsRenderedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ComponentsRenderedHandler handler) {
        handler.onComponentsRendered(this);
    }
}
