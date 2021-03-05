package gwt.material.design.client.ui.table;

import gwt.material.design.client.ui.table.cell.Column;

public class FooterColumn<T> {

    private Column<T, ?> column;
    private final TableFooter.FooterTotalValueProvider<T> valueProvider;

    public FooterColumn(TableFooter.FooterTotalValueProvider<T> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public FooterColumn(Column<T, ?> column, TableFooter.FooterTotalValueProvider<T> valueProvider) {
        this.column = column;
        this.valueProvider = valueProvider;
    }

    public void setColumn(Column<T, ?> columnName) {
        this.column = columnName;
    }

    public Column<T, ?> getColumn() {
        return column;
    }

    public TableFooter.FooterTotalValueProvider<T> getValueProvider() {
        return valueProvider;
    }
}
