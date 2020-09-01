/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2020 GwtMaterialDesign
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
package gwt.material.design.client.data.infinite;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.TableRow;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This will provide a better UX on displaying placeholder rows while requesting
 * data to the server.
 */
public class InfiniteRowLoader<T> extends TableRow {

    public static final String CONTENT_PLACEHOLDER = "content-placeholder";

    protected InfiniteDataView<T> dataView;
    protected Map<String, TableRow> rowMap = new LinkedHashMap<>();
    protected int rowCount;

    public InfiniteRowLoader(InfiniteDataView<T> dataView) {
        this.dataView = dataView;
    }

    /**
     * This will get the last row, then appended the placeholder rows depends
     * on how many requested number of rows.
     */
    public void show() {
        rowCount = dataView.getRows().size();

        RowComponent<T> lastRow = dataView.getRow(rowCount - 1);
        if (lastRow != null) {
            TableRow widget = lastRow.getWidget();
            Widget parent = widget.getParent();
            if (parent instanceof MaterialWidget) {
                // Will determine how many rows are being  requested then we need to attached the placeholder rows
                // to the last row's parent.
                int totalNumberOfRequestedRow = getTotalRequestRows();
                if (totalNumberOfRequestedRow > 0) {
                    MaterialWidget hasWidgets = (MaterialWidget) parent;
                    for (int i = 0; i < totalNumberOfRequestedRow; i++) {
                        hasWidgets.insert(buildPlaceholderRows(), calculateInsertIndex(lastRow));
                    }
                }
            }
        }
    }

    /**
     * This will clear all the Placeholder Rows and detach from it's parent.
     */
    public void hide() {
        rowMap.forEach((s, widgets) -> widgets.removeFromParent());
    }

    /**
     * This will calculate the placeholder index needed when inserting the placeholders into it's parent.
     */
    protected int calculateInsertIndex(RowComponent<T> lastRow) {
        int placeholderIndex = rowCount + 1;
        if (dataView.isUseCategories()) {
            CategoryComponent category = dataView.getCategory(lastRow.getCategoryName());
            if (category != null) {
                int categoryIndex = dataView.getCategories().indexOf(category);
                placeholderIndex = placeholderIndex + (categoryIndex + 1);
            }
        }
        return placeholderIndex;
    }

    /**
     * Will build a Table Row containing the placeholders or the loading grey panels.
     */
    protected TableRow buildPlaceholderRows() {
        List<Column<T, ?>> columns = dataView.getColumns();
        TableRow tableRow = new TableRow();
        tableRow.setId(DOM.createUniqueId());
        for (int k = 0; k < columns.size(); k++) {
            MaterialPanel contentPlaceholder = new MaterialPanel();
            contentPlaceholder.addStyleName(CONTENT_PLACEHOLDER);
            TableData tableData = new TableData();
            tableData.add(contentPlaceholder);
            tableRow.add(tableData);
        }
        rowMap.put(tableRow.getId(), tableRow);
        return tableRow;
    }

    public int getTotalRequestRows() {
        return dataView.loaderIndex - dataView.getVisibleRange().getStart();
    }
}
