package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RenderedEvent extends GwtEvent<RenderedHandler> {

    public static final Type<RenderedHandler> TYPE = new Type<>();

    public static void fire(HasHandlers source) {
        source.fireEvent(new RenderedEvent());
    }

    @Override
    public Type<RenderedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RenderedHandler handler) {
        handler.onRendered(this);
    }
}
