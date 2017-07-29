package gwt.material.design.client.data.infinite;

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


import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.SortContext;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions.EventFunc3;
import gwt.material.design.client.base.InterruptibleTask;
import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.ui.table.TableEvents;
import gwt.material.design.client.ui.table.TableScaffolding;
import gwt.material.design.jquery.client.api.JQueryElement;

import java.util.ArrayList;
import java.util.List;
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
public class InfiniteDataView<T> extends AbstractDataView<T> {

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
    protected int indexOffset = 10;
    protected int lastScrollTop = 0;

    // Lading new data flag
    private boolean loading;
    private boolean forceScroll;

    // Data loading task
    private InterruptibleTask loaderTask;

    private int loaderIndex;
    private int loaderSize;

    // Data loader delay millis
    private int loaderDelay = 200;

    private List<T> loaderCache;

    // Cache the selected rows for persistence
    private List<T> selectedModels = new ArrayList<>();

    // Cached models
    private InfiniteDataCache<T> dataCache = new InfiniteDataCache<>();

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
    protected void onLoaded() {
        setRenderer(new InfiniteRenderer<>());
    }

    @Override
    public void setup(TableScaffolding scaffolding) throws Exception {
        super.setup(scaffolding);

        dynamicView = viewSize == DYNAMIC_VIEW;
        if(dynamicView) {
            setVisibleRange(0, getVisibleRowCapacity());
            setViewSize(range.getLength());
        }

        JQueryElement topWrapper = $("<div>");
        bufferTop = $("<div class='bufferTop'>");
        topWrapper.append(bufferTop);
        tbody.insert(new MaterialWidget(topWrapper.asElement()), 0);

        bufferBottom = $("<div class='bufferBottom'>");
        tableBody.append(bufferBottom);

        container.off(TableEvents.CATEGORY_OPENED);
        container.on(TableEvents.CATEGORY_OPENED, (e, category) -> {
            dataCache.clear();
            updateRows(viewIndex, true);
            forceScroll = true;
            return true;
        });

        container.off(TableEvents.CATEGORY_CLOSED);
        container.on(TableEvents.CATEGORY_CLOSED, (e, category) -> {
            dataCache.clear();
            updateRows(viewIndex, true);
            forceScroll = true;
            return true;
        });

        container.off(TableEvents.ROW_SELECT);
        container.on(TableEvents.ROW_SELECT, new EventFunc3<T, Element, Boolean>() {
            @Override
            public Object call(Event e, T model, Element element, Boolean selected) {
                if(selected) {
                    if(!selectedModels.contains(model)) {
                        selectedModels.add(model);
                    }
                } else {
                    selectedModels.remove(model);
                }
                return true;
            }
        });

        container.off(TableEvents.SELECT_ALL);
        container.on(TableEvents.SELECT_ALL, new EventFunc3<List<T>, List<Element>, Boolean>() {
            @Override
            public Object call(Event e, List<T> models, List<Element> elements, Boolean selected) {
                for(T model : models) {
                    if (selected) {
                        if (!selectedModels.contains(model)) {
                            selectedModels.add(model);
                        }
                    } else {
                        selectedModels.remove(model);
                    }
                }
                return true;
            }
        });

        // Setup the scroll event handlers
        JQueryExtension.$(tableBody).scrollY(id, (e, scroll) ->  onVerticalScroll());
    }

    @Override
    public void render(Components<Component<?>> components) {
        int calcRowHeight = getCalculatedRowHeight();
        int topHeight = loaderIndex * calcRowHeight;
        int catHeight = getCategoryHeight();
        bufferTop.height(topHeight + (isUseCategories() ? (getPassedCategories().size() * catHeight) : 0));

        int categories = isUseCategories() ? getCategories().size() : 0;
        int bottomHeight = ((totalRows - viewSize - indexOffset) * calcRowHeight) - (categories * catHeight) - topHeight;
        bufferBottom.height(bottomHeight);

        super.render(components);

        tableBody.scrollTop(lastScrollTop);
    }

