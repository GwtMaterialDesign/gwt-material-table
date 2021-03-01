package gwt.material.design.client.ui.table;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FooterTotalRow<T> extends TableRow {

    private final FooterTotalFactory<T> rowFactory;
    private Map<String, MaterialLabel> widgetFactory = new HashMap<>();
    private final MaterialDataTable<T> dataTable;

    public interface FooterTotalValueProvider<T> {
        String getValue(List<T> entireData);
    }

    public FooterTotalRow(MaterialDataTable<T> dataTable, FooterTotalFactory<T> rowFactory) {
        this.dataTable = dataTable;
        this.rowFactory = rowFactory;

        setup();
    }

    protected void setup() {
        dataTable.addComponentsRenderedHandler(event -> {
            List<T> entireData = dataTable.getView().getData();
            List<? extends Column<?, ?>> columns = dataTable.getColumns();
            for (Column column : columns) {
                FooterTotalValueProvider<T> tColumnValueProvider = rowFactory.get(column.name());
                if (tColumnValueProvider != null) {
                    String value = tColumnValueProvider.getValue(entireData);
                    if (value != null) {
                        MaterialLabel label = widgetFactory.get(column.name());
                        if (label != null) {
                            label.setText(value);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        List<? extends Column<?, ?>> columns = dataTable.getColumns();
        for (Column column : columns) {
            TableData tableData = new TableData();
            MaterialLabel label = new MaterialLabel();
            tableData.add(label);
            widgetFactory.put(column.name(), label);
            add(tableData);
        }
    }
}
