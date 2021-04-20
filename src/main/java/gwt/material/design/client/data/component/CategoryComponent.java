/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
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
package gwt.material.design.client.data.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.data.HasCategories;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableSubHeader;
import gwt.material.design.client.ui.table.cell.CategoryColumn;
import gwt.material.design.client.ui.table.cell.CategoryValueProvider;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * @author Ben Dol
 */
public class CategoryComponent<T> extends Component<TableSubHeader> {

    public static class OrphanCategoryComponent<T> extends CategoryComponent<T> {
        public OrphanCategoryComponent(HasCategories<T> parent) {
            super(parent, null, null);
        }

        @Override
        protected void render(TableSubHeader subheader, int columnCount) {
            super.render(subheader, columnCount);

            subheader.setName("No Category");
        }
    }

    private Object id;
    private String name;
    private String height;
    private boolean openByDefault;
    private boolean hideName;

    private int currentIndex = -1;
    private int rowCount = 0;

    private HasCategories<T> parent;
    private List<RowComponent<T>> rows = new ArrayList<>();
    private Map<String, TableHeader> tableHeaderMap = new HashMap<>();
    private Map<String, CategoryColumn<T>> columnsMap = new HashMap<>();

    public CategoryComponent(HasCategories<T> parent, String name, Object id) {
        this(parent, name, id, false);
    }

    public CategoryComponent(HasCategories<T> parent, String name, Object id, boolean openByDefault) {
        this.parent = parent;
        this.name = name;
        this.id = id;
        this.openByDefault = openByDefault;
    }

    /**
     * Open this category if we are rendered.
     */
    public void open() {
        if (isRendered()) {
            parent.openCategory(this);
        }
    }

    /**
     * Close this category if we are rendered.
     */
    public void close() {
        if (isRendered()) {
            parent.closeCategory(this);
        }
    }

    /**
     * Toggle the open/close state of this category.
     */
    public void toggle() {
        if (isOpen()) {
            close();
        } else {
            open();
        }
    }

    public String getName() {
        return name;
    }

    public Object getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Render the data category row element.
     * Customization to the element can be performed here.
     */
    protected void render(TableSubHeader subheader, int columnCount) {
        // Do nothing by default
    }

    /**
     * Render the data category row element.
     *
     * @return a fully formed {@link TableSubHeader} object.
     */
    public final TableSubHeader render(int columnCount) {
        TableSubHeader element = getWidget();
        if (element == null) {
            element = new TableSubHeader(this, columnCount);
            setWidget(element);
        }

        render(element, columnCount);
        return element;
    }

    public boolean isOpen() {
        return isRendered() && getWidget().isOpen();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isOpenByDefault() {
        return openByDefault;
    }

    public void setOpenByDefault(boolean openByDefault) {
        this.openByDefault = openByDefault;

        if (isRendered() && openByDefault && !isOpen()) {
            open();
        }
    }

    public boolean isHideName() {
        return hideName;
    }

    public void setHideName(boolean hideName) {
        this.hideName = hideName;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;

        Widget widget = getWidget();
        if (widget != null && widget.isAttached()) {
            widget.setHeight(height);
        }
    }

    public CategoryComponent<T> addColumn(CategoryColumn<T> categoryColumn) {
        if (categoryColumn != null && categoryColumn.getColumn() != null && categoryColumn.getColumn().name() != null) {
            columnsMap.put(categoryColumn.getColumn().name(), categoryColumn);
        }
        return this;
    }

    public void buildColumns(List<Column<T, ?>> allColumns) {
        List<CategoryColumn<T>> categoryColumns = getColumns();
        TableHeader parentTh = new TableHeader();
        parentTh.addStyleName("category-parent");
        for (int i = 1; i < allColumns.size(); i++) {
            Column<T, ?> column = allColumns.get(i);
            if (column != null && !column.name().isEmpty()) {
                TableHeader th = new TableHeader();
                th.addStyleName("category");
                parentTh.add(th);
                tableHeaderMap.put(column.name(), th);
            }
        }

        getWidget().add(parentTh);

        if (categoryColumns != null) {
            for (CategoryColumn<T> categoryColumn : categoryColumns) {
                CategoryValueProvider<T> valueProvider = categoryColumn.getValueProvider();
                if (valueProvider != null && categoryColumn.getColumn() != null) {
                    Column<T, ?> column = categoryColumn.getColumn();
                    TableHeader tableHeader = tableHeaderMap.get(column.name());
                    tableHeader.add(new MaterialLabel(valueProvider.getValue(this)));
                }
            }
        }
    }

    public void recalculateColumns() {
        for (String columnName : tableHeaderMap.keySet()) {
            TableHeader tableHeader = tableHeaderMap.get(columnName);
            //TODO: Issue with parenting
            String display = $("td[data-title='" + columnName + "']").css("display");
            if (display != null) {
                tableHeader.getElement().getStyle().setProperty("display", display);
            }
        }
    }

    public TableHeader getHeader(int index) {
        return getWidget().getHeader(index);
    }

    public TableHeader getHeader(String name) {
        return getWidget().getHeader(name);
    }

    @Override
    public void setWidget(TableSubHeader widget) {
        // copy the elements style and info
        widget.copy(getWidget());

        super.setWidget(widget);
    }

    public List<T> getData() {
        return RowComponent.extractData(rows);
    }

    public void addRow(RowComponent<T> rowComponent) {
        if (!rows.contains(rowComponent)) {
            rows.add(rowComponent);
        }
    }

    public List<RowComponent<T>> getRows() {
        return rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryComponent<T> that = (CategoryComponent<T>) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public Map<String, CategoryColumn<T>> getColumnsMap() {
        return columnsMap;
    }

    public List<CategoryColumn<T>> getColumns() {
        return new ArrayList<>(columnsMap.values());
    }
}
