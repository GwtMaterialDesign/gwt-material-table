/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2021 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client.ui.table;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFooter<T> extends MaterialWidget {

    private Map<String, MaterialLabel> widgetFactory = new HashMap<>();
    private FooterRowFactory<T> rowFactory = new FooterRowFactory<>();
    private final AbstractDataTable<T> dataTable;
    private final TableRow tableRow = new TableRow();
    private final List<Column<T, ?>> columns;

    public interface FooterTotalValueProvider<T> {
        String getValue(List<T> entireData);
    }

    public TableFooter(AbstractDataTable<T> dataTable) {
        super(Document.get().createTFootElement());

        this.dataTable = dataTable;
        this.columns = dataTable.getColumns();
    }

    public void load() {
        if (dataTable != null && rowFactory != null) {
            for (Column<T, ?> column : columns) {
                TableData tableData = new TableData();
                MaterialLabel label = new MaterialLabel();
                tableData.add(label);

                FooterColumn<T> footer = column.getFooter();
                if (footer != null) {
                    rowFactory.addFooterColumn(footer);
                }

                widgetFactory.put(column.name(), label);
                tableRow.add(tableData);
            }

            onComponentsRendered();
        }
        add(tableRow);
    }

    protected void onComponentsRendered() {
        dataTable.addComponentsRenderedHandler(event -> {
            List<T> entireData = dataTable.getView().getData();
            if (rowFactory != null) {
                for (Column<T, ?> column : columns) {
                    TableFooter.FooterTotalValueProvider<T> valueProvider = rowFactory.get(column.name());
                    if (valueProvider != null) {
                        String value = valueProvider.getValue(entireData);
                        if (value != null) {
                            MaterialLabel label = widgetFactory.get(column.name());
                            if (label != null) {
                                label.setText(value);
                            }
                        }
                    }
                }
            }
        });
    }

    public void setRowFactory(FooterRowFactory<T> rowFactory) {
        this.rowFactory = rowFactory;
    }
}
