package gwt.material.design.client.ui.pager;

import gwt.material.design.client.base.helper.EnumHelper;
import gwt.material.design.client.constants.CssType;

public enum PagerType implements CssType {

    DEFAULT(""),
    SIMPLE("simple");

    private final String cssClass;

    PagerType(final String cssClass) {
        this.cssClass = cssClass;
    }

    @Override
    public String getCssName() {
        return cssClass;
    }

    public static PagerType fromStyleName(final String styleName) {
        return EnumHelper.fromStyleName(styleName, PagerType.class, DEFAULT);
    }
}
