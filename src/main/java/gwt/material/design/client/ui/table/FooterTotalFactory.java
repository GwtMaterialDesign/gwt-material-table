package gwt.material.design.client.ui.table;

import gwt.material.design.client.ui.table.cell.Column;

import java.util.HashMap;
import java.util.Map;

public class FooterTotalFactory<T> {

    private  Map<String, FooterTotalRow.FooterTotalValueProvider<T>> map = new HashMap<>();

    public void addTotalColumn(String column, FooterTotalRow.FooterTotalValueProvider<T> valueProvider) {
        map.put(column, valueProvider);
    }

    public void addTotalColumn(Column<T, ?> column, FooterTotalRow.FooterTotalValueProvider<T> valueProvider) {
        addTotalColumn(column.name(), valueProvider);
    }

    public FooterTotalRow.FooterTotalValueProvider<T> get(String columnName) {
        return map.get(columnName);
    }

}
