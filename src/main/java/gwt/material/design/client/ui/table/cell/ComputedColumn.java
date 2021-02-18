package gwt.material.design.client.ui.table.cell;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.data.ColumnContext;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.table.MaterialDataTable;

import java.util.List;

public class ComputedColumn<T, N extends Number> extends NumberColumn<T, N> {

    public ComputedColumn() {
        super(new NumberCell<>());
    }

    public N compute(RowComponent<T> row) {
        return null;
    }

    public void render(ColumnContext<T> columnContext, Number computedValue) {
        N value = computedValue != null ? (N) computedValue : defaultValue;
        Div wrapper = new Div();
        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        NumberCell<N> numberCell = (NumberCell) getCell();
        numberCell.setFormat(format);
        delegate(obj -> value);
        numberCell.render(columnContext.getContext(), value, sb);
        wrapper.getElement().setInnerHTML(sb.toSafeHtml().asString());
        wrapper.setStyleName(TableCssName.CELL);
        wrapper.addStyleName(TableCssName.COMPUTED_CELL);
        columnContext.getTableData().add(wrapper);
    }

    @Override
    public NumberFormat getDefaultFormat() {
        return MaterialDataTable.getDefaultColumnFormatter().getLongFormat();
    }
}
