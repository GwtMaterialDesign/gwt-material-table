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
package gwt.material.design.client.ui.table;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.TableCellElement;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.Renderer;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.SortDir;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.ComponentFactory;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.events.CategoryClosedEvent;
import gwt.material.design.client.data.events.CategoryClosedHandler;
import gwt.material.design.client.data.events.CategoryOpenedEvent;
import gwt.material.design.client.data.events.CategoryOpenedHandler;
import gwt.material.design.client.data.events.ColumnSortEvent;
import gwt.material.design.client.data.events.ColumnSortHandler;
import gwt.material.design.client.data.events.ComponentsRenderedEvent;
import gwt.material.design.client.data.events.ComponentsRenderedHandler;
import gwt.material.design.client.data.events.DestroyEvent;
import gwt.material.design.client.data.events.DestroyHandler;
import gwt.material.design.client.data.events.InsertColumnEvent;
import gwt.material.design.client.data.events.InsertColumnHandler;
import gwt.material.design.client.data.events.RemoveColumnEvent;
import gwt.material.design.client.data.events.RemoveColumnHandler;
import gwt.material.design.client.data.events.RenderedEvent;
import gwt.material.design.client.data.events.RenderedHandler;
import gwt.material.design.client.data.events.RowCollapsedEvent;
import gwt.material.design.client.data.events.RowCollapsedHandler;
import gwt.material.design.client.data.events.RowCollapsingEvent;
import gwt.material.design.client.data.events.RowCollapsingHandler;
import gwt.material.design.client.data.events.RowContextMenuEvent;
import gwt.material.design.client.data.events.RowContextMenuHandler;
import gwt.material.design.client.data.events.RowDoubleClickEvent;
import gwt.material.design.client.data.events.RowDoubleClickHandler;
import gwt.material.design.client.data.events.RowExpandedEvent;
import gwt.material.design.client.data.events.RowExpandedHandler;
import gwt.material.design.client.data.events.RowExpandingEvent;
import gwt.material.design.client.data.events.RowExpandingHandler;
import gwt.material.design.client.data.events.RowLongPressEvent;
import gwt.material.design.client.data.events.RowLongPressHandler;
import gwt.material.design.client.data.events.RowSelectEvent;
import gwt.material.design.client.data.events.RowSelectHandler;
import gwt.material.design.client.data.events.RowShortPressEvent;
import gwt.material.design.client.data.events.RowShortPressHandler;
import gwt.material.design.client.data.events.SelectAllEvent;
import gwt.material.design.client.data.events.SelectAllHandler;
import gwt.material.design.client.data.events.SetupEvent;
import gwt.material.design.client.data.events.SetupHandler;
import gwt.material.design.client.data.factory.RowComponentFactory;
import gwt.material.design.client.events.DefaultHandlerRegistry;
import gwt.material.design.client.events.HandlerRegistry;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.StandardDataView;
import gwt.material.design.client.ui.table.cell.Column;

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
public abstract class AbstractDataTable<T> extends MaterialWidget implements DataDisplay<T> {

    private static final Logger logger = Logger.getLogger(AbstractDataTable.class.getName());

    public static class DefaultTableScaffolding extends AbstractTableScaffolding {
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

        @Override
        protected XScrollPanel createXScrollPanel(Panel container) {
            return new XScrollPanel(container);
        }
    }

    protected DataView<T> view;
    protected TableScaffolding scaffolding;
    protected HandlerRegistry handlerRegistry;

    private boolean focused;
    private boolean refreshing;
    private boolean cellIsEditing;
    private boolean destroyOnUnload;

    private HandlerRegistration setupHandler;

    public AbstractDataTable() {
        this(new StandardDataView<>());
    }

    public AbstractDataTable(DataView<T> view) {
        this(view, new DefaultTableScaffolding());
    }

    public AbstractDataTable(TableScaffolding scaffolding) {
        this();
        this.scaffolding = scaffolding;
    }

