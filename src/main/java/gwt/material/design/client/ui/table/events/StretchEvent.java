package gwt.material.design.client.ui.table.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class StretchEvent extends GwtEvent<StretchHandler> {

    public static final Type<StretchHandler> TYPE = new Type<>();

    public static void fire(HasHandlers source, boolean stretched) {
        source.fireEvent(new StretchEvent(stretched));
    }

    private final boolean stretched;

    public StretchEvent(boolean stretched) {
        this.stretched = stretched;
    }

    public boolean isStretched() {
        return stretched;
    }

    @Override
    public Type<StretchHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(StretchHandler handler) {
        handler.onStretch(this);
    }
}
