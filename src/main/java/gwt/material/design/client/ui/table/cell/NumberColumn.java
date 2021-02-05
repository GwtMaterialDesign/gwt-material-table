package gwt.material.design.client.ui.table.cell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

public class NumberColumn<T, N extends Number> extends Column<T, N> {

    protected NumberFormat format;

    public NumberColumn() {
        super(new NumberCell<>());
    }

    public NumberColumn(NumberFormat format) {
        this();
        this.format = format;
    }

    public NumberColumn(Cell<N> cell) {
        super(cell);
    }

    public NumberColumn(Cell<N> cell, N defaultValue) {
        super(cell, defaultValue);
    }

    public NumberColumn(Cell<N> cell, Value<T, N> delegate) {
        super(cell, delegate);
    }

    public NumberColumn(Cell<N> cell, Value<T, N> delegate, N defaultValue) {
        super(cell, delegate, defaultValue);
    }

    public NumberColumn<T, N> format(NumberFormat format) {
        this.format = format;
        return this;
    }

    @Override
    public Column<T, N> render(Cell.Context context, T object, SafeHtmlBuilder sb) {
        N value = getValue(object);
        if (value != null) {
            sb.append(SimpleSafeHtmlRenderer.getInstance().render(format != null ? format.format(value) : String.valueOf(value)));
        }
        return this;
    }
}
