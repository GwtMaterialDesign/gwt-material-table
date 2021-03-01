package gwt.material.design.client.ui.table;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.List;

public class TableFooterRow extends TableRow {

    private MaterialDataTable<?> dataTable;

    public TableFooterRow(MaterialDataTable<?> dataTable) {
        this.dataTable = dataTable;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        List<? extends Column<?, ?>> columns = dataTable.getColumns();
        for (Column column : columns) {
            TableData tableData = new TableData();
            tableData.add(new MaterialLabel("Data"));
            add(tableData);
        }
    }
}
