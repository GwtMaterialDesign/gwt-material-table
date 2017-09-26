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
package gwt.material.design.client.ui.pager;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.SortContext;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;
import gwt.material.design.client.ui.table.MaterialDataTable;

import java.util.List;

/**
 * Material Data Pager - a simple pager for Material Data Table component
 *
 * @author kevzlou7979
 */
public class MaterialDataPager<T> extends MaterialDataPagerBase<T> implements HasPager {

    private MaterialDataTable<T> table;
    private DataSource<T> dataSource;
    private int offset = 0;
    private int limit = 0;
    private int currentPage = 1;
    private int totalRows = 0;
    private int[] limitOptions = new int[]{5, 10, 20};
    private HandlerRegistration nextRegistration;
    private HandlerRegistration prevRegistration;

    public MaterialDataPager(MaterialDataTable<T> table, DataSource<T> dataSource) {
        super();
        this.table = table;
        this.dataSource = dataSource;
    }

    public MaterialDataPager() {
        super();
    }

    /**
     * Initialize the data pager for navigation
     */
    @Override
    protected void initialize() {
        buildNumPagePanel();
        buildLimitOptionsPanel();
        buildActionPanel();

        limit = limitOptions[0];
        firstPage();
        if (prevRegistration != null)
            prevRegistration.removeHandler();
        if (nextRegistration != null)
            nextRegistration.removeHandler();
        prevRegistration = iconNext.addClickHandler(event -> next());
        nextRegistration = iconPrev.addClickHandler(event -> previous());

        listPages.addValueChangeHandler(event -> gotoPage(event.getValue()));

        // Build the limit options listbox
        listLimitOptions.clear();
        for (int limitOption : limitOptions) {
            listLimitOptions.addItem(limitOption);
        }
        listLimitOptions.addValueChangeHandler(valueChangeEvent -> {
            limit = valueChangeEvent.getValue();
            if ((totalRows / currentPage) < limit) {
                lastPage();
                return;
            }
            gotoPage(listPages.getValue());
        });
    }

    @Override
    public void next() {
        currentPage++;
        gotoPage(currentPage);
    }

    @Override
    public void previous() {
        currentPage--;
        gotoPage(currentPage);
    }

    @Override
    public void lastPage() {
        if (isExcess()) {
            gotoPage((totalRows / limit) + 1);
        }else {
            gotoPage(totalRows / limit);
        }

        listPages.setSelectedIndex(currentPage - 1);
    }

    @Override
    public void firstPage() {
        gotoPage(1);
    }

    @Override
    public void gotoPage(int page) {
        this.currentPage = page;
        doLoad((page * limit) - limit, limit);
    }

    @Override
    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean isNext() {
        return offset + limit < totalRows;
    }

    @Override
    public boolean isPrevious() {
        return offset - limit >= 0;
    }

    /**
     * Set the limit as an array of options to be populated inside the rows per page listbox
     */
    public void setLimitOptions(int... limitOptions) {
        this.limitOptions = limitOptions;
    }

    /**
     * Check whether there are excess rows to be rendered with given limit
     */
    public boolean isExcess() {
        return totalRows % limit > 0;
    }

    /**
     * Check whether the pager is on the last currentPage.
     */
    public boolean isLastPage() {
        return currentPage == (totalRows / limit) + 1;
    }

    /**
     * Load the datasource within a given offset and limit
     */
    protected void doLoad(int offset, int limit) {
        this.offset = offset;

        // Check whether the pager has excess rows with given limit
        if (isLastPage() & isExcess()) {
            // Get the difference between total rows and excess rows
            limit = totalRows - offset;
        }

        int finalLimit = limit;
        dataSource.load(new LoadConfig<>(offset, limit, table.getSortContext(), table.getOpenCategories()), new LoadCallback<T>() {
            @Override
            public void onSuccess(LoadResult<T> loadResult) {
                totalRows = loadResult.getTotalLength();
                table.setVisibleRange(offset, finalLimit);
                table.loaded(loadResult.getOffset(), loadResult.getData());
                updateUi();
            }

            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Load failure", caught);
                //TODO: What we need to do on failure? May be clear table?
            }
        });
    }

    /**
     * Set and update the ui fields of the pager after the datasource load callback
     */
    protected void updateUi() {

        // Action label (current selection)
        int lastRow = (isExcess() & isLastPage()) ? totalRows : (offset + limit);
        actionLabel.setText((offset + 1) + "-" + lastRow + " of " + totalRows);

        // Build the currentPage number listbox
        listPages.clear();
        int pages = (isExcess()) ? (totalRows / limit) + 1 : totalRows / limit;
        for (int i = 1; i <= pages; i++) {
            listPages.addItem(i);
        }
        listPages.setSelectedIndex(currentPage - 1);

        iconNext.setEnabled(true);
        iconPrev.setEnabled(true);

        if (!isNext()) {
            iconNext.setEnabled(false);
        }

        if (!isPrevious()) {
            iconPrev.setEnabled(false);
        }
    }

    public MaterialDataTable<T> getTable() {
        return table;
    }

    public void setTable(MaterialDataTable<T> table) {
        this.table = table;
    }

    public DataSource<T> getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource<T> dataSource) {
        this.dataSource = dataSource;
    }

    public int getOffset() {
        return offset;
    }

    public int getLimit() {
        return limit;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int[] getLimitOptions() {
        return limitOptions;
    }
}
