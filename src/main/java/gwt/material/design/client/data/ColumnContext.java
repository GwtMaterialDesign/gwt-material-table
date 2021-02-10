package gwt.material.design.client.data;

import com.google.gwt.cell.client.Cell;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.cell.Column;

public class ColumnContext<T> {

    protected final Cell.Context context;
    protected final Column<T, ?> column;
    protected final TableData tableData;

    public ColumnContext(Column<T, ?> column, TableData tableData, Cell.Context context) {
        this.column = column;
        this.tableData = tableData;
        this.context = context;
    }

    public Column<T, ?> getColumn() {
        return column;
    }

    public TableData getTableData() {
        return tableData;
    }

    public Cell.Context getContext() {
        return context;
    }
}
