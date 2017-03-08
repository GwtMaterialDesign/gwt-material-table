package gwt.material.design.client.ui.table;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
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

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.RowCountChangeEvent;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.jquery.client.api.Functions.EventFunc1;
import gwt.material.design.jquery.client.api.Functions.EventFunc2;
import gwt.material.design.jquery.client.api.Functions.EventFunc3;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.jquery.client.api.MouseEvent;
import gwt.material.design.jscore.client.api.core.Element;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.Renderer;
import gwt.material.design.client.data.StandardDataView;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.SortContext;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.factory.CategoryComponentFactory;
import gwt.material.design.client.data.factory.RowComponentFactory;
import gwt.material.design.client.js.JsTableSubHeaders;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.events.RowExpand;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An abstract data table implementation that can be attached to the DOM.
 * The core functionality is based on the registered {@link DataView},
 * this will handle scaffolding, event registration, and attachment.
 *
 * @author Ben Dol
 */
public abstract class AbstractDataTable<T> extends MaterialWidget implements DataView<T>, TableEventHandlers<T> {

    private static final Logger logger = Logger.getLogger(AbstractDataTable.class.getName());

    public static class DefaultTableScaffolding extends TableScaffolding {
        @Override
        public MaterialWidget createTableBody() {
            MaterialWidget tableBody = new MaterialWidget(DOM.createDiv());
            tableBody.addStyleName(TableCssName.TABLE_BODY);
            return tableBody;
        }

        @Override
        public MaterialWidget createTopPanel() {
            MaterialWidget topPanel = new MaterialWidget(DOM.createDiv());
            topPanel.addStyleName(TableCssName.TOP_PANEL);
            return topPanel;
        }

        @Override
        public MaterialWidget createInfoPanel() {
            MaterialWidget infoPanel = new MaterialWidget(DOM.createDiv());
            infoPanel.addStyleName(TableCssName.INFO_PANEL);
            return infoPanel;
        }

        @Override
        public MaterialWidget createToolPanel() {
            MaterialWidget toolPanel = new MaterialWidget(DOM.createDiv());
            toolPanel.addStyleName(TableCssName.TOOL_PANEL);
            return toolPanel;
        }

        @Override
        protected Table createTable() {
            Table table = new Table();
            table.addStyleName(TableCssName.TABLE);
            return table;
        }
    }

    protected DataView<T> dataView;
    protected TableScaffolding scaffolding;
    protected LoadedCallback loadedCallback;

    private boolean setup;
    private boolean focused;
    private boolean refreshing;
    private boolean cellIsEditing;
    private String height;

    public AbstractDataTable() {
        this(new StandardDataView<>());
    }

    public AbstractDataTable(DataView<T> dataView) {
        this(dataView, new DefaultTableScaffolding());
    }

    public AbstractDataTable(TableScaffolding scaffolding) {
        this();
        this.scaffolding = scaffolding;
    }

    public AbstractDataTable(DataView<T> dataView, TableScaffolding scaffolding) {
        super(DOM.createElement("section"));
        this.dataView = dataView;
        this.scaffolding = scaffolding;

        dataView.setDisplay(this);
        setStyleName("table-container");
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if(!setup) {
            // Build the table scaffolding
            scaffolding.build();
            scaffolding.apply(this);

            try {
                setup = true;
                setup(scaffolding);

                if(loadedCallback != null) {
                    loadedCallback.onLoaded();
                }
            } catch (Exception ex) {
                logger.log(Level.SEVERE,
                    "Could not setup AbstractDataTable due to previous errors.", ex);
            }
        }
    }

    @Override
    protected void onUnload() {
        super.onUnload();

        destroy();
        setup = false;
    }

    @Override
    public void setup(TableScaffolding scaffolding) throws Exception {
        dataView.setup(scaffolding);

        if(height != null) {
            setHeight(height);
        }
    }

    @Override
    public void destroy() {
        dataView.destroy();
        removeAllHandlers();
    }

    @Override
    public Panel getContainer() {
        return this;
    }

    @Override
    public String getViewId() {
        return dataView.getViewId();
    }

    @Override
    public void setDisplay(DataView<T> display) {
        // Nothing by default
    }

