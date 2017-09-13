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
package gwt.material.design.client.data;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.CategoryComponent.OrphanCategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.ComponentFactory;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.events.DestroyEvent;
import gwt.material.design.client.data.events.InsertColumnEvent;
import gwt.material.design.client.data.events.RangeChangeEvent;
import gwt.material.design.client.data.events.RemoveColumnEvent;
import gwt.material.design.client.data.events.SetupEvent;
import gwt.material.design.client.data.factory.CategoryComponentFactory;
import gwt.material.design.client.data.factory.RowComponentFactory;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.js.Js;
import gwt.material.design.client.js.JsTableElement;
import gwt.material.design.client.js.JsTableSubHeaders;
import gwt.material.design.client.js.StickyTableOptions;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.Selectors;
import gwt.material.design.client.ui.table.*;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.events.RowExpansion;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.*;
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
    protected DataDisplay display;
    protected DataSource<T> dataSource;
    protected Renderer<T> renderer;
    protected SortContext<T> sortContext;
    protected Column<T, ?> autoSortColumn;
    protected RowComponentFactory<T> rowFactory;
    protected ComponentFactory<? extends CategoryComponent, String> categoryFactory;
    protected ProvidesKey<T> keyProvider;
    //protected List<ComponentFactory<?, T>> componentFactories;
    protected JsTableSubHeaders subheaderLib;
    protected int categoryHeight = 0;
    protected int lastScrollTop = 0;
    protected String height;
    protected boolean rendering;
    protected boolean redraw;
    protected boolean redrawCategories;
    private boolean pendingRenderEvent;

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
    protected JQueryElement topPanel;

    // Configurations
    protected Range range = new Range(0, 0);
    protected int totalRows = 20;
    protected int longPressDuration = 500;

    private int lastSelected;
    private boolean setup;
    private boolean loadMask;
    private boolean shiftDown;
    private boolean useRowExpansion;
    private boolean useStickyHeader;
    private boolean useLoadOverlay;
    private boolean useCategories;
    private SelectionType selectionType = SelectionType.NONE;

    // Components
    protected final Components<RowComponent<T>> rows = new Components<>();
    protected final Components<RowComponent<T>> pendingRows = new Components<>();
    protected final Components<CategoryComponent> categories = new Components<>();

    // Rendering
    protected final List<Column<T, ?>> columns = new ArrayList<>();
    protected final List<TableHeader> headers = new ArrayList<>();
    protected HandlerRegistration attachHandler;

    public static final String ORPHAN_PATTERN = "<@orphans@>";

    private static final String expansionHtml = "<tr class='expansion'>" +
     "<td class='expansion' colspan='100%'>" +
        "<div>" +
            "<section class='overlay'>" +
                "<div class='progress' style='height:4px;top:-1px;'>" +
                    "<div class='indeterminate'></div>" +
                "</div>" +
            "</section>" +
            "<div class='content'><br/><br/><br/></div>" +
        "</div>" +
     "</td></tr>";

    public static final String maskHtml = "<div class='mask'>" +
        //"<!--i style='left:50%;top:20%;z-index:9999;position:absolute;color:white' class='fa fa-3x fa-spinner fa-spin'></i-->" +
    "</div>";

    public static final String transitionEvents = "transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd";

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
        onConstructed();
    }

    /**
     * Called after the data view is constructed.
     * Note that this is not when the data view is attached,
     * see {@link #setup(TableScaffolding)}.
     */
    protected void onConstructed() {
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
                category.setRowCount(0);
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
            Component<?> component = components.get(components.size() - 1);
            Widget componentWidget = component.getWidget();
            AttachEvent.Handler handler = event -> {
                if(attachHandler != null) {
                    attachHandler.removeHandler();
                }

                // Recheck the row height to ensure
                // the calculated row height is accurate.
                getCalculatedRowHeight();

                // Fixes an issue with heights updating too early.
                // Also ensure the cell widths are updated.
                subheaderLib.recalculate(true);

                // Fixes an issue with heights updating too early.
                subheaderLib.updateHeights();

                rendering = false;

                container.trigger(TableEvents.COMPONENTS_RENDERED, null);

                if(pendingRenderEvent) {
                    container.trigger(TableEvents.RENDERED, null);
                    pendingRenderEvent = false;
                }
            };
            if (componentWidget == null || componentWidget.isAttached()) {
                handler.onAttachOrDetach(null);
            } else {
                attachHandler = componentWidget.addAttachHandler(handler);
            }
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
    protected boolean renderRows(Components<RowComponent<T>> rows) {
        // Make sure we are setup, if we aren't then store the rows
        // the rows will be attached upon setup.
        if(!setup) {
            pendingRows.clear();
            pendingRows.addAll(rows);
            return false; // early exit, not setup yet.
        }
        rendering = true;
        Range visibleRange = getVisibleRange();

        // Check if we need to redraw categories
        if(redrawCategories) {
            redrawCategories = false;

            // When we perform a category redraw we have
            // to clear the row elements also.
            this.rows.clearWidgets();

            if (isUseCategories()) {
                List<CategoryComponent> openCategories = getOpenCategories();
                categories.clearWidgets();

                for (CategoryComponent category : categories) {
                    // Re-render the category component
                    renderComponent(category);

                    if (openCategories.contains(category) && category.isRendered()) {
                        subheaderLib.open(category.getWidget().$this());
                    }
                }
            } else {
                categories.clearWidgets();
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
                CategoryComponent category = row.getCategory();
                if(category == null) {
                    category = buildCategoryComponent(row);
                    categories.add(category);
                }

                if (category.isRendered()) {
                    category.getWidget().setVisible(true);
                }
            }

            // Do we have an existing row to use
            if (index < this.rows.size()) {
                RowComponent<T> existingRow = this.rows.get(index);
                if(existingRow != null) {
                    // Replace the rows element with the
                    // existing indexes element.
                    row.setWidget(existingRow.getWidget());
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
        return true;
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
                if(isUseCategories()){
                    category = rowComponent.getCategory();

                    // Ensure the category exists and is rendered
                    if(category != null && !category.isRendered()) {
                        renderComponent(category);
                    }
                }
                T data = rowComponent.getData();

                // Draw the table row
                row = renderer.drawRow(this, rowComponent, getValueKey(data), columns, redraw);

                if(row != null) {
                    if(category != null) {
                        if(categories.size() > 1) {
                            int categoryIndex = 0/*category.getCurrentIndex()*/;
                            //if(categoryIndex == -1) {
                                categoryIndex = tbody.getWidgetIndex(category.getWidget());
                                //category.setCurrentIndex(categoryIndex);
                            //}

                            int categoryCount = category.getRowCount() + 1;
                            category.setRowCount(categoryCount);

                            // Calculate the rows index
                            index = (categoryIndex + categoryCount) - 1;
                        }

                        // Check the display of the row
                        TableSubHeader subHeader = category.getWidget();
                        if (subHeader != null && subHeader.isOpen()) {
                            row.getElement().getStyle().clearDisplay();
                        }
                    } else {
                        // Not using categories
                        row.getElement().getStyle().clearDisplay();
                    }

                    rows.add(rowComponent);
                }
            } else if(component instanceof CategoryComponent) {
                CategoryComponent categoryComponent = (CategoryComponent)component;
                row = bindCategoryEvents(renderer.drawCategory(categoryComponent));

                if(categoryComponent.isOpenByDefault()) {
                    row.addAttachHandler(event -> openCategory(categoryComponent), true);
                }
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

    protected void renderColumns() {
        for (Column<T, ?> column : columns) {
            renderColumn(column);
        }
    }

    public void renderColumn(Column<T, ?> column) {
        int index = columns.indexOf(column) + getColumnOffset();

        TableHeader th = renderer.drawColumnHeader(column, column.getName(), index);
        if (th != null) {
            if (column.isSortable()) {
                th.$this().on("click", e -> {
                    sort(rows, th, column, index);
                    return true;
                });
                th.addStyleName(TableCssName.SORTABLE);
            }

            addHeader(index, th);
        }

        for (RowComponent<T> row : rows) {
            Context context = new Context(row.getIndex(), index, getValueKey(row.getData()));
            renderer.drawColumn(row.getWidget(), context, row.getData(), column, index, true);
        }

        refreshStickyHeaders();
    }

    @Override
    public void refresh() {
        // Recheck the row height to ensure
        // the calculated row height is accurate.
        getCalculatedRowHeight();

        if(redraw && setup) {
            // Render the rows
            renderRows(rows);
        }
    }

    @Override
    public void setRenderer(Renderer<T> renderer) {
        if(this.renderer != null) {
            // Copy existing render properties.
            renderer.copy(this.renderer);
        }
        this.renderer = renderer;
    }

    public Renderer<T> getRenderer() {
        return renderer;
    }

    @Override
    public void setDataSource(DataSource<T> dataSource) {
        if(dataSource instanceof HasDataView) {
            ((HasDataView<T>) dataSource).setDataView(this);
        }
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
            topPanel = $(scaffolding.getTopPanel());
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
                addHeader(0, expandHeader);
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
                refresh();
                return true;
            });

            // We will check the window resize just in case
            // it has updated the view size of the data view.
            $(window()).on("resize." + id, e -> {
                refresh();
                return true;
            });

            setup = true;

            onSetup(scaffolding);

            SetupEvent.fire(this, scaffolding);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Problem setting up the DataView.", ex);
            throw ex;
        }
    }

    protected void onSetup(TableScaffolding scaffolding) {
        // We are setup, lets check the render tasks
        if(height != null) {
            setHeight(height);
        }

        setSelectionType(selectionType);

        renderColumns();

        for(CategoryComponent category : categories) {
            if(!category.isRendered()) {
                renderCategory(category);
            }
        }

        if(!pendingRows.isEmpty()) {
            Components<RowComponent<T>> sortedRows = null;
            if(maybeApplyAutoSortColumn()) {
                // We have an auto sort column, sort the pending rows.
                Column<T, ?> column = sortContext.getSortColumn();
                sortedRows = sort(pendingRows, sortContext.getTableHeader(), column, columns.indexOf(column) + getColumnOffset(), false);
            }

            if(sortedRows == null) {
                renderRows(pendingRows);
                pendingRows.clearComponents();
            } else {
                renderRows(sortedRows);
            }
        }
    }

    @Override
    public void destroy() {
        rows.clear();
        categories.clear();

        columns.clear();
        headers.clear();
        headerRow.clear();

        container.off("." + id);
        tableBody.off("." + id);
        $(window()).off("." + id);

        $table.stickyTableHeaders("destroy");
        subheaderLib.unload();

        setRedraw(true);
        rendering = false;
        setup = false;

        // display control
        display.removeJQueryHandlers();

        DestroyEvent.fire(this);
    }

    /**
     * Prepare all row specific functionality.
     */
    protected void prepareRows() {
        JQueryElement rows = $table.find("tr.data-row");
        rows.off("." + id);

        // Select row click bind
        // This will also update the check status of check all input.
        rows.on("tap." + id + " click." + id, (e, o) -> {
             Element row = $(e.getCurrentTarget()).asElement();
            int rowIndex = getRowIndexByElement(row);
            if (selectionType.equals(SelectionType.MULTIPLE) && shiftDown) {
                if (lastSelected < rowIndex) {
                    // Increment
                    for (int i = lastSelected; i <= rowIndex; i++) {
                        if (i < getVisibleItemCount()) {
                            RowComponent<T> rowComponent = this.rows.get(i);
                            if (rowComponent != null && rowComponent.isRendered()) {
                                selectRow(rowComponent.getWidget().getElement(), true);
                            }
                        }
                    }
                } else {
                    // Decrement
                    for (int i = lastSelected - 1; i >= rowIndex - 1; i--) {
                        if (i >= 0) {
                            RowComponent<T> rowComponent = this.rows.get(i);
                            if (rowComponent != null && rowComponent.isRendered()) {
                                selectRow(rowComponent.getWidget().getElement(), true);
                            }
                        }
                    }
                }
            } else {
                toggleRowSelect(row);
            }
            return false;
        });

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
                        expansion[0] = $(expansionHtml).insertAfter(tr);
                        expansion[0] = expansion[0].find("td.expansion div");
                    }

                    final boolean expanding = !expansion[0].hasClass("expanded");
                    final JQueryElement row = tr.next();
                    final T model = getModelByRowElement(tr.asElement());

                    expansion[0].one(transitionEvents,
                        (e1, param1) -> {
                            if (!recalculated[0]) {
                                // Recalculate subheaders
                                subheaderLib.recalculate(true);
                                recalculated[0] = true;

                                // Apply overlay
                                JQueryElement overlay = row.find("section.overlay");
                                overlay.height(row.outerHeight(false));

                                if(expanding) {
                                    // Fire table expanded event
                                    container.trigger(TableEvents.ROW_EXPANDED, new RowExpansion<>(model, row));
                                } else {
                                    // Fire table collapsed event
                                    container.trigger(TableEvents.ROW_COLLAPSED, new RowExpansion<>(model, row));
                                }
                            }
                            return true;
                        });

                    if(expanding) {
                        // Fire table expand event
                        container.trigger(TableEvents.ROW_EXPAND, new RowExpansion<>(model, row));
                    } else {
                        container.trigger(TableEvents.ROW_COLLAPSE, new RowExpansion<>(model, row));
                    }

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
            $(subheaderLib).off("before-recalculate");
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

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Range getVisibleRange() {
        return range;
    }

    @Override
    public void setVisibleRange(int start, int length) {
        setVisibleRange(new Range(start, length));
    }

    @Override
    public void setVisibleRange(Range range) {
        setVisibleRange(range, true);
    }

    protected void setVisibleRange(Range range, boolean forceRangeChangeEvent) {
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
            // Update the range start
            this.range = new Range(start, this.range.getLength());
        }

        // Update the page size
        final boolean pageSizeChanged = (pageSize != length);
        if (pageSizeChanged) {
            this.range = new Range(this.range.getStart(), length);
        }

        // Clear the rows
        rows.clear();

        // Update the pager and data source if the range changed
        if (pageStartChanged || pageSizeChanged || forceRangeChangeEvent) {
            RangeChangeEvent.fire(this, getVisibleRange());
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

        if(columns.size() < beforeIndex) {
            columns.add(column);
        } else {
            columns.add(beforeIndex, column);
        }

        if(setup) {
            renderColumn(column);
        }

        InsertColumnEvent.fire(this, beforeIndex, column, header);
    }

    protected void updateSortContext(TableHeader th, Column<T, ?> column) {
        updateSortContext(th, column, null);
    }

    protected void updateSortContext(TableHeader th, Column<T, ?> column, SortDir dir) {
        if(sortContext == null) {
            sortContext = new SortContext<>(column, th);
        } else {
            Column<T, ?> sortColumn = sortContext.getSortColumn();
            if(sortColumn != column) {
                sortContext.setSortColumn(column);
                sortContext.setTableHeader(th);
            } else if(dir == null && sortContext.isSorted()) {
                sortContext.reverse();
            }
        }
        if(dir != null) {
            sortContext.setSortDir(dir);
        }
    }

    @Override
    public void sort(int columnIndex) {
        sort(columnIndex, null);
    }

    @Override
    public void sort(int columnIndex, SortDir dir) {
        sort(columns.get(columnIndex), dir);
    }

    @Override
    public void sort(Column<T, ?> column) {
        sort(column, null);
    }

    @Override
    public void sort(Column<T, ?> column, SortDir dir) {
        if(column != null) {
            int index = columns.indexOf(column) + getColumnOffset();
            TableHeader th = headers.get(index);
            sort(rows, th, column, index, dir);
        } else {
            throw new RuntimeException("Cannot sort on a null column.");
        }
    }

    protected Components<RowComponent<T>> sort(Components<RowComponent<T>> rows, TableHeader th, Column<T, ?> column,
                                               int index) {
        return sort(rows, th, column, index, dataSource == null || !dataSource.useRemoteSort());
    }

    protected Components<RowComponent<T>> sort(Components<RowComponent<T>> rows, TableHeader th, Column<T, ?> column,
                                               int index, SortDir dir) {
        return sort(rows, th, column, index, dir, dataSource == null || !dataSource.useRemoteSort());
    }

    protected Components<RowComponent<T>> sort(Components<RowComponent<T>> rows, TableHeader th, Column<T, ?> column,
                                               int index, boolean renderRows) {
        return sort(rows, th, column, index, null, renderRows);
    }

    protected Components<RowComponent<T>> sort(Components<RowComponent<T>> rows, TableHeader th, Column<T, ?> column,
                                               int index, SortDir dir, boolean renderRows) {
        SortContext<T> oldSortContext = new SortContext<>(this.sortContext);
        updateSortContext(th, column, dir);

        Components<RowComponent<T>> clonedRows = new Components<>(rows, RowComponent::new);
        if(doSort(sortContext, clonedRows)) {
            th.addStyleName(TableCssName.SELECTED);

            // Draw and apply the sort icon.
            renderer.drawSortIcon(th, sortContext);

            // No longer a fresh sort
            sortContext.setSorted(true);

            if (renderRows) {
                // Render the new sort order.
                renderRows(clonedRows);
            }

            container.trigger(TableEvents.SORT_COLUMN, new Object[]{sortContext, index});
        } else {
            // revert the sort context
            sortContext = oldSortContext;
        }

        return clonedRows;
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
        if (isUseCategories()) {
            // Split row data into categories
            Map<String, List<RowComponent<T>>> splitMap = new HashMap<>();
            List<RowComponent<T>> orphanRows = new ArrayList<>();

            for (RowComponent<T> row : rows) {
                if(row != null) {
                    String category = row.getCategoryName();
                    if(category != null) {
                        List<RowComponent<T>> data = splitMap.computeIfAbsent(category, k -> new ArrayList<>());
                        data.add(row);
                    } else {
                        orphanRows.add(row);
                    }
                }
            }

            if(!orphanRows.isEmpty()) {
                splitMap.put(ORPHAN_PATTERN, orphanRows);
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
            } else if(sortContext != null) {
                rows.sort(new DataSort<>(new Comparator<RowComponent<T>>() {
                    @Override
                    public int compare(RowComponent<T> o1, RowComponent<T> o2) {
                        return o1.getData().toString().compareToIgnoreCase(o2.getData().toString());
                    }
                }, sortContext.getSortDir()));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void removeColumn(int colIndex) {
        removeColumn(colIndex, true);
    }

    public void removeColumn(int colIndex, boolean hardRemove) {
        int index = colIndex + getColumnOffset();
        headerRow.remove(index);

        for(RowComponent<T> row : rows) {
            row.getWidget().remove(index);
        }

        reindexColumns();
        refreshStickyHeaders();

        if(hardRemove) {
            columns.remove(colIndex);

            RemoveColumnEvent.fire(this, colIndex);
        }
    }

    @Override
    public void removeColumns() {
        if(!columns.isEmpty()) {
            int size = columns.size() - 1;
            for (int i = 0; i < size; i++) {
                removeColumn(i, false);
            }
            columns.clear();

            for (int i = 0; i < size; i++) {
                RemoveColumnEvent.fire(this, i);
            }
        }
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
        if(setup) {
            if (!selectionType.equals(SelectionType.NONE) && !hadSelection) {
                setupHeaderSelectionBox();

                if(selectionType.equals(SelectionType.MULTIPLE)) {
                    setupShiftDetection();
                }

                // Rebuild the columns
                for (RowComponent<T> row : rows) {
                    row.getWidget().insert(renderer.drawSelectionCell(), 0);
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
        th.setStyleName(TableCssName.SELECTION);
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

                selectAllRows(!marked || hasDeselectedRows(true));
                return false;
            });
        }
        addHeader(0, th);
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

        for(RowComponent<T> row : rows) {
            TableRow tableRow = row.getWidget();
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
    public boolean isRendering() {
        return rendering;
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
                // deselect all rows when using single selection
                if(!selectionType.equals(SelectionType.MULTIPLE)) {
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
                // deselect all rows when using single selection
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
    public void deselectRow(Element row, boolean fireEvent) {
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
    public boolean hasDeselectedRows(boolean visibleOnly) {
        return $table.find(Selectors.rowInputNotCheckedSelector + (visibleOnly ? ":visible" : "")).length() > 0;
    }

    @Override
    public boolean hasSelectedRows(boolean visibleOnly) {
        return $table.find(Selectors.rowInputCheckedSelector + (visibleOnly ? ":visible" : "")).length() > 0;
    }

    @Override
    public List<T> getSelectedRowModels(boolean visibleOnly) {
        final List<T> models = new ArrayList<>();
        $table.find(Selectors.rowInputCheckedSelector + (visibleOnly ? ":visible" : "")).each((i, e) -> {
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
        if(range.getStart() < 0 || range.getLength() < 1) {
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

        pendingRenderEvent = true;

        if(maybeApplyAutoSortColumn()) {
            // We have an auto sort column, sort the new rows.
            Column<T, ?> column = sortContext.getSortColumn();
            rows = sort(rows, sortContext.getTableHeader(), column, columns.indexOf(column) + getColumnOffset(), false);
        }

        // Render the new rows normally
        renderRows(rows);
    }

    /**
     * Check and apply the auto sort column {@link Column#setAutoSort(boolean)}
     * if no sort has been invoked.
     * @return true if the auto sort column is assigned.
     */
    protected boolean maybeApplyAutoSortColumn() {
        // Check if we already have a sort column
        if(sortContext == null || sortContext.getSortColumn() == null) {
            Column<T, ?> autoSortColumn = getAutoSortColumn();

            if(autoSortColumn != null) {
                if(setup) {
                    int index = columns.indexOf(autoSortColumn) + getColumnOffset();
                    updateSortContext(headers.get(index), autoSortColumn);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get the auto sorting column, or null if no column is auto sorting.
     */
    protected Column<T, ?> getAutoSortColumn() {
        if(autoSortColumn == null) {
            for (Column<T, ?> column : columns) {
                if (column.isAutoSort()) {
                    autoSortColumn = column;
                    return autoSortColumn;
                }
            }
        }
        return autoSortColumn;
    }

    public ComponentFactory<? extends CategoryComponent, String> getCategoryFactory() {
        return categoryFactory;
    }

    protected RowComponent<T> buildRowComponent(T data) {
        if(data != null) {
            assert rowFactory != null : "The dataview's row factory cannot be null";
            return /*buildCustomComponents(*/rowFactory.generate(this, data)/*)*/;
        }
        return null;
    }

    protected CategoryComponent buildCategoryComponent(RowComponent<T> row) {
        return row != null ? buildCategoryComponent(row.getCategoryName()) : null;
    }

    protected CategoryComponent buildCategoryComponent(String categoryName) {
        if(categoryName != null) {
            // Generate the category if not exists
            if (categoryFactory != null) {
                CategoryComponent category = getCategory(categoryName);
                if (category == null) {
                    return categoryFactory.generate(this, categoryName);
                } else {
                    return category;
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
        if(category != null && !hasCategory(category.getName())) {
            categories.add(category);

            if(setup && isUseCategories()) {
                renderCategory(category);
            }
        }
    }

    protected void renderCategory(CategoryComponent category) {
        if(category != null) {
            // Render the category component
            renderComponent(category);

            if (subheaderLib != null) {
                subheaderLib.detect();
                subheaderLib.recalculate(true);
            }
        }
    }

    @Override
    public boolean hasCategory(String categoryName) {
        if(categoryName != null) {
            for (CategoryComponent category : categories) {
                if (category.getName().equals(categoryName)) {
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
            subheaderLib.close(category.getWidget().$this());
            category.getWidget().setEnabled(false);
        }
    }

    @Override
    public void enableCategory(String categoryName) {
        CategoryComponent category = getCategory(categoryName);
        if(category != null && category.isRendered()) {
            category.getWidget().setEnabled(true);
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
                TableSubHeader element = category.getWidget();
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
            if(row.getCategoryName().equals(category.getName())) {
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
    public void updateRow(final T model) {
        RowComponent<T> row = getRowByModel(model);
        if (row != null) {
            row.setRedraw(true);
            row.setData(model);
            renderComponent(row);
        }
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

    @Override
    public RowComponent<T> getRowByModel(T model) {
        for (final RowComponent<T> row : rows) {
            if (row.getData().equals(model)) {
                return row;
            }
        }
        return null;
    }

    protected int getRowIndexByElement(Element rowElement) {
        for(RowComponent<T> row : rows) {
            if(row.isRendered() && row.getWidget().getElement().equals(rowElement)) {
                return row.getIndex();
            }
        }
        return -1;
    }

    protected Element getRowElementByModel(T model) {
        for(RowComponent<T> row : rows) {
            if(row.getData().equals(model)) {
                return row.getWidget().getElement();
            }
        }
        return null;
    }

    protected T getModelByRowElement(Element rowElement) {
        for(RowComponent<T> row : rows) {
            if(row.isRendered() && row.getWidget().getElement().equals(rowElement)) {
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

    protected List<RowComponent<T>> getRowsByCategory(Components<RowComponent<T>> rows, CategoryComponent category) {
        List<RowComponent<T>> byCategory = new ArrayList<>();
        for(RowComponent<T> row : rows) {
            if(row.getCategoryName().equals(category.getName())) {
                byCategory.add(row);
            }
        }
        return byCategory;
    }

    protected List<CategoryComponent> getHiddenCategories() {
        List<CategoryComponent> hidden = new ArrayList<>();
        for(CategoryComponent category : categories) {
            TableSubHeader element = category.getWidget();
            if(element != null && !element.isVisible()) {
                hidden.add(category);
            }
        }
        return hidden;
    }

    protected List<CategoryComponent> getVisibleCategories() {
        List<CategoryComponent> visible = new ArrayList<>();
        for(CategoryComponent category : categories) {
            TableSubHeader element = category.getWidget();
            if(element != null && element.isVisible()) {
                visible.add(category);
            }
        }
        return visible;
    }

    protected List<CategoryComponent> getPassedCategories() {
        List<CategoryComponent> passed = new ArrayList<>();
        int scrollTop = tableBody.scrollTop();
        for(CategoryComponent category : categories) {
            if(isCategoryEmpty(category) && scrollTop > (getRowHeight() + thead.$this().height())) {
                passed.add(category);
            } else {
                // Hit the current category
                return passed;
            }
        }
        // No categories are populated.
        return new ArrayList<>();
    }

    @Override
    public void setRowFactory(RowComponentFactory<T> rowFactory) {
        this.rowFactory = rowFactory;
    }

    @Override
    public RowComponentFactory<T> getRowFactory() {
        return rowFactory;
    }

    @Override
    public void setCategoryFactory(ComponentFactory<? extends CategoryComponent, String> categoryFactory) {
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
                    maskElement = $(maskHtml);
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
        if(this.useCategories && !useCategories) {
            //subheaderLib.unload();
            categories.clearWidgets();
            setRedrawCategories(true);
        }
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
        setLoadMask(false);
    }

    @Override
    public Widget getContainer() {
        return display.asWidget();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setDisplay(DataDisplay display) {
        assert display != null : "Display cannot be null";
        this.display = display;
    }

    @Override
    public final void fireEvent(GwtEvent<?> event) {
        getContainer().fireEvent(event);
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
            boolean fullSelection = !hasDeselectedRows(false);

            if (!fullSelection && hasSelectedRows(true)) {
                input.prop("indeterminate", true);
            } else if (fullSelection) {
                input.prop("checked", true);
            }
        }
    }

    public List<RowComponent<T>> getRows() {
        return Collections.unmodifiableList(rows);
    }

    protected List<T> getData() {
        return RowComponent.extractData(rows);
    }

    /**
     * Get a stored data category by name.
     */
    @Override
    public CategoryComponent getCategory(String name) {
        if(name != null) {
            for (CategoryComponent category : categories) {
                if (category.getName().equals(name)) {
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

    public int getCategoryHeight() {
        if (isUseCategories() && categoryHeight == 0) {
            try {
                CategoryComponent categoryComponent = categories.get(0);
                if (categoryComponent != null && categoryComponent.isRendered()) {
                    categoryHeight = categoryComponent.getWidget().getOffsetHeight();
                }
            } catch (IndexOutOfBoundsException ex) {
                logger.log(Level.FINE, "Couldn't get the first category.", ex);
            }
        }
        return categoryHeight;
    }

    @Override
    public void openCategory(String categoryName) {
        openCategory(getCategory(categoryName));
    }

    @Override
    public void openCategory(CategoryComponent category) {
        if(category != null && category.isRendered()) {
            subheaderLib.open(category.getWidget().$this());
        }
    }

    @Override
    public void closeCategory(String categoryName) {
        closeCategory(getCategory(categoryName));
    }

    @Override
    public void closeCategory(CategoryComponent category) {
        if(category != null && category.isRendered()) {
            subheaderLib.close(category.getWidget().$this());
        }
    }

    /**
     * Get a stored data categories subheader by name.
     */
    protected TableSubHeader getTableSubHeader(String name) {
        CategoryComponent category  = getCategory(name);
        return category != null ? category.getWidget() : null;
    }

    /**
     * Get a stored data categories subheader by jquery element.
     */
    protected TableSubHeader getTableSubHeader(JQueryElement elem) {
        for(CategoryComponent category : categories) {
            TableSubHeader subheader = category.getWidget();
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
            rows.clearWidgets();
        }
    }

    @Override
    public void clearCategories() {
        for(CategoryComponent category : categories) {
            TableSubHeader subheader = category.getWidget();
            if(subheader != null && subheader.isAttached()) {
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

    @Override
    public List<TableHeader> getHeaders() {
        return Collections.unmodifiableList(headers);
    }

    protected void addHeader(int index, TableHeader header) {
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

    protected int getCategoryRowCount(String category) {
        int count = 0;
        for(RowComponent<T> row : rows) {
            String rowCategory = row.getCategoryName();
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

    public boolean isShiftDown() {
        return shiftDown;
    }
}
