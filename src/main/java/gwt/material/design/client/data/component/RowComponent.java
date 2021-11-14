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

import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.helper.ScrollHelper;
import gwt.material.design.client.data.ColumnContext;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.factory.Category;
import gwt.material.design.client.ui.animate.MaterialAnimation;
import gwt.material.design.client.ui.animate.Transition;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.TableRow;
import gwt.material.design.client.ui.table.cell.ComputedColumn;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.*;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * @author Ben Dol
 */
public class RowComponent<T> extends Component<TableRow> implements Comparable<T>, HasEnabled {
    private final Comparator<T> DEFAULT_COMPARATOR = (o1, o2) -> o1.toString().compareToIgnoreCase(o2.toString());

    private T data;
    private int index;
    private Category categoryInfo;
    private final DataView<T> dataView;
    private Map<String, ColumnContext<T>> columns = new LinkedHashMap<>();
    private Comparator<T> comparator;

    public RowComponent(RowComponent<T> clone) {
        super(clone.getWidget(), clone.isRedraw());
        addAll(clone.getChildren());
        data = clone.data;
        index = clone.index;
        categoryInfo = clone.categoryInfo;
        dataView = clone.dataView;
        comparator = clone.comparator;
    }

    public RowComponent(T data, DataView<T> dataView, Category categoryInfo) {
        this.data = data;
        this.dataView = dataView;
        this.categoryInfo = categoryInfo;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DataView<T> getDataView() {
        return dataView;
    }

    public CategoryComponent<T> getCategory() {
        return categoryInfo != null ? dataView.getCategory(categoryInfo.getName()) : null;
    }

    public String getCategoryName() {
        return categoryInfo != null ? categoryInfo.getName() : null;
    }

    public void setCategoryName(String categoryName) {
        if (categoryInfo != null) {
            this.categoryInfo.setName(categoryName);
            setRedraw(true);
            dataView.addCategory(categoryName);
            dataView.renderComponent(this);
        }
    }

    public Category getCategoryInfo() {
        return categoryInfo;
    }

    public int getLeftFrozenColumns() {
        return dataView.getLeftFrozenColumns();
    }

    public int getRightFrozenColumns() {
        return dataView.getRightFrozenColumns();
    }

    public boolean hasFrozenColumns() {
        return hasLeftFrozen() || hasRightFrozen();
    }

    public boolean hasLeftFrozen() {
        return getLeftFrozenColumns() > 0;
    }

    public boolean hasRightFrozen() {
        return getRightFrozenColumns() > 0;
    }

    /**
     * Expands the row.
     * Also see {@link #collapse()}
     */
    public boolean expand() {
        return dataView.expandRow(this, true);
    }

    /**
     * Collapses the row.
     * Also see {@link #expand()}
     */
    public boolean collapse() {
        return dataView.expandRow(this, false);
    }

    /**
     * Expand or collapse the row.
     * Also see {@link #expand()} and {@link #collapse()}
     */
    public boolean expandOrCollapse() {
        return dataView.expandOrCollapseRow(this);
    }

    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getWidget().setEnabled(enabled);
    }

    @Override
    protected void clearWidget() {
        TableRow row = getWidget();
        if (row != null) {
            clearRowExpansion();
        }
        super.clearWidget();
    }

    public void clearRowExpansion() {
        JQueryElement next = $(getWidget()).next();
        if (next.is("tr.expansion")) {
            next.remove();
        }
    }

    public static <T> List<T> extractData(List<RowComponent<T>> rows) {
        List<T> data = new ArrayList<>();
        for (RowComponent<T> row : rows) {
            if (row != null) {
                data.add(row.getData());
            }
        }
        return data;
    }

    public Comparator<T> getComparator() {
        return comparator;
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(T o) {
        T data = getData();
        // Favour the comparable object if no comparator is provided.
        // Fallback to the default comparator otherwise.
        if (comparator == null) {
            if (data instanceof Comparable) {
                return ((Comparable) data).compareTo(o);
            } else {
                return DEFAULT_COMPARATOR.compare(getData(), o);
            }
        } else {
            return comparator.compare(getData(), o);
        }
    }

    public void addColumn(ColumnContext<T> computedColumn) {
        columns.put(computedColumn.getColumn().name(), computedColumn);
    }

    public List<ColumnContext<T>> getColumns() {
        return new ArrayList<>(columns.values());
    }

    public List<ComputedColumn<T, ?>> getComputedColumns() {
        List<ComputedColumn<T, ?>> computedColumns = new ArrayList<>();
        if (columns != null) {
            for (String key : columns.keySet()) {
                ColumnContext<T> columnTableData = columns.get(key);
                if (columnTableData.getColumn() instanceof ComputedColumn) {
                    computedColumns.add((ComputedColumn<T, ?>) columnTableData.getColumn());
                }
            }
        }
        return computedColumns;
    }

    public ColumnContext<T> getColumnContext(String name) {
        return columns.get(name);
    }

    public void loading(boolean loading) {
        List<ColumnContext<T>> columns = getColumns();
        for (ColumnContext<T> column : columns) {
            TableData tableData = column.getTableData();
            Widget widget = tableData.getWidget(0);
            if (loading) {
                widget.addStyleName("content-placeholder");
            } else {
                highlight();
                widget.removeStyleName("content-placeholder");
            }
        }
    }

    public void highlight() {
        highlight(new DefaultRowHighlightConfig());
    }

    public void highlight(RowHighlightConfig config) {
        MaterialAnimation animation = new MaterialAnimation();
        ScrollHelper helper = new ScrollHelper();
        TableRow row = getWidget();

        if (helper.isInViewPort(row.getElement())) {
            animation.setTransition(config.getTransition());
            animation.setDelay(config.getDelay());
            animation.setDuration(config.getDuration());
            animation.animate(getWidget());
        } else {
            helper.setContainerElement($(dataView.getContainer().getElement()).find(".table-body").asElement());
            helper.setAddedScrollOffset(config.getOffsetTop());
            helper.scrollTo(getWidget());
            helper.setCompleteCallback(() -> animation.animate(getWidget()));
        }
    }

    @Override
    public String toString() {
        return "RowComponent{" +
            "data=" + data +
            ", index=" + index +
            ", categoryInfo='" + categoryInfo + '\'' +
            '}';
    }
}
