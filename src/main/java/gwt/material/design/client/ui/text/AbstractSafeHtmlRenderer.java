package gwt.material.design.client.ui.text;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public abstract class AbstractSafeHtmlRenderer<T> implements SafeHtmlRenderer<T> {

    private SafeHtml defaultHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;

    public AbstractSafeHtmlRenderer() {
    }

    public AbstractSafeHtmlRenderer(SafeHtml defaultHtml) {
        this.defaultHtml = defaultHtml;
    }

    @Override
    public SafeHtml render(T object) {
        return (object == null) ? defaultHtml : safeRender(object);
    }

    @Override
    public void render(T object, SafeHtmlBuilder builder) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public abstract SafeHtml safeRender(T object);

    public SafeHtml getDefaultHtml() {
        return defaultHtml;
    }

    public void setDefaultHtml(SafeHtml defaultHtml) {
        this.defaultHtml = defaultHtml;
    }
}
