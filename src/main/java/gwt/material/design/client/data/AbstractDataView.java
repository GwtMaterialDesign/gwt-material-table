package gwt.material.design.client.data;

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

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.RowCountChangeEvent;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.CategoryComponent.OrphanCategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.factory.CategoryComponentFactory;
import gwt.material.design.client.data.factory.RowComponentFactory;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.jquery.JQueryMutate;
import gwt.material.design.client.js.Js;
import gwt.material.design.client.js.JsTableElement;
import gwt.material.design.client.js.JsTableSubHeaders;
import gwt.material.design.client.js.StickyTableOptions;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.table.*;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.events.RowExpand;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gwt.material.design.jquery.client.api.JQuery.$;
import static gwt.material.design.jquery.client.api.JQuery.window;

/**
 * Abstract DataView handles the creation, preparation and UI logic for
 * the table rows and subheaders (if enabled). All of the basic table
 * rendering is handled.
 *
 * @param <T>
 * @author Ben Dol
 */
public abstract class AbstractDataView<T> implements DataView<T> {

    private static final Logger logger = Logger.getLogger(AbstractDataView.class.getName());

    // Main
    protected final String id;
    protected DataView<T> display;
    protected DataSource<T> dataSource;
    protected Renderer<T> renderer;
    protected SortContext<T> sortContext;
    protected RowComponentFactory<T> rowFactory;
    protected CategoryComponentFactory categoryFactory;
    protected ProvidesKey<T> keyProvider;
    //protected List<ComponentFactory<?, T>> componentFactories;
    protected JsTableSubHeaders subheaderLib;
    protected Panel xScrollPanel;
    protected String height;

    // DOM
    protected Table table;
    protected MaterialWidget thead;
    protected MaterialWidget tbody;
    protected MaterialProgress progressWidget;
    protected TableRow headerRow;
    protected JQueryElement container;
    protected JsTableElement $table;
    protected JQueryElement maskElement;
    protected JQueryElement tableBody;
    protected JQueryElement innerScroll;
    protected JQueryElement topPanel;

    // Configurations
    protected Range range = new Range(0, 20);
    protected int totalRows = 20;
    protected int longPressDuration = 500;
    protected boolean rendering;
    protected boolean redraw;
    protected boolean redrawCategories;

    private int rowCount;
    private int lastSelected;
    private boolean setup;
    private boolean loadMask;
    private boolean shiftDown;
    private boolean isExact;
    private boolean useRowExpansion;
    private boolean useStickyHeader;
    private boolean useLoadOverlay;
    private boolean useCategories;
    private SelectionType selectionType = SelectionType.NONE;

    // Components
    protected final Components<RowComponent<T>> rows = new Components<>();
    protected final Components<CategoryComponent> categories = new Components<>();

    // Rendering
    protected final List<Column<T, ?>> columns = new ArrayList<>();
    protected final List<TableHeader> headers = new ArrayList<>();
    protected HandlerRegistration attachHandler;

    public AbstractDataView() {
        this("DataView");
    }

    public AbstractDataView(String id) {
        this(id, null);
    }

    public AbstractDataView(ProvidesKey<T> keyProvider) {
        this("DataView", keyProvider);
    }

    public AbstractDataView(String id, ProvidesKey<T> keyProvider) {
        this.id = id;
        this.keyProvider = keyProvider;
        this.categoryFactory = new CategoryComponentFactory();
        this.rowFactory = new RowComponentFactory<>();
        //this.componentFactories = new ArrayList<>();

        setRenderer(new BaseRenderer<>());
        onLoaded();
    }

    /**
     * Called after the data view is constructed.
     * Note that this is not when the data view is attached,
     * see {@link #setup(TableScaffolding)}.
     */
    protected void onLoaded() {
        // Do nothing by default
    }

    @Override
    public void render(Components<Component<?>> components) {
        // Clear the current row components
        // This does not clear the rows DOM elements
        this.rows.clearComponents();

        // Render the new components
        for(Component<?> component : components) {
            renderComponent(component);
        }

        redraw = false;
        prepareRows();

        // Reset category indexes and row counts
        if(isUseCategories()) {
            for (CategoryComponent category : categories) {
                category.setCurrentIndex(-1);
                category.setRowCount(-1);
            }
        }

        if(!components.isEmpty()) {
            // Remove the last attach handler
            if(attachHandler != null) {
                attachHandler.removeHandler();
            }
            // When the last component has been rendered we
            // will set the rendering flag to false.
            // This can be improved later.
            Scheduler.get().scheduleDeferred(() -> {
                Component<?> component = components.get(components.size() - 1);
                Widget componentWidget = component.getElement();
                if (componentWidget == null || componentWidget.isAttached()) {
                    rendering = false;
                } else {
                    attachHandler = componentWidget.addAttachHandler(event -> rendering = false);
                }
            });
        } else {
            rendering = false;
        }
    }

    /**
     * Compile list of {@link RowComponent}'s and invoke a render.
     * Controls the building of category components and custom components.
     * Which then {@link #render(Components)} is invoked to perform DOM render.
     *
     * Rows which are already rendered are reprocessed based on their equals method.
     * If the data is equal to the row in the same index it use the existing row.
     *
     * @param rows list of rows to be rendered against the existing rows.
     */
    protected void renderRows(Components<RowComponent<T>> rows) {
        rendering = true;
        Range visibleRange = getVisibleRange();

        // Check if we need to redraw categories
        if(redrawCategories) {
            redrawCategories = false;

            // When we perform a category redraw we have
            // to clear the row elements also.
            this.rows.clearElements();

            if (isUseCategories()) {
                List<CategoryComponent> openCategories = getOpenCategories();
                categories.clearElements();

                for (CategoryComponent category : categories) {
                    // Re-render the category component
                    renderComponent(category);

                    if (openCategories.contains(category) && category.isRendered()) {
                        subheaderLib.open(category.getElement().$this());
                    }
                }
            } else {
                categories.clearElements();
            }
        }

        // The linear component list.
        // This component list will be the rendering
        // blueprint for the current view, so the sequence
        // is compiled according to the component generation.
        Components<Component<?>> components = new Components<>(visibleRange.getLength());

        int index = 0;
        for(RowComponent<T> row : rows) {
            if (components.isFull()) {
                break; // Component stack is full, break
            }

            if (isUseCategories()) {
                CategoryComponent category = getCategory(row.getDataCategory());
                if(category == null && !hasCategory(row.getDataCategory())) {
                    category = buildCategoryComponent(row);
                }

                if(category != null) {
                    // Create or assign the category
                    if (!categories.contains(category)) {
                        categories.add(category);
                    }

                    if (!components.contains(category)) {
                        //components.add(category);

                        if (category.isRendered()) {
                            category.getElement().setVisible(true);
                        }
                    }
                }
            }

            // Do we have an existing row to use
            if (index < this.rows.size()) {
                RowComponent<T> existingRow = this.rows.get(index);
                if(existingRow != null) {
                    // Replace the rows element with the
                    // existing indexes element.
                    row.setElement(existingRow.getElement());
                    row.setRedraw(true);

                    // Rebuild the rows custom components
                    //existingRow.destroyChildren();
                    //buildCustomComponents(existingRow);
                }
            }
            row.setIndex(index++);
            components.add(row);
        }

        // Render the component stack
        render(components);
    }