    @Override
    public boolean renderRows(Components<RowComponent<T>> rows) {
        int prevCategories = categories.size();
        if(super.renderRows(rows)) {
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

                        if (reach < getTotalRows()) {
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
                for (CategoryComponent category : getPassedCategories()) {
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
        if(super.doSort(sortContext, rows)) {
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
    public void refreshView() {
        super.refreshView();
        int rangeStart = range.getStart();
        setVisibleRange(rangeStart, dynamicView ? getVisibleRowCapacity() : viewSize);
        setViewSize(range.getLength());
        updateRows(viewIndex, true);
        forceScroll = true;
    }

    @Override
    public void setDataSource(final DataSource<T> dataSource) {
        if(!dataSource.useRemoteSort()) {
            logger.warning("It is recommended that an InfiniteDataView only use a remote sorting " +
                "DataSource, this will be set for you.");

            super.setDataSource(new DataSource<T>() {
                @Override
                public void load(LoadConfig<T> loadConfig, LoadCallback<T> callback) {
                    dataSource.load(loadConfig, callback);
                }
                @Override
                public boolean useRemoteSort() {
                    return true;
                }
            });
        } else {
            super.setDataSource(dataSource);
        }
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

    protected Object onVerticalScroll() {
        if(!rendering) {
            int index = (int) Math.ceil(tableBody.scrollTop() / getCalculatedRowHeight());
            if(index == 0 || index != viewIndex) {
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
        if(loading) {
            // Avoid loading again before the last load
            return;
        }
        logger.finest("requestData() offset: " + index + ", viewSize: " + viewSize);
        loaderIndex = Math.max(0, index - indexOffset);
        loaderSize = viewSize + indexOffset;
        if (loaderTask == null) {
            loaderTask = new InterruptibleTask() {
                @Override
                public void onExecute() {
                    if(checkCache) {
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
        if(loaderCache != null && !loaderCache.isEmpty()) {
            loaded(loaderIndex, loaderCache, false);
            loaderCache.clear();
            loaderCache = null;
        } else {
            Scheduler.get().scheduleFinally(() -> display.setLoadMask(true));

            dataSource.load(new LoadConfig<>(loaderIndex, loaderSize, getSortContext(), getOpenCategories()),
                    new LoadCallback<T>() {
                @Override
                public void onSuccess(LoadResult<T> result) {
                    loaded(result.getOffset(), result.getData(), result.getTotalLength(), result.isCacheData());
                }
                @Override
                public void onFailure(Throwable caught) {
                    GWT.log("Load failure", caught);
                    //TODO: What we need to do on failure? Maybe clear table?
                }
            });
        }
    }

    @Override
    public void setLoadMask(boolean loadMask) {
        if(loadMask || !forceScroll) {
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
     * @param data the new list of data loaded
     * @param totalRows the new total row count
     */
    public void loaded(int startIndex, List<T> data, int totalRows, boolean cacheData) {
        lastScrollTop = tableBody.scrollTop();
        setTotalRows(totalRows);
        setVisibleRange(startIndex, loaderSize);

        if(cacheData) {
            dataCache.addCache(startIndex, data);
        }
        super.loaded(startIndex, data);
        loading = false;

        if(forceScroll) {
            forceScroll = false;
            updateRows((int) Math.ceil(tableBody.scrollTop() / getCalculatedRowHeight()), false);
        }

        // Ensure selection persistence
        for(T model : selectedModels) {
            Element row = getRowElementByModel(model);
            if(row != null) {
                selectRow(row, false);
            }
        }
    }

    /**
     * Returns the total number of rows that are visible given
     * the current grid height.
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
        return rows;
    }

    @Override
    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);

        // Update the view row size.
        refreshView();
    }

    public int getLoaderDelay() {
        return loaderDelay;
    }

    public void setLoaderDelay(int loaderDelay) {
        this.loaderDelay = loaderDelay;
    }

    @Override
    public List<T> getSelectedRowModels(boolean visibleOnly) {
        if(visibleOnly) {
            return super.getSelectedRowModels(true);
        } else {
            return selectedModels;
        }
    }

    public int getIndexOffset() {
        return indexOffset;
    }

    /**
     * The amount of data that will buffer your start index and view size.
     * This can be useful in removing and loading delays. Defaults to 10.
     */
    public void setIndexOffset(int indexOffset) {
        this.indexOffset = Math.max(1, indexOffset);
    }
}