    public AbstractDataTable(DataView<T> view, TableScaffolding scaffolding) {
        super(DOM.createElement("section"));
        this.view = view;
        this.scaffolding = scaffolding;

        view.setDisplay(this);
        setStyleName("table-container");

        handlerRegistry = new DefaultHandlerRegistry(this);

        // Build the table scaffolding
        scaffolding.build();
        scaffolding.apply(this);

        // Apply the DOM build.
        build();
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if(!view.isSetup()) {
            try {
                view.setup(scaffolding);
            } catch (Exception ex) {
                logger.log(Level.SEVERE,
                    "Could not setup AbstractDataTable due to previous errors.", ex);
            }
            // We should recalculate when we load again.
        } else if(view.isUseCategories()) {
            view.getSubheaderLib().recalculate(true);
        }
    }

    @Override
    protected void onUnload() {
        super.onUnload();

        clearHandlers();

        if(destroyOnUnload) {
            view.destroy();
        }
    }

    @Override
    public final DataView<T> getView() {
        return view;
    }

    @Override
    public final Range getVisibleRange() {
        return view.getVisibleRange();
    }

    @Override
    public final void setVisibleRange(int start, int length) {
        view.setVisibleRange(start, length);
    }

    @Override
    public final void setVisibleRange(Range range) {
        view.setVisibleRange(range);
    }

    @Override
    public final int getRowCount() {
        return view.getRowCount();
    }

    @Override
    public final void setRowData(int start, List<? extends T> values) {
        view.setRowData(start, values);
    }

    @Override
    public final void setCategoryFactory(ComponentFactory<? extends CategoryComponent, String> categoryFactory) {
        view.setCategoryFactory(categoryFactory);
    }

    @Override
    public final CategoryComponent getCategory(String categoryName) {
        return view.getCategory(categoryName);
    }

    @Override
    public final List<CategoryComponent> getCategories() {
        return view.getCategories();
    }

    @Override
    public final List<CategoryComponent> getOpenCategories() {
        return view.getOpenCategories();
    }

    @Override
    public final boolean isCategoryEmpty(CategoryComponent category) {
        return view.isCategoryEmpty(category);
    }

    @Override
    public final void addCategory(String category) {
        view.addCategory(category);
    }

    @Override
    public final void addCategory(CategoryComponent category) {
        view.addCategory(category);
    }

    @Override
    public final boolean hasCategory(String categoryName) {
        return view.hasCategory(categoryName);
    }

    @Override
    public final void disableCategory(String categoryName) {
        view.disableCategory(categoryName);
    }

    @Override
    public final void enableCategory(String categoryName) {
        view.enableCategory(categoryName);
    }

    @Override
    public final void openCategory(String categoryName) {
        view.openCategory(categoryName);
    }

    @Override
    public final void openCategory(CategoryComponent category) {
        view.openCategory(category);
    }

    @Override
    public final void closeCategory(String categoryName) {
        view.closeCategory(categoryName);
    }

    @Override
    public final void closeCategory(CategoryComponent category) {
        view.closeCategory(category);
    }

    @Override
    public final void clearRowsAndCategories(boolean clearData) {
        view.clearRowsAndCategories(clearData);
    }

    @Override
    public final void clearCategories() {
        view.clearCategories();
    }

    @Override
    public final void addColumn(Column<T, ?> column) {
        view.addColumn(column);
    }

    @Override
    public final void addColumn(Column<T, ?> column, String header) {
        view.addColumn(column, header);
    }

    @Override
    public final void insertColumn(int beforeIndex, Column<T, ?> col, String header) {
        view.insertColumn(beforeIndex, col, header);
    }

    @Override
    public final void removeColumn(int colIndex) {
        view.removeColumn(colIndex);
    }

    @Override
    public final void removeColumns() {
        view.removeColumns();
    }

    @Override
    public final List<Column<T, ?>> getColumns() {
        return view.getColumns();
    }

    @Override
    public final int getColumnOffset() {
        return view.getColumnOffset();
    }

    @Override
    public final void sort(int columnIndex) {
        view.sort(columnIndex);
    }

    @Override
    public final void sort(int columnIndex, SortDir dir) {
        view.sort(columnIndex, dir);
    }

    @Override
    public final void sort(Column<T, ?> column) {
        view.sort(column);
    }

    @Override
    public final void sort(Column<T, ?> column, SortDir dir) {
        view.sort(column, dir);
    }

    @Override
    public int getLeftFrozenColumns() {
        return view.getLeftFrozenColumns();
    }

    @Override
    public int getRightFrozenColumns() {
        return view.getRightFrozenColumns();
    }

    @Override
    public final void setDataSource(DataSource<T> dataSource) {
        view.setDataSource(dataSource);
    }