    @SuppressWarnings("unchecked")
    protected void renderComponent(Component<?> component) {
        if(component != null) {
            TableRow row;
            int index = -1;

            if(component instanceof RowComponent) {
                RowComponent<T> rowComponent = (RowComponent<T>)component;

                // Check if the row has a category
                // Categories have been rendered before the rows
                CategoryComponent category = null;
                String rowCategory = rowComponent.getDataCategory();
                if(isUseCategories()){
                    category = getCategory(rowCategory);

                    // Ensure the category exists and is rendered
                    if(category != null && !category.isRendered()) {
                        renderComponent(category);
                    }
                }
                T data = rowComponent.getData();

                // Draw the table row
                row = renderer.drawRow(this, rowComponent, getValueKey(data), columns, redraw);

                if(row != null) {
                    if(category != null){
                        if(categories.size() > 1) {
                            int categoryIndex = category.getCurrentIndex();
                            if(categoryIndex == -1) {
                                categoryIndex = tbody.getWidgetIndex(category.getElement());
                                category.setCurrentIndex(categoryIndex);
                            }

                            int categoryCount = category.getRowCount() + 1;
                            category.setRowCount(categoryCount);

                            // Calculate the rows index
                            index = categoryIndex + categoryCount;
                        }

                        // Check the display of the row
                        TableSubHeader element = category.getElement();
                        if (element != null && element.isOpen()) {
                            row.getElement().getStyle().clearDisplay();
                        }
                    } else {
                        // Not using categories
                        row.getElement().getStyle().clearDisplay();
                    }

                    rows.add(rowComponent);
                }
            } else if(component instanceof CategoryComponent) {
                row = bindCategoryEvents(renderer.drawCategory((CategoryComponent)component));
            } else {
                row = renderer.drawCustom(component);
            }

            if(row != null) {
                if(row.getParent() == null) {
                    if (index < 0) {
                        tbody.add(row);
                    } else {
                        tbody.insert(row, index + 1);
                    }
                } else {
                    // TODO: calculate row element repositioning.
                    // This will only apply if its index is different
                    // to the new index generated based on the category.
                }
            } else {
                logger.warning("Attempted to add a null TableRow to tbody, the row was ignored.");
            }

            // Render the components children
            /*for(Component<?> child : component.getChildren()) {
                renderComponent(child);
            }*/
        }
    }

    @Override
    public void refreshView() {
        // Recheck the row height to ensure
        // the calculated row height is accurate.
        renderer.getCalculatedRowHeight();

        if(redraw) {
            renderRows(rows);
        }
    }

    @Override
    public void setRenderer(Renderer<T> renderer) {
        this.renderer = renderer;
    }

