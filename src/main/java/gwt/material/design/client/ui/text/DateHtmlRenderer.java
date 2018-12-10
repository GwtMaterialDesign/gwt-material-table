package gwt.material.design.client.ui.text;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class DateHtmlRenderer extends AbstractSafeHtmlRenderer<String> {

    public DateHtmlRenderer() {
    }

    public DateHtmlRenderer(SafeHtml defaultHtml) {
        super(defaultHtml);
    }

    @Override
    public SafeHtml safeRender(java.lang.String object) {
        return SafeHtmlUtils.fromString(object);
    }
}
