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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.factory.FooterColumnsFactory;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.FooterColumn;
import gwt.material.design.client.ui.table.cell.FooterValueProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFooter<T> extends MaterialWidget {

    private Map<String, MaterialLabel> widgetFactory = new HashMap<>();
    private FooterColumnsFactory<T> columnsFactory = new FooterColumnsFactory<>();
    private final AbstractDataTable<T> dataTable;
    private final TableRow tableRow = new TableRow();
    private final List<Column<T, ?>> columns;

    public TableFooter(AbstractDataTable<T> dataTable) {
        super(Document.get().createTFootElement());

        this.dataTable = dataTable;
        this.columns = dataTable.getColumns();
    }

    public void load() {
        if (dataTable != null && columnsFactory != null) {
            for (Column<T, ?> column : columns) {
                TableData tableData = new TableData();
                MaterialLabel label = new MaterialLabel();
                tableData.add(label);

                FooterColumn<T> footer = column.getFooter();
                if (footer != null) {
                    columnsFactory.addFooterColumn(footer);
                }

                widgetFactory.put(column.name(), label);
                tableRow.add(tableData);
            }

            onComponentsRendered();
        }
        add(tableRow);
    }

    protected void onComponentsRendered() {
        dataTable.addComponentsRenderedHandler(event -> updateFooterValues());
        dataTable.addRowEmptyHandler(event -> setVisible(false));
    }

    protected void updateFooterValues() {
        List<T> entireData = dataTable.getView().getData();
        if (columnsFactory != null) {
            for (Column<T, ?> column : columns) {
                FooterValueProvider<T> valueProvider = columnsFactory.get(column.name());
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
        setVisible(true);
    }

    public void setColumnsFactory(FooterColumnsFactory<T> columnsFactory) {
        this.columnsFactory = columnsFactory;
    }
}