    @Override
    public void setDataSource(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public DataSource<T> getDataSource() {
        return dataSource;
    }

    @Override
    public JsTableSubHeaders getSubheaderLib() {
        return subheaderLib;
    }

    @Override
    public void setup(TableScaffolding scaffolding) throws Exception {
        try {
            container = $(getContainer());
            table = scaffolding.getTable();
            tableBody = $(scaffolding.getTableBody());
            innerScroll = $(scaffolding.getTableBody()).children("*").first();
            topPanel = $(scaffolding.getTopPanel());
            xScrollPanel = scaffolding.getXScrollPanel();
            tbody = table.getBody();
            thead = table.getHead();
            $table = table.getJsElement();

            headerRow = new TableRow();
            thead.add(headerRow);

            // Create progress widget
            progressWidget = new MaterialProgress();
            progressWidget.setTop(0);
            progressWidget.setGwtDisplay(Display.NONE);
            TableRow progressRow = new TableRow();
            progressRow.addStyleName(TableCssName.STICKYEXCLUDE);
            progressRow.setHeight("3px");
            TableData progressTd = new TableData();
            progressTd.getElement().setAttribute("colspan", "999");
            progressTd.setPadding(0);
            progressTd.setHeight("0px");
            progressTd.add(progressWidget);
            progressRow.add(progressTd);
            thead.add(progressRow);

            if(useRowExpansion) {
                // Add the expand header
                TableHeader expandHeader = new TableHeader();
                expandHeader.setStyleName(TableCssName.COLEX);
                addHeader(0, expandHeader, null);
            }

            if(!selectionType.equals(SelectionType.NONE)) {
                setupHeaderSelectionBox();

                if(selectionType.equals(SelectionType.MULTIPLE)) {
                    setupShiftDetection();
                }
            }

            // Setup the sticky header bar
            if (useStickyHeader) {
                setupStickyHeader();
            }

            // Setup the subheaders for categories
            setupSubHeaders();

            // Setup the resize event handlers
            tableBody.on("resize." + id, e -> {
                refreshView();
                return true;
            });

            // We will check the window resize just in case
            // it has updated the view size of the data view.
            $(window()).on("resize." + id, e -> {
                refreshView();
                return true;
            });

            // XScroll Setup
            $table.on("resize." + id, e -> {
                xScrollPanel.setWidth(innerScroll.asElement().getScrollWidth() + "px");
                return true;
            });

            // Setup inner scroll x scroll binding
            JQueryElement $xScrollPanel = $(xScrollPanel);
            $xScrollPanel.on("scroll." + id, e -> {
                innerScroll.prop("scrollLeft", $xScrollPanel.scrollLeft());
                return true;
            });

            innerScroll.on("scroll." + id, e -> {
                $xScrollPanel.prop("scrollLeft", innerScroll.scrollLeft());
                return true;
            });

            JQueryMutate.$(innerScroll).mutate("scrollWidth", (el, info) -> {
                xScrollPanel.setWidth(el.getScrollWidth() + "px");
            });

            Scheduler.get().scheduleDeferred(() -> {
                innerScroll.css("margin-left", "60px");
            });

            setup = true;

            if(height != null) {
                setHeight(height);
            }
        }
        catch (Exception ex) {
            logger.log(Level.SEVERE, "Problem setting up the DataView.", ex);
            throw ex;
        }
    }

    @Override
    public void destroy() {
        rows.clearComponents();
        categories.clearComponents();

        columns.clear();
        headers.clear();
        headerRow.clear();

        rendering = false;

        container.off("." + id);
        tableBody.off("." + id);
        $(window()).off("." + id);
    }

    /**
     * Prepare all row specific functionality.
     */
    protected void prepareRows() {
        JQueryElement rows = $table.find("tr.data-row");
        rows.off("." + id);

        if(!selectionType.equals(SelectionType.NONE)) {
            // Select row click bind
            // This will also update the check status of check all input.
            rows.on("tap." + id + " click." + id, (e, o) -> {
                if (!selectionType.equals(SelectionType.NONE)) {
                    Element row = $(e.getCurrentTarget()).asElement();
                    int rowIndex = getRowIndexByElement(row);
                    if (selectionType.equals(SelectionType.MULTIPLE) && shiftDown) {
                        if (lastSelected < rowIndex) {
                            // Increment
                            for (int i = lastSelected; i < rowIndex; i++) {
                                if (i < getVisibleItemCount()) {
                                    RowComponent<T> rowComponent = this.rows.get(i);
                                    if (rowComponent != null && rowComponent.isRendered()) {
                                        selectRow(rowComponent.getElement().getElement(), true);
                                    }
                                }
                            }
                        } else {
                            // Decrement
                            for (int i = lastSelected - 1; i >= rowIndex - 1; i--) {
                                if (i >= 0) {
                                    RowComponent<T> rowComponent = this.rows.get(i);
                                    if (rowComponent != null && rowComponent.isRendered()) {
                                        selectRow(rowComponent.getElement().getElement(), true);
                                    }
                                }
                            }
                        }
                    } else {
                        toggleRowSelect(row);
                    }
                }
                return false;
            });
        }

        rows.on("contextmenu." + id, (e, o) -> {
            Element row = $(e.getCurrentTarget()).asElement();

            // Fire row select event
            container.trigger(TableEvents.ROW_CONTEXTMENU, new Object[] {
                e, getModelByRowElement(row), row
            });
            return false;
        });

        rows.on("dblclick." + id, (e, o) -> {
            Element row = $(e.getCurrentTarget()).asElement();

            // Fire row select event
            container.trigger(TableEvents.ROW_DOUBLECLICK, new Object[] {
                e, getModelByRowElement(row), row
            });
            return false;
        });

        JQueryExtension.$(rows).longpress(e -> {
            Element row = $(e.getCurrentTarget()).asElement();

            // Fire row select event
            container.trigger(TableEvents.ROW_LONGPRESS, new Object[] {
                e, getModelByRowElement(row), row
            });
            return true;
        }, e -> {
            Element row = $(e.getCurrentTarget()).asElement();

            // Fire row select event
            container.trigger(TableEvents.ROW_SHORTPRESS, new Object[] {
                e, getModelByRowElement(row), row
            });
            return true;
        }, longPressDuration);

        JQueryElement expands = $table.find("i#expand");
        expands.off("." + id);
        if(useRowExpansion) {
            // Expand current row extra information
            expands.on("tap." + id + " click." + id, e -> {
                final boolean[] recalculated = {false};

                JQueryElement tr = $(e.getCurrentTarget()).parent().parent();
                if (!tr.hasClass("disabled") && !tr.is("[disabled]")) {
                    JQueryElement[] expansion = new JQueryElement[]{tr.next().find("td.expansion div")};

                    if (expansion[0].length() < 1) {
                        expansion[0] = $("<tr class='expansion'>" +
                                "<td class='expansion' colspan='100%'>" +
                                "<div>" +
                                "<section class='overlay'>" +
                                "<div class='progress' style='height:4px;top:-1px;'>" +
                                "<div class='indeterminate'></div>" +
                                "</div>" +
                                "</section>" +
                                "<div class='content'><br/><br/><br/></div>" +
                                "</div>" +
                                "</td></tr>");

                        expansion[0].insertAfter(tr);
                        expansion[0] = expansion[0].find("td.expansion div");
                    }

                    final boolean expanding = !expansion[0].hasClass("expanded");
                    final JQueryElement expandRow = tr.next();
                    final T model = getModelByRowElement(tr.asElement());

                    expansion[0].one("transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd",
                        (e1, param1) -> {
                            if (!recalculated[0]) {
                                // Recalculate subheaders
                                subheaderLib.recalculate(true);
                                recalculated[0] = true;

                                // Apply overlay
                                JQueryElement overlay = expandRow.find("section.overlay");
                                overlay.height(expandRow.outerHeight(false));

                                // Fire table expand event
                                container.trigger(TableEvents.ROW_EXPANDED, new RowExpand<>(model, expandRow, expanding));
                            }
                            return true;
                        });

                    // Fire table expand event
                    container.trigger(TableEvents.ROW_EXPAND, new RowExpand<>(model, expandRow, expanding));

                    Scheduler.get().scheduleDeferred(() -> {
                        expansion[0].toggleClass("expanded");
                    });
                }

                e.stopPropagation();
                return true;
            });
        }

        subheaderLib.detect();
        subheaderLib.recalculate(true);
    }

    protected void setupStickyHeader() {
        if($table != null && display != null) {
            $table.stickyTableHeaders(StickyTableOptions.create(
                $(".table-body", getContainer())));
        }
    }

    protected void setupSubHeaders() {
        if($table != null && display != null) {
            subheaderLib = JsTableSubHeaders.newInstance(
                $(".table-body", getContainer()), "tr.subheader");

            final JQueryElement header = $table.find("thead");
            $(subheaderLib).on("before-recalculate", e -> {
                boolean updateMargin = header.is(":visible") && isUseStickyHeader();
                subheaderLib.setMarginTop(updateMargin ? header.outerHeight() : 0);
                return true;
            });

            // Load the subheaders after binding.
            subheaderLib.load();
        }
    }

    protected boolean isWithinView(int start, int length) {
        return isWithinView(start, length, true);
    }

    protected boolean isWithinView(int start, int length, boolean canOverflow) {
        int end = start + length;
        int rangeStart = range.getStart();
        int rangeEnd = rangeStart + range.getLength();
        return ((canOverflow ? (end > rangeStart && start < rangeEnd) : (start >= rangeStart && end <= rangeEnd)));
    }

    /**
     * Ensure that the cached data is consistent with the data size.
     */
    private void updateCachedData() {
        int rangeStart = range.getStart();
        int expectedLastIndex = Math.max(0, Math.min(range.getLength(), getRowCount() - rangeStart));
        int lastIndex = getVisibleItemCount() - 1;
        while (lastIndex >= expectedLastIndex) {
            rows.remove(lastIndex);
            lastIndex--;
        }
    }

    @Override
    public Range getVisibleRange() {
        return range;
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(Handler handler) {
        return table.addHandler(handler, RangeChangeEvent.getType());
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
        return table.addHandler(handler, RowCountChangeEvent.getType());
    }

    @Override
    public boolean isRowCountExact() {
        return isExact;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public void setRowCount(int count) {
        rowCount = count;
    }

    @Override
    public void setRowCount(int count, boolean isExact) {
        if (count == getRowCount() && isExact == isRowCountExact()) {
            return;
        }
        setRowCount(count);
        this.isExact = isExact;

        // Update the cached data.
        updateCachedData();

        // Fire the row count change event
        container.trigger(TableEvents.ROW_COUNT_CHANGE, new Object[]{count, isExact});
    }

    @Override
    public void setVisibleRange(int start, int length) {
        setVisibleRange(new Range(start, length));
    }

    @Override
    public void setVisibleRange(Range range) {
        setVisibleRange(range, false, true);
    }

    protected void setVisibleRange(Range range, boolean clearData, boolean forceRangeChangeEvent) {
        final int start = range.getStart();
        final int length = range.getLength();
        if (start < 0) {
            throw new IllegalArgumentException("Range start cannot be less than 0");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Range length cannot be less than 0");
        }

        // Update the page start.
        final int pageStart = this.range.getStart();
        final int pageSize = this.range.getLength();
        final boolean pageStartChanged = (pageStart != start);
        if (pageStartChanged) {
            // Trim the data if we aren't clearing it.
            if (!clearData) {
                if (start > pageStart) {
                    int increase = start - pageStart;
                    if (getVisibleItemCount() > increase) {
                        // Remove the data we no longer need.
                        for (int i = 0; i < increase; i++) {
                            rows.remove(rows.size()-1);
                        }
                    }
                } else {
                    int decrease = pageStart - start;
                    if ((getVisibleItemCount() > 0) && (decrease < pageSize)) {
                        // Add null data
                        for (int i = 0; i < decrease; i++) {
                            rows.add(null);
                        }
                    }
                }
            }

            // Update the range start
            this.range = new Range(start, this.range.getLength());
        }

        // Update the page size
        final boolean pageSizeChanged = (pageSize != length);
        if (pageSizeChanged) {
            this.range = new Range(this.range.getStart(), length);
        }

        // Clear the rows
        if (clearData) {
            rows.clear();
        }

        // Trim the row values if needed
        updateCachedData();

        // Update the pager and data source if the range changed
        if (pageStartChanged || pageSizeChanged || forceRangeChangeEvent) {
            RangeChangeEvent.fire(display, getVisibleRange());
        }
    }

    @Override
    public boolean isHeaderVisible(int colIndex) {
        return colIndex < headers.size() && (headers.get(colIndex).$this().is(":visible") || headers.get(colIndex).isVisible());
    }

    @Override
    public void addColumn(Column<T, ?> column) {
        addColumn(column, "");
    }

    @Override
    public void addColumn(Column<T, ?> column, String header) {
        insertColumn(columns.size(), column, header);
    }

    @Override
    public void insertColumn(int beforeIndex, Column<T, ?> column, String header) {
        // Allow insert at the end.
        if (beforeIndex != getColumnCount()) {
            checkColumnBounds(beforeIndex);
        }

        String name = column.getName();
        if(name == null || name.isEmpty()) {
            // Set the columns name
            column.setName(header);
        }

        int index = beforeIndex + getColumnOffset();

        TableHeader th = renderer.drawColumnHeader(column, header, index);
        if(th != null) {
            if (column.isSortable()) {
                th.$this().on("click", e -> {
                    sort(th, column, index);
                    return true;
                });
                th.addStyleName(TableCssName.SORTABLE);
            }

            addHeader(index, th, column);
        }

        if(columns.size() < beforeIndex) {
            columns.add(column);
        } else {
            columns.add(beforeIndex, column);
        }

        for (RowComponent<T> row : rows) {
            Context context = new Context(row.getIndex(), index, getValueKey(row.getData()));
            renderer.drawColumn(row.getElement(), context, row.getData(), column, index, true);
        }

        refreshStickyHeaders();
    }

    protected void updateSortContext(TableHeader th, Column<T, ?> column) {
        if(sortContext == null) {
            sortContext = new SortContext<>(column, th);
        } else {
            Column<T, ?> sortColumn = sortContext.getSortColumn();
            if(sortColumn != column) {
                sortContext.setSortColumn(column);
                sortContext.setTableHeader(th);
            } else {
                sortContext.reverse();
            }
        }
    }

    protected void sort(TableHeader th, Column<T, ?> column, int index) {
        SortContext<T> oldSortContext = this.sortContext;
        updateSortContext(th, column);

        Components<RowComponent<T>> rows = new Components<>(this.rows, RowComponent::new);
        if(doSort(sortContext, rows)) {
            th.addStyleName(TableCssName.SELECTED);

            // Draw and apply the sort icon.
            renderer.drawSortIcon(th, sortContext);

            // Render the new sort order.
            renderRows(rows);

            container.trigger(TableEvents.SORT_COLUMN, new Object[]{sortContext, index});
        } else {
            // revert the sort context
            sortContext = oldSortContext;
        }
    }

    /**
     * Perform a sort on the a set of {@link RowComponent}'s.
     * Sorting will check for each components category and sort per category if found.
     * @return true if the data was sorted, false if no sorting was performed.
     */
    protected boolean doSort(SortContext<T> sortContext, Components<RowComponent<T>> rows) {

        if (dataSource != null && dataSource.useRemoteSort()) {
            // The sorting should be handled by an external
            // data source rather than re-ordered by the
            // client comparator.
            return true;
        }

        Comparator<? super RowComponent<T>> comparator = sortContext != null
                ? sortContext.getSortColumn().getSortComparator() : null;
        if (isUseCategories() && !categories.isEmpty()) {
            // Split row data into categories
            Map<String, List<RowComponent<T>>> splitMap = new HashMap<>();
            List<RowComponent<T>> orphanRows = new ArrayList<>();

            for (RowComponent<T> row : rows) {
                if(row != null) {
                    String category = row.getDataCategory();
                    if(category != null) {
                        List<RowComponent<T>> data = splitMap.computeIfAbsent(category, k -> new ArrayList<>());
                        data.add(row);
                    } else {
                        orphanRows.add(row);
                    }
                }
            }

            if(!orphanRows.isEmpty()) {
                splitMap.put("<@orphans@>", orphanRows);
            }

            rows.clearComponents();
            for (Map.Entry<String, List<RowComponent<T>>> entry : splitMap.entrySet()) {
                List<RowComponent<T>> list = entry.getValue();
                if (comparator != null) {
                    list.sort(new DataSort<>(comparator, sortContext.getSortDir()));
                }
                rows.addAll(list);
            }
        } else {
            if (comparator != null) {
                rows.sort(new DataSort<>(comparator, sortContext.getSortDir()));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removeColumn(int colIndex) {
        int index = colIndex + getColumnOffset();
        headerRow.remove(index);

        for(RowComponent<T> row : getRows()) {
            row.getElement().remove(index);
        }
        columns.remove(colIndex);

        reindexColumns();
        refreshStickyHeaders();
    }

    @Override
    public List<Column<T, ?>> getColumns() {
        return columns;
    }

    @Override
    public int getColumnOffset() {
        return selectionType.equals(SelectionType.NONE) ? 0 : 1;
    }

    /**
     * Check that the specified column is within bounds.
     *
     * @param col the column index
     * @throws IndexOutOfBoundsException if the column is out of bounds
     */
    private void checkColumnBounds(int col) {
        if (col < 0 || col >= getColumnCount()) {
            throw new IndexOutOfBoundsException("Column index is out of bounds: " + col);
        }
    }

    /**
     * Get the number of columns in the table.
     *
     * @return the column count
     */
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public SelectionType getSelectionType() {
        return selectionType;
    }

    @Override
    public void setSelectionType(SelectionType selectionType) {
        boolean hadSelection = !this.selectionType.equals(SelectionType.NONE);
        this.selectionType = selectionType;

        // Add the selection header
        if(isSetup()) {
            if (!selectionType.equals(SelectionType.NONE) && !hadSelection) {
                setupHeaderSelectionBox();

                if(selectionType.equals(SelectionType.MULTIPLE)) {
                    setupShiftDetection();
                }

                // Rebuild the columns
                for (RowComponent<T> row : getRows()) {
                    row.getElement().insert(renderer.drawSelectionCell(), 0);
                }
                reindexColumns();
            } else if (selectionType.equals(SelectionType.NONE) && hadSelection) {
                removeHeader(0);
                $("td#col0", getContainer()).remove();
                reindexColumns();
            }
        }
    }

    protected void setupHeaderSelectionBox() {
        // Setup select all checkbox
        TableHeader th = new TableHeader();
        th.setId("col0");
        th.addStyleName("frozen-col");
        th.addStyleName(TableCssName.SELECTION);
        if(selectionType.equals(SelectionType.MULTIPLE)) {
            new MaterialCheckBox(th.getElement());

            // Select all row click bind
            // This will also update the check status of check all input.
            JQueryElement selectAll = $(th).find("label");
            selectAll.off("." + id);
            selectAll.on("tap." + id + " click." + id, (e) -> {
                JQueryElement input = $("input", th);

                boolean marked = Js.isTrue(input.prop("checked")) ||
                    Js.isTrue(input.prop("indeterminate"));

                selectAllRows(!marked || hasUnselectedRows(true));
                return false;
            });
        }
        addHeader(0, th, null);
    }

    protected void setupShiftDetection() {
        tableBody.attr("tabindex", "0");

        tableBody.off("keydown");
        tableBody.keydown(e -> {
            shiftDown = e.isShiftKey();
            return true;
        });

        tableBody.off("keyup");
        tableBody.keyup(e -> {
            shiftDown = e.isShiftKey();
            return true;
        });
    }

    protected void reindexColumns() {
        int colMod = getColumnOffset();

        for(RowComponent<T> row : getRows()) {
            TableRow tableRow = row.getElement();
            for(int i = colMod; i < tableRow.getWidgetCount(); i++) {
                TableData td = tableRow.getColumn(i);
                if(!td.getStyleName().contains("colex")) {
                    td.setId("col" + i);
                }
            }
        }

        for(int i = colMod; i < headerRow.getWidgetCount(); i++) {
            TableData td = headerRow.getColumn(i);
            if(!td.getStyleName().contains("colex")) {
                td.setId("col" + i);
            }
        }
    }

    @Override
    public boolean isSetup() {
        return setup;
    }

    @Override
    public void setUseStickyHeader(boolean stickyHeader) {
        if (this.useStickyHeader && !stickyHeader) {
            // Destroy existing sticky header function
            $table.stickyTableHeaders("destroy");
        } else if (stickyHeader) {
            // Initialize sticky header
            setupStickyHeader();
        }
        this.useStickyHeader = stickyHeader;
    }

    @Override
    public boolean isUseStickyHeader() {
        return useStickyHeader;
    }

    /**
     * TODO: This method can be optimized.
     */
    private void refreshStickyHeaders() {
        if($table != null) {
            // Destroy existing sticky header function
            $table.stickyTableHeaders("destroy");

            if(isUseStickyHeader()) {
                // Initialize sticky header
                setupStickyHeader();
            }
        }
    }

    @Override
    public void selectAllRows(boolean select) {
        selectAllRows(select, true);
    }

    @Override
    public void selectAllRows(boolean select, boolean fireEvent) {
        List<Element> rows = new ArrayList<>();

        // Select all rows
        $table.find("tr.data-row").each((i, e) -> {
            JQueryElement row = $(e);

            if(row.is(":visible") && !row.hasClass("disabled") && !row.is("[disabled]")) {
                JQueryElement input = $("td#col0 input", row);
                input.prop("checked", select);

                boolean isSelected = row.hasClass("selected");
                row.removeClass("selected");
                if(select) {
                    row.addClass("selected");

                    // Only add to row selection if
                    // not selected previously.
                    if(!isSelected) {
                        rows.add(row.asElement());
                    }
                } else if(isSelected) {
                    rows.add(row.asElement());
                }
            }
        });

        // Update check all input
        updateCheckAllInputState();

        if(fireEvent) {
            // Fire select all event
            container.trigger(TableEvents.SELECT_ALL, new Object[]{
                    getModelsByRowElements(rows), rows, select
            });
        }
    }

    @Override
    public void toggleRowSelect(Element row) {
        toggleRowSelect(row, true);
    }

    @Override
    public void toggleRowSelect(Element row, boolean fireEvent) {
        JQueryElement $row = $(row);
        if(!$row.hasClass("disabled") && !$row.is("[disabled]")) {
            boolean selected = Js.isTrue($row.hasClass("selected"));
            if(selected) {
                $("td#col0 input", row).prop("checked", false);
                $row.removeClass("selected");
            } else {
                // unselect all rows when using single selection
                if(selectionType.equals(SelectionType.SINGLE)) {
                    selectAllRows(false, true);
                }

                $("td#col0 input", row).prop("checked", true);
                $row.addClass("selected");
                lastSelected = getRowIndexByElement(row);
            }

            // Update check all input
            updateCheckAllInputState();

            if(fireEvent) {
                // Fire row select event
                container.trigger(TableEvents.ROW_SELECT, new Object[] {
                        getModelByRowElement(row), row, !selected
                });
            }
        }
    }

    @Override
    public void selectRow(Element row, boolean fireEvent) {
        JQueryElement $row = $(row);
        if(!$row.hasClass("disabled") && !$row.is("[disabled]")) {
            if(!Js.isTrue($row.hasClass("selected"))) {
                // unselect all rows when using single selection
                if(selectionType.equals(SelectionType.SINGLE)) {
                    selectAllRows(false, true);
                }

                $("td#col0 input", row).prop("checked", true);
                $row.addClass("selected");
                lastSelected = getRowIndexByElement(row);
            }

            // Update check all input
            updateCheckAllInputState();

            if(fireEvent) {
                // Fire row select event
                container.trigger(TableEvents.ROW_SELECT, new Object[] {
                        getModelByRowElement(row), row, true
                });
            }
        }
    }

    @Override
    public void unselectRow(Element row, boolean fireEvent) {
        JQueryElement $row = $(row);
        if(!$row.hasClass("disabled") && !$row.is("[disabled]")) {
            if(Js.isTrue($row.hasClass("selected"))) {
                $("td#col0 input", row).prop("checked", false);
                $row.removeClass("selected");
            }

            // Update check all input
            updateCheckAllInputState();

            if(fireEvent) {
                // Fire row select event
                container.trigger(TableEvents.ROW_SELECT, new Object[] {
                        getModelByRowElement(row), row, false
                });
            }
        }
    }

    @Override
    public boolean hasUnselectedRows(boolean visibleOnly) {
        return $table.find("tr:not([disabled]):not(.disabled) td#col0 input:not(:checked)"
                + (visibleOnly ? ":visible" : "")).length() > 0;
    }

    @Override
    public boolean hasSelectedRows(boolean visibleOnly) {
        return $table.find("tr:not([disabled]):not(.disabled) td#col0 input:checked"
                + (visibleOnly ? ":visible" : "")).length() > 0;
    }

    @Override
    public List<T> getSelectedRowModels(boolean visibleOnly) {
        final List<T> models = new ArrayList<>();
        $table.find("tr:not([disabled]):not(.disabled) td#col0 input:checked"
                + (visibleOnly ? ":visible" : "")).each((i, e) -> {
            T model = getModelByRowElement($(e).parent().parent().asElement());
            if(model != null) {
                models.add(model);
            }
        });
        return models;
    }

    @Override
    public void setRowData(int start, List<? extends T> values) {
        int length = values.size();
        int end = start + length;

        // Make sure we have a valid range
        if(range == null) {
            setVisibleRange(0, length);
        }

        // Current range start and end
        int rangeStart = range.getStart();
        int rangeEnd = rangeStart + range.getLength();

        // Calculated boundary scope
        int boundedStart = Math.max(start, rangeStart);
        int boundedEnd = Math.min(end, rangeEnd);

        if (start != rangeStart && boundedStart >= boundedEnd) {
            // The data is out of range for the current range.
            // Intentionally allow empty lists that start on the range start.
            return;
        }

        // Merge the existing data with the new data
        Components<RowComponent<T>> rows = new Components<>(this.rows);
        for (int i = boundedStart; i < boundedEnd; i++) {
            RowComponent<T> newRow = buildRowComponent(values.get(i - boundedStart));
            if(i < rows.size()) {
                // Within the existing data set
                rows.set(i, newRow);
            } else {
                // Expanding the data set
                rows.add(newRow);
            }
        }

        // Ensure sort order is applied for new rows
        doSort(sortContext, rows);

        // Render the new rows
        renderRows(rows);
    }

    protected RowComponent<T> buildRowComponent(T data) {
        if(data != null) {
            assert rowFactory != null : "The dataview's row factory cannot be null";
            return /*buildCustomComponents(*/rowFactory.generate(data)/*)*/;
        }
        return null;
    }

    protected CategoryComponent buildCategoryComponent(RowComponent<T> row) {
        return row != null ? buildCategoryComponent(row.getDataCategory()) : null;
    }

    protected CategoryComponent buildCategoryComponent(String category) {
        if(category != null) {
            // Generate the category if not exists
            if (categoryFactory != null) {
                if (!hasCategory(category)) {
                    return categoryFactory.generate(category);
                } else {
                    return getCategory(category);
                }
            }
        }
        return null;
    }

    /*protected RowComponent<T> buildCustomComponents(RowComponent<T> row) {
        if(row != null) {
            // custom components
            for (ComponentFactory<?, T> factory : componentFactories) {
                T data = row.getData();
                if(data != null) {
                    Component<?> component = factory.generate(data);
                    if (component != null) {
                        row.add(component);
                    } else {
                        logger.fine("DataView component factory: " + factory.toString() + " returned a null component.");
                    }
                }
            }
        }
        return row;
    }*/

    /**
     * Get the key for the specified value. If a keyProvider is not specified or the value is null,
     * the value is returned. If the key provider is specified, it is used to get the key from
     * the value.
     *
     * @param value the value
     * @return the key
     */
    public Object getValueKey(T value) {
        ProvidesKey<T> keyProvider = getKeyProvider();
        return (keyProvider == null || value == null) ? value : keyProvider.getKey(value);
    }

    @Override
    public ProvidesKey<T> getKeyProvider() {
        return keyProvider;
    }

    @Override
    public int getVisibleItemCount() {
        return rows.size();
    }

    @Override
    public int getRowHeight() {
        return renderer.getExpectedRowHeight();
    }

    @Override
    public void setRowHeight(int rowHeight) {
        renderer.setExpectedRowHeight(rowHeight);
    }

    protected int getCalculatedRowHeight() {
        if(!rows.isEmpty()) {
            renderer.calculateRowHeight(rows.get(0));
        }
        return renderer.getCalculatedRowHeight();
    }

    @Override
    public void addCategory(String category) {
        if(category != null) {
            addCategory(buildCategoryComponent(category));
        }
    }

    @Override
    public void addCategory(final CategoryComponent category) {
        if(category != null && !hasCategory(category.getCategory())) {
            categories.add(category);

            if(isUseCategories()) {
                // Render the category component
                renderComponent(category);

                if(subheaderLib != null) {
                    subheaderLib.detect();
                    subheaderLib.recalculate(true);
                }
            }
        }
    }

    @Override
    public boolean hasCategory(String categoryName) {
        if(categoryName != null) {
            for (CategoryComponent category : categories) {
                if (category.getCategory().equals(categoryName)) {
                    return true;
                }
            }
        }
        return getOrphansCategory() != null;
    }

    @Override
    public void disableCategory(String categoryName) {
        CategoryComponent category = getCategory(categoryName);
        if(category != null && category.isRendered()) {
            subheaderLib.close(category.getElement().$this());
            category.getElement().setEnabled(false);
        }
    }

    @Override
    public List<CategoryComponent> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    @Override
    public List<CategoryComponent> getOpenCategories() {
        List<CategoryComponent> openCategories = new ArrayList<>();
        if(isUseCategories()) {
            for (CategoryComponent category : categories) {
                TableSubHeader element = category.getElement();
                if (element != null && element.isOpen()) {
                    openCategories.add(category);
                }
            }
        }
        return openCategories;
    }

    @Override
    public boolean isCategoryEmpty(CategoryComponent category) {
        for(RowComponent<T> row : rows) {
            if(row.getDataCategory().equals(category.getCategory())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public SortContext<T> getSortContext() {
        return sortContext;
    }

    @Override
    public boolean isRedraw() {
        return redraw;
    }

    @Override
    public void setRedraw(boolean redraw) {
        setRedrawCategories(redraw);
        this.redraw = redraw;
    }

    @Override
    public RowComponent<T> getRow(T model) {
        for(RowComponent<T> row : rows) {
            if(row.getData().equals(model)) {
                return row;
            }
        }
        return null;
    }

    @Override
    public RowComponent<T> getRow(int index) {
        for(RowComponent<T> row : rows) {
            if(row.isRendered() && row.getIndex() == index) {
                return row;
            }
        }
        return null;
    }

    protected int getRowIndexByElement(Element rowElement) {
        int index = 0;
        for(RowComponent<T> row : rows) {
            index++;
            if(row.isRendered() && row.getElement().getElement().equals(rowElement)) {
                break;
            }
        }
        return index;
    }

    protected Element getRowElementByModel(T model) {
        for(RowComponent<T> row : rows) {
            if(row.getData().equals(model)) {
                return row.getElement().getElement();
            }
        }
        return null;
    }

    protected T getModelByRowElement(Element rowElement) {
        for(RowComponent<T> row : rows) {
            if(row.isRendered() && row.getElement().getElement().equals(rowElement)) {
                return row.getData();
            }
        }
        return null;
    }

    protected List<T> getModelsByRowElements(List<Element> rowElements) {
        List<T> models = new ArrayList<>();
        for(Element element : rowElements) {
            models.add(getModelByRowElement(element));
        }
        return models;
    }

    protected Map<CategoryComponent, List<RowComponent<T>>> generateRowMap(Components<RowComponent<T>> rows, boolean autoCreate) {
        Map<CategoryComponent, List<RowComponent<T>>> dataMap = new HashMap<>();

        for(CategoryComponent category : categories) {
            dataMap.put(category, new ArrayList<>());
        }

        for(RowComponent<T> row : rows) {
            CategoryComponent category = getCategory(row.getDataCategory());
            if(category == null && !hasCategory(row.getDataCategory())) {
                if(autoCreate) {
                    category = buildCategoryComponent(row);
                } else {
                    // When we are not auto creating we will
                    // orphan the data without a category.
                    category = getOrphansCategory();
                }
            }

            if(category != null) {
                dataMap.computeIfAbsent(category, k -> new ArrayList<>()).add(row);
            }
        }
        return dataMap;
    }

    protected List<RowComponent<T>> getRowsByCategory(Components<RowComponent<T>> rows, CategoryComponent category) {
        List<RowComponent<T>> byCategory = new ArrayList<>();
        for(RowComponent<T> row : rows) {
            if(row.getDataCategory().equals(category.getCategory())) {
                byCategory.add(row);
            }
        }
        return byCategory;
    }

    protected List<CategoryComponent> getHiddenCategories() {
        List<CategoryComponent> hidden = new ArrayList<>();
        for(CategoryComponent category : categories) {
            TableSubHeader element = category.getElement();
            if(element != null && !element.isVisible()) {
                hidden.add(category);
            }
        }
        return hidden;
    }

    protected List<CategoryComponent> getVisibleCategories() {
        List<CategoryComponent> visible = new ArrayList<>();
        for(CategoryComponent category : categories) {
            TableSubHeader element = category.getElement();
            if(element != null && element.isVisible()) {
                visible.add(category);
            }
        }
        return visible;
    }

    @Override
    public void setRowFactory(RowComponentFactory<T> rowFactory) {
        this.rowFactory = rowFactory;
    }

    @Override
    public void setCategoryFactory(CategoryComponentFactory categoryFactory) {
        this.categoryFactory = categoryFactory;
    }

    @Override
    public void setLoadMask(boolean loadMask) {
        if(!isSetup()) {
            // The widget isn't ready yet
            return;
        }
        if (!this.loadMask && loadMask) {
            if(isUseLoadOverlay()) {
                if (maskElement == null) {
                    maskElement = $("<div style='position:absolute;width:100%;height:100%;top:0;opacity:0.2;background-color:black;z-index:9999;'>" +
                        "<!--i style='left:50%;top:20%;z-index:9999;position:absolute;color:white' class='fa fa-3x fa-spinner fa-spin'></i-->" +
                        "</div>");
                }
                $table.prepend(maskElement);
            }
        } else if(!loadMask && maskElement != null) {
            maskElement.detach();
        }
        getProgressWidget().setVisible(loadMask);
        this.loadMask = loadMask;
    }

    @Override
    public boolean isLoadMask() {
        return loadMask;
    }

    @Override
    public MaterialProgress getProgressWidget() {
        return progressWidget;
    }

    @Override
    public int getTotalRows() {
        return totalRows;
    }

    @Override
    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    @Override
    public boolean isUseCategories() {
        return useCategories;
    }

    @Override
    public void setUseCategories(boolean useCategories) {
        this.useCategories = useCategories;
    }

    @Override
    public boolean isUseLoadOverlay() {
        return useLoadOverlay;
    }

    @Override
    public void setUseLoadOverlay(boolean useLoadOverlay) {
        this.useLoadOverlay = useLoadOverlay;
    }

    @Override
    public boolean isUseRowExpansion() {
        return useRowExpansion;
    }

    @Override
    public void setUseRowExpansion(boolean useRowExpansion) {
        this.useRowExpansion = useRowExpansion;
    }

    @Override
    public int getLongPressDuration() {
        return longPressDuration;
    }

    @Override
    public void setLongPressDuration(int longPressDuration) {
        this.longPressDuration = longPressDuration;
    }

    @Override
    public void loaded(int startIndex, List<T> data) {
        setRowData(startIndex, data);
        display.setLoadMask(false);
    }

    @Override
    public Panel getContainer() {
        return display.getContainer();
    }

    @Override
    public String getViewId() {
        return id;
    }

    @Override
    public void setDisplay(DataView<T> display) {
        assert display != null : "Display cannot be null";
        this.display = display;
    }

    @Override
    public final void fireEvent(GwtEvent<?> event) {
        table.fireEvent(event);
    }

    protected TableSubHeader bindCategoryEvents(TableSubHeader category) {
        if(category != null) {
            // Attach the category events
            category.$this().off("opened");
            category.$this().on("opened", (e, categoryElem) -> {
                return container.trigger(TableEvents.CATEGORY_OPENED, category.getName());
            });
            category.$this().off("closed");
            category.$this().on("closed", (e, categoryElem) -> {
                return container.trigger(TableEvents.CATEGORY_CLOSED, category.getName());
            });
        }
        return category;
    }

    public void updateCheckAllInputState() {
        updateCheckAllInputState(null);
    }

    protected void updateCheckAllInputState(JQueryElement input) {
        if(Js.isUndefinedOrNull(input)) {
            input = $table.find("th#col0 input");
        }
        input.prop("indeterminate", false);
        input.prop("checked", false);

        if($("tr.data-row:visible", getContainer()).length() > 0) {
            boolean fullSelection = !hasUnselectedRows(false);

            if (!fullSelection && hasSelectedRows(true)) {
                input.prop("indeterminate", true);
            } else if (fullSelection) {
                input.prop("checked", true);
            }
        }
    }

    protected List<RowComponent<T>> getRows() {
        return rows;
    }

    protected List<T> getData() {
        return RowComponent.extractData(rows);
    }

    /**
     * Get a stored data category by name.
     */
    protected CategoryComponent getCategory(String name) {
        if(name != null) {
            for (CategoryComponent category : categories) {
                if (category.getCategory().equals(name)) {
                    return category;
                }
            }
        } else {
            return getOrphansCategory();
        }
        return null;
    }

    /**
     * Get the {@link OrphanCategoryComponent} for orphan rows.
     */
    protected OrphanCategoryComponent getOrphansCategory() {
        for(CategoryComponent category : categories) {
            if(category instanceof OrphanCategoryComponent) {
                return (OrphanCategoryComponent)category;
            }
        }
        return null;
    }

    /**
     * Get a stored data categories subheader by name.
     */
    protected TableSubHeader getTableSubHeader(String name) {
        CategoryComponent category  = getCategory(name);
        return category != null ? category.getElement() : null;
    }

    /**
     * Get a stored data categories subheader by jquery element.
     */
    protected TableSubHeader getTableSubHeader(JQueryElement elem) {
        for(CategoryComponent category : categories) {
            TableSubHeader subheader = category.getElement();
            if(subheader != null && $(subheader).is(elem)) {
                return subheader;
            }
        }
        return null;
    }

    protected void clearExpansions() {
        $("tr.expansion", getContainer()).remove();
    }

    @Override
    public void clearRows(boolean clearData) {
        if(clearData) {
            rows.clear();
        } else {
            rows.clearElements();
        }
    }

    @Override
    public void clearCategories() {
        for(CategoryComponent category : categories) {
            TableSubHeader subheader = category.getElement();
            if(subheader != null) {
                subheader.removeFromParent();
            }
        }
        categories.clear();
    }

    @Override
    public void clearRowsAndCategories(boolean clearData) {
        clearRows(clearData);
        clearCategories();
    }

    protected void addHeader(int index, TableHeader header, Column<T, ?> column) {
        if(headers.size() < 1) {
            headers.add(header);
        } else {
            headers.add(index, header);
        }
        headerRow.insert(header, index);
    }

    protected void removeHeader(int index) {
        if(index < headers.size()) {
            headers.remove(index);
            headerRow.remove(index);
        }
        refreshStickyHeaders();
    }

    protected List<TableHeader> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    protected int getCategoryRowCount(String category) {
        int count = 0;
        for(RowComponent<T> row : rows) {
            String rowCategory = row.getDataCategory();
            if(rowCategory != null) {
                if(rowCategory.equals(category)) {
                    count++;
                }
            } else if(category == null) {
                // no category
                count++;
            }
        }
        return count;
    }

    protected void setRedrawCategories(boolean redrawCategories) {
        this.redrawCategories = redrawCategories;
    }

    @Override
    public void setHeight(String height) {
        this.height = height;

        // Avoid setting the height prematurely.
        if(setup) {
            tableBody.height(height);
        }
    }

    @Override
    public String getHeight() {
        return height;
    }
}
