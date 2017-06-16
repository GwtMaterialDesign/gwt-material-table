package gwt.material.design.client.ui.table;

import com.google.gwt.user.client.Window;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.ui.html.Div;

public class XScrollPanel extends Div {

    private Div scrollBar = new Div();

    public XScrollPanel() {
        super("x-scroll");

        add(scrollBar);

        super.setStyle("width: calc(100% - "+ JQueryExtension.scrollBarWidth() +"px)");
        setHeight("16px");
    }

    @Override
    public void setWidth(String width) {
        scrollBar.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        scrollBar.setHeight(height);
    }
}
