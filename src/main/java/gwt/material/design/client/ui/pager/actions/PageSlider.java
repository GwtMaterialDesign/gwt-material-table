package gwt.material.design.client.ui.pager.actions;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.pager.MaterialDataPager;

public class PageSlider extends MaterialPanel {

    protected MaterialDataPager pager;
    protected MaterialButton next = new MaterialButton();
    protected MaterialButton previous = new MaterialButton();

    public PageSlider() {
        next.addStyleName("slide-next");
        previous.addStyleName("slide-previous");
        next.setIconType(IconType.KEYBOARD_ARROW_RIGHT);
        next.setWaves(WavesType.DEFAULT);
        previous.setIconType(IconType.KEYBOARD_ARROW_LEFT);
        previous.setWaves(WavesType.DEFAULT);
    }

    public <T> PageSlider(MaterialDataPager pager) {
        this();

        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        next.addClickHandler(clickEvent -> pager.next());
        previous.addClickHandler(clickEvent -> pager.previous());

        add(next);
        add(previous);
    }

    public MaterialButton getNext() {
        return next;
    }

    public MaterialButton getPrevious() {
        return previous;
    }
}
