package gwt.material.design.client.ui.table.cell;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.TimeZone;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

import java.util.Date;

public class DateCell extends com.google.gwt.cell.client.DateCell {

    protected DateTimeFormat format;
    protected TimeZone timeZone;

    @Override
    public void render(Context context, Date value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.append(SimpleSafeHtmlRenderer.getInstance().render(format.format(value, timeZone)));
        }
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public void setFormat(DateTimeFormat format) {
        this.format = format;
    }
}
