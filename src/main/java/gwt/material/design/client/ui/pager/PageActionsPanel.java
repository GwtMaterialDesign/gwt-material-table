package gwt.material.design.client.ui.pager;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Span;

/**
 * Widget for building the action panel - contains a page detail including the arrow next / previous icons.
 *
 * @author kevzlou7979
 */
public class PageActionsPanel extends MaterialWidget {

    private final MaterialDataPager pager;
    protected Span actionLabel = new Span();
    protected MaterialIcon iconNext = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
    protected MaterialIcon iconPrev = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);

    public PageActionsPanel(MaterialDataPager pager) {
        super(Document.get().createDivElement(), TableCssName.ACTION_PAGE_PANEL);
        setGrid("s12 m4 l3");
        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // Add the next button
        iconNext.setWaves(WavesType.DEFAULT);
        iconNext.setCircle(true);
        add(iconNext);

        // Add the action label
        add(actionLabel);

        // Add the previous button
        iconPrev.setWaves(WavesType.DEFAULT);
        iconPrev.setCircle(true);
        add(iconPrev);

        // Register the handlers
        registerHandler(iconNext.addClickHandler(clickEvent -> pager.next()));
        registerHandler(iconPrev.addClickHandler(clickEvent -> pager.previous()));
    }

    public Span getActionLabel() {
        return actionLabel;
    }

    public MaterialIcon getIconNext() {
        return iconNext;
    }

    public MaterialIcon getIconPrev() {
        return iconPrev;
    }
}
