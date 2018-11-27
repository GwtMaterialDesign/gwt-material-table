package gwt.material.design.client.ui.text;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class NumberHtmlRenderer<T extends Number> extends AbstractSafeHtmlRenderer<T> {

    public NumberHtmlRenderer() {
        this(SafeHtmlUtils.fromString("-"));
    }

    public NumberHtmlRenderer(SafeHtml defaultHtml) {
        super(defaultHtml);
    }

    @Override
    public SafeHtml safeRender(T object) {
        return SafeHtmlUtils.fromString(NumberFormat.getDecimalFormat().format(object));
    }
}
