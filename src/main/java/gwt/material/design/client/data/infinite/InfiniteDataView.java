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
package gwt.material.design.client.data.infinite;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.base.InterruptibleTask;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.SortContext;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;
import gwt.material.design.client.events.DefaultHandlerRegistry;
import gwt.material.design.client.events.HandlerRegistry;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.ui.accessibility.DataTableAccessibilityControls;
import gwt.material.design.client.ui.table.DataDisplay;
import gwt.material.design.client.ui.table.TableScaffolding;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * The InfiniteDataView is designed for lazy loading data, while using minimal DOM row elements.<br/>
 * The data source will invoke a load when the view index changes on scrolling.<br/>
 * <br/>
 * <b>How to use:</b>
 * <ul>
 *   <li>View size can be configured manually or can be set to dynamic view using {@link #DYNAMIC_VIEW}.</li>
 *   <li>Provide a valid {@link DataSource} implementation.</li>
 * </ul>
 *
 * @author Ben Dol
 */
public class InfiniteDataView<T> extends AbstractDataView<T> implements HasLoader {

    private static final Logger logger = Logger.getLogger(InfiniteDataView.class.getName());

    /**
     * Dynamic view will detect available space for the row views.
     */
    public static final int DYNAMIC_VIEW = -1;

    // Static view size specification
    // -1 is dynamic view sizing.
    private int viewSize = DYNAMIC_VIEW;
    private boolean dynamicView = true;

    // Buffers for artificial spacing when
    // cycling the rows within the view.
    private JQueryElement bufferTop;
    private JQueryElement bufferBottom;

    // The current index of the view.
    protected int viewIndex;
    protected int lastScrollTop = 0;

    // Lading new data flag
    private boolean loading;
    private boolean forceScroll;

    // Data loading task
    private InterruptibleTask loaderTask;

    protected int loaderBuffer = 10;
    protected int loaderIndex;
    protected int loaderSize;

    // Data loader delay millis
    private int loaderDelay = 200;

    protected List<T> loaderCache;

    // Cache the selected rows for persistence
    private List<T> selectedModels = new ArrayList<>();

    // Cached models
    protected InfiniteDataCache<T> dataCache = new InfiniteDataCache<>();

    // Row Loader
    protected InfiniteRowLoader<T> rowLoader = new InfiniteRowLoader<>(this);

    // Handler registry
    private HandlerRegistry handlers;

    public InfiniteDataView(int totalRows, DataSource<T> dataSource) {
        this(totalRows, DYNAMIC_VIEW, dataSource);
    }

    public InfiniteDataView(int totalRows, int viewSize, DataSource<T> dataSource) {
        super();
        this.viewSize = viewSize;

        setTotalRows(totalRows);
        setDataSource(dataSource);
    }

    public InfiniteDataView(String name, int totalRows, DataSource<T> dataSource) {
        this(name, totalRows, null, dataSource);
    }

    public InfiniteDataView(String name, int totalRows, ProvidesKey<T> keyProvider, DataSource<T> dataSource) {
        super(name, keyProvider);

        setTotalRows(totalRows);
        setDataSource(dataSource);
    }

    public InfiniteDataView(String name, int totalRows, int viewSize, DataSource<T> dataSource) {
        this(name, totalRows, viewSize, null, dataSource);
    }

    public InfiniteDataView(String name, int totalRows, int viewSize, ProvidesKey<T> keyProvider, DataSource<T> dataSource) {
        super(name, keyProvider);
        this.viewSize = viewSize;

        setTotalRows(totalRows);
        setDataSource(dataSource);
    }

    @Override
    protected void onConstructed() {
        setRenderer(new InfiniteRenderer<>());
    }

    @Override
    protected void onSetup(TableScaffolding scaffolding) {
        dynamicView = viewSize == DYNAMIC_VIEW;
        if (dynamicView) {
            setVisibleRange(0, getVisibleRowCapacity());
            setViewSize(range.getLength());
        }

        JQueryElement topWrapper = $("<div>");
        bufferTop = $("<div class='bufferTop'>");
        topWrapper.append(bufferTop);
        tbody.insert(new MaterialWidget(topWrapper.asElement()), 0);

        bufferBottom = $("<div class='bufferBottom'>");
        tableBody.append(bufferBottom);

        handlers.clearHandlers();

        handlers.registerHandler(display.addCategoryOpenedHandler(event -> {
            dataCache.clear();
            updateRows(viewIndex, true);
            forceScroll = true;
        }));

        handlers.registerHandler(display.addCategoryClosedHandler(event -> {
            dataCache.clear();
            updateRows(viewIndex, true);
            forceScroll = true;
        }));

        handlers.registerHandler(display.addRowSelectHandler(event -> {
            if (event.isSelected()) {
                if (!selectedModels.contains(event.getModel())) {
                    selectedModels.add(event.getModel());
                }
            } else {
                selectedModels.remove(event.getModel());
            }
        }));

        handlers.registerHandler(display.addSelectAllHandler(event -> {
            for (T model : event.getModels()) {
                if (event.isSelected()) {
                    if (!selectedModels.contains(model)) {
                        selectedModels.add(model);
                    }
                } else {
                    selectedModels.remove(model);
                }
            }
        }));

        // Setup the scroll event handlers
        JQueryExtension.$(tableBody).scrollY(id, (event, scroll) -> onVerticalScroll());

        // Accessibility Controls for infinite loading
        DataTableAccessibilityControls accessibilityControl = getAccessibilityControl();
        if (accessibilityControl != null) {
            accessibilityControl.registerInfiniteDataLoadingControl(this);
        }

        super.onSetup(scaffolding);
    }

    @Override
    public void setDisplay(DataDisplay<T> display) {
        super.setDisplay(display);

        if (handlers != null) {
            handlers.clearHandlers();
        }
        // Assign a new registry.
        handlers = new DefaultHandlerRegistry(this.display, false);
    }

    @Override
    public void render(Components<Component<?>> components) {
        int calcRowHeight = getCalculatedRowHeight();
        int topHeight = loaderIndex * calcRowHeight;
        int catHeight = getCategoryHeight();
        bufferTop.height(topHeight + (isUseCategories() ? (getPassedCategories().size() * catHeight) : 0));

        int categories = isUseCategories() ? getCategories().size() : 0;
        int bottomHeight = ((totalRows - viewSize - loaderBuffer) * calcRowHeight) - (categories * catHeight) - topHeight;
        bufferBottom.height(bottomHeight);

        super.render(components);

        tableBody.scrollTop(lastScrollTop);
    }

    @Override
    public boolean renderRows(Components<RowComponent<T>> rows) {
        int prevCategories = categories.size();
        if (super.renderRows(rows)) {
            if (isUseCategories()) {
                // Update the view size to accommodate the new categories
                int newCatCount = categories.size() - prevCategories;
                if (newCatCount != 0) {
                    setVisibleRange(viewIndex, viewSize - newCatCount);
                    setViewSize(range.getLength());
                }

                // show all the categories
                List<CategoryComponent> lastHidden = new ArrayList<>();
                for (CategoryComponent category : categories) {
                    if (category.isRendered()) {
                        category.getWidget().setVisible(true);
                    }

                    boolean hidden = false;
                    if (isCategoryEmpty(category)) {
                        Range range = getVisibleRange();
                        int reach = range.getStart() + range.getLength();

                        if (getTotalRows() > reach) {
                            if (category.isRendered()) {
                                category.getWidget().setVisible(false);
                            }
                            lastHidden.add(category);
                            hidden = true;
                        }
                    }

                    if (!hidden) {
                        // Reshow the previously hidden categories
                        // This is because we have found a valid category
                        // after these were hidden, implying valid data.
                        for (CategoryComponent hiddenCategory : lastHidden) {
                            if (hiddenCategory.isRendered()) {
                                hiddenCategory.getWidget().setVisible(true);
                            }
                        }
                    }
                }

                // hide passed empty categories
                for (CategoryComponent<T> category : getPassedCategories()) {
                    if (category.isRendered()) {
                        category.getWidget().setVisible(false);
                    }
                }

                subheaderLib.recalculate(true);
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean doSort(SortContext<T> sortContext, Components<RowComponent<T>> rows) {
        if (super.doSort(sortContext, rows)) {
            // TODO: Potentially sort the cache data?
            dataCache.clear(); // invalidate the cache upon successful sorts
            return true;
        } else {
            return false;
        }
    }

    protected void setViewSize(int viewSize) {
        this.viewSize = viewSize;
    }

    @Override
    public void refresh() {
        int rangeStart = range.getStart();
        setVisibleRange(rangeStart, 0);
        super.refresh();
        setVisibleRange(rangeStart, dynamicView ? getVisibleRowCapacity() : viewSize);
        setViewSize(range.getLength());
        updateRows(viewIndex, true);
        forceScroll = true;
    }

    @Override
    public void addCategory(CategoryComponent category) {
        super.addCategory(category);
        // Update the view size to accommodate the new category
        setVisibleRange(viewIndex, viewSize + 1);
        setViewSize(range.getLength());
    }

    public double getVisibleHeight() {
        // We only want to account for row space.
        return tableBody.height() - headerRow.$this().height();
    }

    public Object onVerticalScroll() {
        if (!rendering) {
            int index = (int) Math.ceil(tableBody.scrollTop() / getCalculatedRowHeight());
            if (index == 0 || index != viewIndex) {
                updateRows(index, false);
            }
        }
        return true;
    }

    protected void updateRows(int newIndex, boolean reload) {
        // the number of rows visible within the grid's viewport
        viewIndex = Math.min(newIndex, Math.max(0, totalRows - viewSize));
        requestData(viewIndex, !reload);
    }

    protected void requestData(int index, boolean checkCache) {
        if (loading) {
            // Avoid loading again before the last load
            return;
        }
        logger.fine("requestData() offset: " + index + ", viewSize: " + viewSize);
        loaderIndex = Math.max(0, index - loaderBuffer);
        loaderSize = viewSize + loaderBuffer;
        if (loaderTask == null) {
            loaderTask = new InterruptibleTask() {
                @Override
                public void onExecute() {
                    if (checkCache) {
                        List<T> cachedData = dataCache.getCache(loaderIndex, loaderSize);
                        if (!cachedData.isEmpty()) {
                            // Found in the cache
                            loaderCache = cachedData;
                        }
                    }
                    doLoad();
                }
            };
        }

        loaderTask.delay(loaderDelay);
    }

    protected void doLoad() {
        loading = true;

        // Check if the data was found in the cache
        if (loaderCache != null && !loaderCache.isEmpty()) {
            loaded(loaderIndex, loaderCache, false);
            loaderCache.clear();
            loaderCache = null;
        } else {
            setLoadMask(true);
            rowLoader.show();
            dataSource.load(new LoadConfig<T>(this, loaderIndex, loaderSize, getSortContext(), getOpenCategories()),
                new LoadCallback<T>() {
                    @Override
                    public void onSuccess(LoadResult<T> result) {
                        rowLoader.hide();
                        loaded(result.getOffset(), result.getData(), result.getTotalLength(), result.isCacheData());
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        logger.log(Level.SEVERE, "Load failure", caught);
                        loading = false;
                        // TODO: What we need to do on failure? Maybe clear table?
                    }
                });
        }
    }

    @Override
    public void setLoadMask(boolean loadMask) {
        if (loadMask || !forceScroll) {
            super.setLoadMask(loadMask);

            // Ensure the mask element uses max height
            Scheduler.get().scheduleDeferred(() -> {
                if (loadMask && maskElement != null) {
                    maskElement.height(bufferBottom.outerHeight(true) + bufferTop.outerHeight(true)
                        + tableBody.outerHeight(true) + 1000 + "px");
                }
            });
        }
    }

    @Override
    public void loaded(int startIndex, List<T> data) {
        loaded(startIndex, data, getTotalRows(), true);
    }

    /**
     * Provide the option to load data with a cache parameter.
     */
    public void loaded(int startIndex, List<T> data, boolean cacheData) {
        loaded(startIndex, data, getTotalRows(), cacheData);
    }

    /**
     * With infinite data loading it is often required to
     * change the total rows, upon loading of new data.
     * See {@link #loaded(int, List)} for standard use.
     *
     * @param startIndex the new start index
     * @param data       the new list of data loaded
     * @param totalRows  the new total row count
     */
    public void loaded(int startIndex, List<T> data, int totalRows, boolean cacheData) {
        lastScrollTop = tableBody.scrollTop();
        setTotalRows(totalRows);
        setVisibleRange(startIndex, loaderSize);

        if (cacheData) {
            dataCache.addCache(startIndex, data);
        }
        super.loaded(startIndex, data);
        loading = false;

        if (forceScroll) {
            forceScroll = false;
            updateRows((int) Math.ceil(tableBody.scrollTop() / getCalculatedRowHeight()), false);
        }

        // Ensure selection persistence
        for (T model : selectedModels) {
            Element row = getRowElementByModel(model);
            if (row != null) {
                selectRow(row, false);
            }
        }
    }

    /**
     * Returns the total number of rows that are visible given
     * the current grid height.
     * //TODO: Issue with returned number of rows causing the bottom area to have an excess whitespace.
     */
    public int getVisibleRowCapacity() {
        int rh = getCalculatedRowHeight();
        double visibleHeight = getVisibleHeight();
        int rows = (int) ((visibleHeight < 1) ? 0 : Math.floor(visibleHeight / rh));

        int calcHeight = rh * rows;

        while (calcHeight < visibleHeight) {
            rows++;
            calcHeight = rh * rows;
        }

        logger.finest("row height: " + rh + " visibleHeight: " + visibleHeight + " visible rows: "
            + rows + " calcHeight: " + calcHeight);
        return rows + 2;
    }

    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);

        // Update the view row size.
        if (isSetup()) {
            refresh();
        }
    }

    @Override
    public List<T> getSelectedRowModels(boolean visibleOnly) {
        if (visibleOnly) {
            return super.getSelectedRowModels(true);
        } else {
            return selectedModels;
        }
    }

    @Override
    public int getLoaderDelay() {
        return loaderDelay;
    }

    @Override
    public void setLoaderDelay(int loaderDelay) {
        this.loaderDelay = loaderDelay;
    }

    @Override
    public int getLoaderBuffer() {
        return loaderBuffer;
    }

    @Override
    public void setLoaderBuffer(int loaderBuffer) {
        this.loaderBuffer = Math.max(1, loaderBuffer);
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    public boolean isDynamicView() {
        return dynamicView;
    }
}