    @Override
    public final DataSource<T> getDataSource() {
        return view.getDataSource();
    }

    @Override
    public void loaded(int startIndex, List<T> data) {
        view.loaded(startIndex, data);
    }

    @Override
    public final void setRenderer(Renderer<T> renderer) {
        view.setRenderer(renderer);
    }

    @Override
    public final int getTotalRows() {
        return view.getTotalRows();
    }

    @Override
    public final void setTotalRows(int totalRows) {
        view.setTotalRows(totalRows);
    }

    @Override
    public final int getRowHeight() {
        return view.getRowHeight();
    }

    @Override
    public final void setRowHeight(int rowHeight) {
        view.setRowHeight(rowHeight);
    }

    @Override
    public final void updateRow(T model) {
        view.updateRow(model);
    }

    @Override
    public List<RowComponent<T>> getRows() {
        return view.getRows();
    }

    @Override
    public final RowComponent<T> getRow(T model) {
        return view.getRow(model);
    }

    @Override
    public final RowComponent<T> getRow(int index) {
        return view.getRow(index);
    }

    @Override
    public final RowComponent<T> getRowByModel(T model) {
        return view.getRowByModel(model);
    }

    @Override
    public final void clearRows(boolean clearData) {
        view.clearRows(clearData);
    }

    @Override
    public final void setRowFactory(RowComponentFactory<T> rowFactory) {
        view.setRowFactory(rowFactory);
    }

    @Override
    public RowComponentFactory<T> getRowFactory() {
        return view.getRowFactory();
    }

    @Override
    public final void setSelectionType(SelectionType selectionType) {
        view.setSelectionType(selectionType);
    }

    @Override
    public final SelectionType getSelectionType() {
        return view.getSelectionType();
    }

    @Override
    public final void selectAllRows(boolean select) {
        view.selectAllRows(select);
    }

    @Override
    public final void selectAllRows(boolean select, boolean fireEvent) {
        view.selectAllRows(select, fireEvent);
    }

    @Override
    public final void selectRow(Element row, boolean fireEvent) {
        view.selectRow(row, fireEvent);
    }

    @Override
    public final void deselectRow(Element row, boolean fireEvent) {
        view.deselectRow(row, fireEvent);
    }

    @Override
    public final boolean hasDeselectedRows(boolean visibleOnly) {
        return view.hasDeselectedRows(visibleOnly);
    }

    @Override
    public final boolean hasSelectedRows(boolean visibleOnly) {
        return view.hasSelectedRows(visibleOnly);
    }

    @Override
    public final List<T> getSelectedRowModels(boolean visibleOnly) {
        return view.getSelectedRowModels(visibleOnly);
    }

    @Override
    public final boolean isUseStickyHeader() {
        return view.isUseStickyHeader();
    }

    @Override
    public final void setUseStickyHeader(boolean stickyHeader) {
        view.setUseStickyHeader(stickyHeader);
    }

    @Override
    public final boolean isUseCategories() {
        return view.isUseCategories();
    }

    @Override
    public final void setUseCategories(boolean useCategories) {
        view.setUseCategories(useCategories);
    }

    @Override
    public final boolean isUseLoadOverlay() {
        return view.isUseLoadOverlay();
    }

    @Override
    public final void setUseLoadOverlay(boolean useLoadOverlay) {
        view.setUseLoadOverlay(useLoadOverlay);
    }

    @Override
    public final boolean isUseRowExpansion() {
        return view.isUseRowExpansion();
    }

    @Override
    public final void setUseRowExpansion(boolean useRowExpansion) {
        view.setUseRowExpansion(useRowExpansion);
    }

    @Override
    public final int getLongPressDuration() {
        return view.getLongPressDuration();
    }

    @Override
    public final void setLongPressDuration(int longPressDuration) {
        view.setLongPressDuration(longPressDuration);
    }

    @Override
    public final String getHeight() {
        return view.getHeight();
    }

