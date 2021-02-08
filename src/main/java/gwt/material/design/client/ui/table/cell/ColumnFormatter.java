package gwt.material.design.client.ui.table.cell;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;

public class ColumnFormatter {

    private DateTimeFormat dateFormat;
    private NumberFormat integerFormat;
    private NumberFormat longFormat;
    private NumberFormat doubleFormat;
    private NumberFormat floatFormat;
    private NumberFormat bigDecimalFormat;
    private NumberFormat shortFormat;

    public DateTimeFormat getDateFormat() {
        return dateFormat;
    }

    public ColumnFormatter setDateFormat(DateTimeFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public NumberFormat getIntegerFormat() {
        return integerFormat;
    }

    public ColumnFormatter setIntegerFormat(NumberFormat integerFormat) {
        this.integerFormat = integerFormat;
        return this;
    }

    public NumberFormat getLongFormat() {
        return longFormat;
    }

    public ColumnFormatter setLongFormat(NumberFormat longFormat) {
        this.longFormat = longFormat;
        return this;
    }

    public NumberFormat getDoubleFormat() {
        return doubleFormat;
    }

    public ColumnFormatter setDoubleFormat(NumberFormat doubleFormat) {
        this.doubleFormat = doubleFormat;
        return this;
    }

    public NumberFormat getFloatFormat() {
        return floatFormat;
    }

    public ColumnFormatter setFloatFormat(NumberFormat floatFormat) {
        this.floatFormat = floatFormat;
        return this;
    }

    public NumberFormat getBigDecimalFormat() {
        return bigDecimalFormat;
    }

    public ColumnFormatter setBigDecimalFormat(NumberFormat bigDecimalFormat) {
        this.bigDecimalFormat = bigDecimalFormat;
        return this;
    }

    public NumberFormat getShortFormat() {
        return shortFormat;
    }

    public ColumnFormatter setShortFormat(NumberFormat shortFormat) {
        this.shortFormat = shortFormat;
        return this;
    }
}