    @Override
    public void setHeight(String height) {
       this.height = height;

        // Avoid setting the height prematurely.
        if(setup) {
            JQuery.$(scaffolding.getTableBody()).height(height);
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        CellBasedWidgetImpl.get().onBrowserEvent(this, event);

        // Ignore spurious events (such as onblur) while we refresh the table.
        if (refreshing) {
            return;
        }

        // Verify that the target is still a child of this widget. IE fires focus
        // events even after the element has been removed from the DOM.
        EventTarget eventTarget = event.getEventTarget();
        if (!com.google.gwt.dom.client.Element.is(eventTarget)) {
            return;
        }
        com.google.gwt.dom.client.Element target = com.google.gwt.dom.client.Element.as(eventTarget);
        if (!getElement().isOrHasChild(com.google.gwt.dom.client.Element.as(eventTarget))) {
            return;
        }
        super.onBrowserEvent(event);

        String eventType = event.getType();
        if (BrowserEvents.FOCUS.equals(eventType)) {
            // Remember the focus state.
            focused = true;
            onFocus();
        } else if (BrowserEvents.BLUR.equals(eventType)) {
            // Remember the blur state.
            focused = false;
            onBlur();
        } else if (BrowserEvents.KEYDOWN.equals(eventType)) {
            // A key event indicates that we already have focus.
            focused = true;
        } else if (BrowserEvents.MOUSEDOWN.equals(eventType)
            && CellBasedWidgetImpl.get().isFocusable(com.google.gwt.dom.client.Element.as(target))) {
            // If a natively focusable element was just clicked, then we must have
            // focus.
            focused = true;
        }
    }

    protected void onFocus() {
        // Do nothing by default
    }

    protected void onBlur() {
        // Do nothing by default
    }

    /**
     * Get the index of the row value from the associated {@link TableRowElement}.
     *
     * @param row the row element
     * @return the row value index
     */
    public final int getRowValueIndex(TableRowElement row) {
        return row.getSectionRowIndex() + getVisibleRange().getStart();
    }

    /**
     * Fire an event to the Cell within the specified {@link TableCellElement}.
     */
    private <C> void fireEventToCell(Event event, String eventType, com.google.gwt.dom.client.Element parentElem,
                                     final T rowValue, Context context, HasCell<T, C> column) {
        // Check if the cell consumes the event.
        Cell<C> cell = column.getCell();
        if (!cellConsumesEventType(cell, eventType)) {
            return;
        }

        C cellValue = column.getValue(rowValue);
        boolean cellWasEditing = cell.isEditing(context, parentElem, cellValue);
        if (column instanceof Column) {
          /*
           * If the HasCell is a Column, let it handle the event itself. This is
           * here for legacy support.
           */
            Column<T, C> col = (Column<T, C>) column;
            col.onBrowserEvent(context, parentElem, rowValue, event);
        } else {
            // Create a FieldUpdater.
            final FieldUpdater<T, C> fieldUpdater = column.getFieldUpdater();
            final int index = context.getIndex();
            ValueUpdater<C> valueUpdater = (fieldUpdater == null) ? null : (value) -> {
                fieldUpdater.update(index, rowValue, value);
            };

            // Fire the event to the cell.
            cell.onBrowserEvent(context, parentElem, cellValue, event, valueUpdater);
        }

        // Reset focus if needed.
        cellIsEditing = cell.isEditing(context, parentElem, cellValue);
        if (cellWasEditing && !cellIsEditing) {
            CellBasedWidgetImpl.get().resetFocus(() -> {
                setFocus(true);
            });
        }
    }

    /**
     * Check if a cell consumes the specified event type.
     *
     * @param cell the cell
     * @param eventType the event type to check
     * @return true if consumed, false if not
     */
    protected boolean cellConsumesEventType(Cell<?> cell, String eventType) {
        Set<String> consumedEvents = cell.getConsumedEvents();
        return consumedEvents != null && consumedEvents.contains(eventType);
    }

    /**
     * Get the tables scaffolding elements.
     */
    public TableScaffolding getScaffolding() {
        return scaffolding;
    }

    public void setLoadedCallback(LoadedCallback callback) {
        this.loadedCallback = callback;
    }

    // Data View Methods

    @Override
    public void render(Components<Component<?>> components) {
        dataView.render(components);
    }

    @Override
    public void loaded(int startIndex, List<T> data) {
        dataView.loaded(startIndex, data);
    }

    @Override
    public void refreshView() {
        dataView.refreshView();
    }

    @Override
    public void clearRows(boolean clearData) {
        dataView.clearRows(clearData);
    }

    @Override
    public void clearRowsAndCategories(boolean clearData) {
        dataView.clearRowsAndCategories(clearData);
    }

    @Override
    public void clearCategories() {
        dataView.clearCategories();
    }

    @Override
    public List<Column<T, ?>> getColumns() {
        return dataView.getColumns();
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(Handler handler) {
        return dataView.addRangeChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
        return dataView.addRowCountChangeHandler(handler);
    }

    @Override
    public int getRowCount() {
        return dataView.getRowCount();
    }

    @Override
    public Range getVisibleRange() {
        return dataView.getVisibleRange();
    }

    @Override
    public boolean isRowCountExact() {
        return dataView.isRowCountExact();
    }

    @Override
    public void setRowCount(int count) {
        dataView.setRowCount(count);
    }

    @Override
    public void setRowCount(int count, boolean isExact) {
        dataView.setRowCount(count, isExact);
    }

    @Override
    public void setVisibleRange(int start, int length) {
        dataView.setVisibleRange(start, length);
    }

    @Override
    public void setVisibleRange(Range range) {
        dataView.setVisibleRange(range);
    }

    @Override
    public boolean isHeaderVisible(int colIndex) {
        return dataView.isHeaderVisible(colIndex);
    }

    @Override
    public void addColumn(Column<T, ?> column) {
        addColumn(column, "");
    }

    @Override
    public void addColumn(Column<T, ?> column, String header) {
        insertColumn(getColumns().size(), column, header);
    }

    @Override
    public void insertColumn(int beforeIndex, Column<T, ?> col, String header) {
        dataView.insertColumn(beforeIndex, col, header);
    }

    @Override
    public void removeColumn(int colIndex) {
        dataView.removeColumn(colIndex);
    }

    @Override
    public int getColumnOffset() {
        return dataView.getColumnOffset();
    }

    @Override
    public void setSelectionType(SelectionType selectionType) {
        dataView.setSelectionType(selectionType);
    }

    @Override
    public SelectionType getSelectionType() {
        return dataView.getSelectionType();
    }

    @Override
    public void setUseStickyHeader(boolean stickyHeader) {
        dataView.setUseStickyHeader(stickyHeader);
    }

    @Override
    public boolean isUseStickyHeader() {
        return dataView.isUseStickyHeader();
    }

    @Override
    public JsTableSubHeaders getSubheaderLib() {
        return dataView.getSubheaderLib();
    }

    @Override
    public void selectAllRows(boolean select) {
        dataView.selectAllRows(select);
    }

    @Override
    public void selectAllRows(boolean select, boolean fireEvent) {
        dataView.selectAllRows(select, fireEvent);
    }

    @Override
    public void toggleRowSelect(com.google.gwt.dom.client.Element row) {
        dataView.toggleRowSelect(row);
    }

    @Override
    public void toggleRowSelect(com.google.gwt.dom.client.Element row, boolean fireEvent) {
        dataView.toggleRowSelect(row, fireEvent);
    }

    @Override
    public void selectRow(com.google.gwt.dom.client.Element row, boolean fireEvent) {
        dataView.selectRow(row, fireEvent);
    }

    @Override
    public void unselectRow(com.google.gwt.dom.client.Element row, boolean fireEvent) {
        dataView.unselectRow(row, fireEvent);
    }

    @Override
    public boolean hasUnselectedRows(boolean visibleOnly) {
        return dataView.hasUnselectedRows(visibleOnly);
    }

    @Override
    public boolean hasSelectedRows(boolean visibleOnly) {
        return dataView.hasSelectedRows(visibleOnly);
    }

    @Override
    public List<T> getSelectedRowModels(boolean visibleOnly) {
        return dataView.getSelectedRowModels(visibleOnly);
    }

    @Override
    public void setRowData(int start, List<? extends T> values) {
        dataView.setRowData(start, values);
    }

    @Override
    public int getVisibleItemCount() {
        return dataView.getVisibleItemCount();
    }

    @Override
    public int getRowHeight() {
        return dataView.getRowHeight();
    }

    @Override
    public void setRowHeight(int rowHeight) {
        dataView.setRowHeight(rowHeight);
    }

    @Override
    public void setDataSource(DataSource<T> dataSource) {
        dataView.setDataSource(dataSource);
    }

    @Override
    public DataSource<T> getDataSource() {
        return dataView.getDataSource();
    }

    @Override
    public void setRenderer(Renderer<T> renderer) {
        dataView.setRenderer(renderer);
    }

    @Override
    public void addCategory(CategoryComponent category) {
        dataView.addCategory(category);
    }

    @Override
    public boolean hasCategory(String categoryName) {
        return dataView.hasCategory(categoryName);
    }

    @Override
    public void disableCategory(String categoryName) {
        dataView.disableCategory(categoryName);
    }

    @Override
    public List<CategoryComponent> getCategories() {
        return dataView.getCategories();
    }

    @Override
    public List<CategoryComponent> getOpenCategories() {
        return dataView.getOpenCategories();
    }

    @Override
    public boolean isCategoryEmpty(CategoryComponent category) {
        return dataView.isCategoryEmpty(category);
    }

    @Override
    public void setRowFactory(RowComponentFactory<T> rowFactory) {
        dataView.setRowFactory(rowFactory);
    }

    @Override
    public void setCategoryFactory(CategoryComponentFactory categoryFactory) {
        dataView.setCategoryFactory(categoryFactory);
    }

    @Override
    public SortContext<T> getSortContext() {
        return dataView.getSortContext();
    }

    @Override
    public ProvidesKey<T> getKeyProvider() {
        return dataView.getKeyProvider();
    }

    @Override
    public boolean isRedraw() {
        return dataView.isRedraw();
    }

    @Override
    public void setRedraw(boolean redraw) {
        dataView.setRedraw(redraw);
    }

    @Override
    public void setLoadMask(boolean loadMask) {
        dataView.setLoadMask(loadMask);
    }

    @Override
    public boolean isLoadMask() {
        return dataView.isLoadMask();
    }

    @Override
    public MaterialProgress getProgressWidget() {
        return dataView.getProgressWidget();
    }

    @Override
    public int getTotalRows() {
        return dataView.getTotalRows();
    }

    @Override
    public void setTotalRows(int totalRows) {
        dataView.setTotalRows(totalRows);
    }

    @Override
    public boolean isSetup() {
        return dataView.isSetup();
    }

    @Override
    public boolean isUseCategories() {
        return dataView.isUseCategories();
    }

    @Override
    public void setUseCategories(boolean useCategories) {
        dataView.setUseCategories(useCategories);
    }

    @Override
    public boolean isUseLoadOverlay() {
        return dataView.isUseLoadOverlay();
    }

    @Override
    public void setUseLoadOverlay(boolean useLoadOverlay) {
        dataView.setUseLoadOverlay(useLoadOverlay);
    }

    @Override
    public boolean isUseRowExpansion() {
        return dataView.isUseRowExpansion();
    }

    @Override
    public void setUseRowExpansion(boolean useRowExpansion) {
        dataView.setUseRowExpansion(useRowExpansion);
    }

    @Override
    public int getLongPressDuration() {
        return dataView.getLongPressDuration();
    }

    @Override
    public void setLongPressDuration(int longPressDuration) {
        dataView.setLongPressDuration(longPressDuration);
    }

    // Event Handler Methods

    @Override
    public void removeAllHandlers() {
        $this().off("." + getViewId());
    }

    @Override
    public void addSelectAllHandler(EventFunc3<List<T>, List<JQueryElement>, Boolean> handler) {
        $this().on(TableEvents.SELECT_ALL + "." + getViewId(), handler);
    }

    @Override
    public void removeSelectAllHandler(EventFunc3<List<T>, List<JQueryElement>, Boolean> handler) {
        $this().off(TableEvents.SELECT_ALL, handler);
    }

    @Override
    public void removeSelectAllHandlers() {
        $this().off(TableEvents.SELECT_ALL);
    }

    @Override
    public void addRowSelectHandler(EventFunc3<T, Element, Boolean> handler) {
        $this().on(TableEvents.ROW_SELECT + "." + getViewId(), handler);
    }

    @Override
    public void removeRowSelectHandler(EventFunc3<T, Element, Boolean> handler) {
        $this().off(TableEvents.ROW_SELECT, handler);
    }

    @Override
    public void removeRowSelectHandlers() {
        $this().off(TableEvents.ROW_SELECT);
    }

    @Override
    public void addStretchHandler(EventFunc1<Boolean> handler) {
        $this().on(TableEvents.STRETCH + "." + getViewId(), handler);
    }

    @Override
    public void removeStretchHandler(EventFunc1<Boolean> handler) {
        $this().off(TableEvents.STRETCH, handler);
    }

    @Override
    public void removeStretchHandlers() {
        $this().off(TableEvents.STRETCH);
    }

    @Override
    public void addRowExpandHandler(EventFunc1<RowExpand> handler) {
        $this().on(TableEvents.ROW_EXPAND + "." + getViewId(), handler);
    }

    @Override
    public void removeRowExpandHandler(EventFunc1<RowExpand> handler) {
        $this().off(TableEvents.ROW_EXPAND, handler);
    }

    @Override
    public void removeRowExpandHandlers() {
        $this().off(TableEvents.ROW_EXPAND);
    }

    @Override
    public void addRowExpandedHandler(EventFunc1<RowExpand> handler) {
        $this().on(TableEvents.ROW_EXPANDED + "." + getViewId(), handler);
    }

    @Override
    public void removeRowExpandedHandler(EventFunc1<RowExpand> handler) {
        $this().off(TableEvents.ROW_EXPANDED, handler);
    }

    @Override
    public void removeRowExpandedHandlers() {
        $this().off(TableEvents.ROW_EXPANDED);
    }

    @Override
    public void addRowCountChangeHandler(EventFunc2<Integer, Boolean> handler) {
        $this().on(TableEvents.ROW_COUNT_CHANGE + "." + getViewId(), handler);
    }

    @Override
    public void removeRowCountChangeHandler(EventFunc2<Integer, Boolean> handler) {
        $this().off(TableEvents.ROW_COUNT_CHANGE, handler);
    }

    @Override
    public void removeRowCountChangeHandlers() {
        $this().off(TableEvents.ROW_COUNT_CHANGE);
    }

    @Override
    public void addRowContextMenuHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().on(TableEvents.ROW_CONTEXTMENU + "." + getViewId(), handler);
    }

    @Override
    public void removeRowContextMenuHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().off(TableEvents.ROW_CONTEXTMENU, handler);
    }