    @Override
    public final void setHeight(String height) {
        view.setHeight(height);
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
        if (!Element.is(eventTarget)) {
            return;
        }
        Element target = Element.as(eventTarget);
        if (!getElement().isOrHasChild(Element.as(eventTarget))) {
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
                && CellBasedWidgetImpl.get().isFocusable(Element.as(target))) {
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
        return row.getSectionRowIndex() + getView().getVisibleRange().getStart();
    }

    /**
     * Fire an event to the Cell within the specified {@link TableCellElement}.
     */
    private <C> void fireEventToCell(Event event, String eventType, Element parentElem,
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
    @Override
    public TableScaffolding getScaffolding() {
        return scaffolding;
    }

    /**
     * @deprecated use {@link #addSetupHandler(SetupHandler)}
     */
    @Deprecated
    public void setLoadedCallback(LoadedCallback callback) {
        if(setupHandler != null) {
            setupHandler.removeHandler();
            setupHandler = null;
        }
        setupHandler = addSetupHandler(event -> {
            callback.onLoaded();
        });
    }

    /**
     * Event fired when the tables view calls {@link DataView#setup(TableScaffolding)}.
     * @return Handler registration to remove the event handler.
     */
    public HandlerRegistration addSetupHandler(SetupHandler handler) {
        return addHandler(handler, SetupEvent.TYPE);
    }

    /**
     * Event fired when the tables view calls {@link DataView#destroy()}.
     * @return Handler registration to remove the event handler.
     */
    public HandlerRegistration addDestroyHandler(DestroyHandler handler) {
        return addHandler(handler, DestroyEvent.TYPE);
    }

    /**
     * Event fired when the tables view calls {@link DataView#insertColumn(int, Column, String)}.
     * @return Handler registration to remove the event handler.
     */
    public HandlerRegistration addInsertColumnHandler(InsertColumnHandler<T> handler) {
        return addHandler(handler, InsertColumnEvent.TYPE);
    }

    /**
     * Event fired when the tables view calls {@link DataView#removeColumn(int)}.
     * @return Handler registration to remove the event handler.
     */
    public HandlerRegistration addRemoveColumnHandler(RemoveColumnHandler handler) {
        return addHandler(handler, RemoveColumnEvent.TYPE);
    }

    public boolean isDestroyOnUnload() {
        return destroyOnUnload;
    }

    public void setDestroyOnUnload(boolean destroyOnUnload) {
        this.destroyOnUnload = destroyOnUnload;
    }

    // Event Handlers

    @Override
    public HandlerRegistration registerHandler(HandlerRegistration registration) {
        return handlerRegistry.registerHandler(registration);
    }

    @Override
    public void clearHandlers() {
        handlerRegistry.clearHandlers();
    }

    @Override
    public HandlerRegistration addSelectAllHandler(SelectAllHandler<T> handler) {
        return addHandler(handler, SelectAllEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowSelectHandler(RowSelectHandler<T> handler) {
        return addHandler(handler, RowSelectEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowExpandingHandler(RowExpandingHandler<T> handler) {
        return addHandler(handler, RowExpandingEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowExpandedHandler(RowExpandedHandler<T> handler) {
        return addHandler(handler, RowExpandedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowCollapseHandler(RowCollapsingHandler<T> handler) {
        return addHandler(handler, RowCollapsingEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowCollapsedHandler(RowCollapsedHandler<T> handler) {
        return addHandler(handler, RowCollapsedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowContextMenuHandler(RowContextMenuHandler<T> handler) {
        return addHandler(handler, RowContextMenuEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowDoubleClickHandler(RowDoubleClickHandler<T> handler) {
        return addHandler(handler, RowDoubleClickEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowLongPressHandler(RowLongPressHandler<T> handler) {
        return addHandler(handler, RowLongPressEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRowShortPressHandler(RowShortPressHandler<T> handler) {
        return addHandler(handler, RowShortPressEvent.TYPE);
    }

    @Override
    public HandlerRegistration addColumnSortHandler(ColumnSortHandler<T> handler) {
        return addHandler(handler, ColumnSortEvent.TYPE);
    }

    @Override
    public HandlerRegistration addCategoryOpenedHandler(CategoryOpenedHandler handler) {
        return addHandler(handler, CategoryOpenedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addCategoryClosedHandler(CategoryClosedHandler handler) {
        return addHandler(handler, CategoryClosedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addComponentsRenderedHandler(ComponentsRenderedHandler handler) {
        return addHandler(handler, ComponentsRenderedEvent.TYPE);
    }

    @Override
    public HandlerRegistration addRenderedHandler(RenderedHandler handler) {
        return addHandler(handler, RenderedEvent.TYPE);
    }
}
