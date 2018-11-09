package gwt.material.design.client.ui.text;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class TextHtmlRenderer extends AbstractSafeHtmlRenderer<String> {

    public TextHtmlRenderer() {
        this(SafeHtmlUtils.fromString("-"));
    }

    public TextHtmlRenderer(SafeHtml defaultHtml) {
        super(defaultHtml);
    }

    @Override
    public SafeHtml safeRender(T object) {
        return SafeHtmlUtils.fromString(object.toString());
    }
}