    @Override
    public void removeRowContextMenuHandlers() {
        $this().off(TableEvents.ROW_CONTEXTMENU);
    }

    @Override
    public void addRowDoubleClickHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().on(TableEvents.ROW_DOUBLECLICK + "." + getViewId(), handler);
    }

    @Override
    public void removeRowDoubleClickHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().off(TableEvents.ROW_DOUBLECLICK, handler);
    }

    @Override
    public void removeRowDoubleClickHandlers() {
        $this().off(TableEvents.ROW_DOUBLECLICK);
    }

    @Override
    public void addRowLongPressHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().on(TableEvents.ROW_LONGPRESS + "." + getViewId(), handler);
    }

    @Override
    public void removeRowLongPressHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().off(TableEvents.ROW_LONGPRESS, handler);
    }

    @Override
    public void removeRowLongPressHandlers() {
        $this().off(TableEvents.ROW_LONGPRESS);
    }

    @Override
    public void addRowShortPressHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().on(TableEvents.ROW_SHORTPRESS + "." + getViewId(), handler);
    }

    @Override
    public void removeRowShortPressHandler(EventFunc3<MouseEvent, T, Element> handler) {
        $this().off(TableEvents.ROW_SHORTPRESS, handler);
    }

    @Override
    public void removeRowShortPressHandlers() {
        $this().off(TableEvents.ROW_SHORTPRESS);
    }

    @Override
    public void addSortColumnHandler(EventFunc2<SortContext, Integer> handler) {
        $this().on(TableEvents.SORT_COLUMN + "." + getViewId(), handler);
    }

    @Override
    public void removeSortColumnHandler(EventFunc2<SortContext, Integer> handler) {
        $this().off(TableEvents.SORT_COLUMN, handler);
    }

    @Override
    public void removeSortColumnHandlers() {
        $this().off(TableEvents.SORT_COLUMN);
    }

    @Override
    public void addCategoryOpenedHandler(EventFunc1<String> handler) {
        $this().on(TableEvents.CATEGORY_OPENED + "." + getViewId(), handler);
    }

    @Override
    public void removeCategoryOpenedHandler(EventFunc1<String> handler) {
        $this().off(TableEvents.CATEGORY_OPENED, handler);
    }

    @Override
    public void removeCategoryOpenedHandlers() {
        $this().off(TableEvents.CATEGORY_OPENED);
    }

    @Override
    public void addCategoryClosedHandler(EventFunc1<String> handler) {
        $this().on(TableEvents.CATEGORY_CLOSED + "." + getViewId(), handler);
    }

    @Override
    public void removeCategoryClosedHandler(EventFunc1<String> handler) {
        $this().off(TableEvents.CATEGORY_CLOSED, handler);
    }

    @Override
    public void removeCategoryClosedHandlers() {
        $this().off(TableEvents.CATEGORY_CLOSED);
    }
}
