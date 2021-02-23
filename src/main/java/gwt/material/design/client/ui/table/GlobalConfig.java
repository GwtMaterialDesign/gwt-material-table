package gwt.material.design.client.ui.table;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import gwt.material.design.client.ui.table.cell.ColumnFormatProvider;

public class GlobalConfig {

    private ColumnFormatProvider defaultFormatProvider = new ColumnFormatProvider();
    private String defaultBlankPlaceholder = "";

    public GlobalConfig() {
        defaultFormatProvider.setDateFormat(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL));
        defaultFormatProvider.setIntegerFormat(NumberFormat.getFormat("#"));
        defaultFormatProvider.setLongFormat(NumberFormat.getFormat("#"));
        defaultFormatProvider.setDoubleFormat(NumberFormat.getDecimalFormat());
        defaultFormatProvider.setFloatFormat(NumberFormat.getDecimalFormat());
        defaultFormatProvider.setBigDecimalFormat(NumberFormat.getDecimalFormat());
        defaultFormatProvider.setShortFormat(NumberFormat.getFormat("#"));
    }

    public ColumnFormatProvider getDefaultFormatProvider() {
        return defaultFormatProvider;
    }

    public void setDefaultFormatProvider(ColumnFormatProvider defaultFormatProvider) {
        this.defaultFormatProvider = defaultFormatProvider;
    }

    public String getDefaultBlankPlaceholder() {
        return defaultBlankPlaceholder;
    }

    public void setDefaultBlankPlaceholder(String defaultBlankPlaceholder) {
        this.defaultBlankPlaceholder = defaultBlankPlaceholder;
    }
}
