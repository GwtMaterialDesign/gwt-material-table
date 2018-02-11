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
import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;
import gwt.material.design.client.ui.table.MaterialDataTable;

/**
 * Material Data Pager - a simple pager for Material Data Table component
 *
 * @author kevzlou7979
 */
public class MaterialDataPager<T> extends MaterialWidget implements HasPager {

    protected MaterialDataTable<T> table;
    protected DataSource<T> dataSource;

    protected int offset = 0;
    protected int limit = 0;
    protected int currentPage = 1;
    protected int totalRows = 0;
    protected int[] limitOptions = new int[]{5, 10, 20};

    private PageActionsPanel actionsPanel = new PageActionsPanel(this);
    private PageNumberSelection pageNumberSelection = new PageNumberSelection(this);
    private PageRowSelection pageRowSelection = new PageRowSelection(this);

    public MaterialDataPager() {
        super(Document.get().createDivElement(), TableCssName.DATA_PAGER, TableCssName.ROW);
    }

    public MaterialDataPager(MaterialDataTable<T> table, DataSource<T> dataSource) {
        this();
        this.table = table;
        this.dataSource = dataSource;
    }

    /**
     * Initialize the data pager for navigation
     */
    @Override
    protected void onLoad() {
        super.onLoad();

        load();
    }

    protected void load() {
        if (limit == 0) {
            limit = limitOptions[0];
        }
        add(actionsPanel);
        add(pageRowSelection);
        add(pageNumberSelection);

        firstPage();
    }

    public void updateRowsPerPage(int limit) {
        if ((totalRows / currentPage) < limit) {
            lastPage();
            return;
        }
        gotoPage(pageNumberSelection.getValue());
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
    	gotoPage(totalPages());

        pageNumberSelection.updatePageNumber(currentPage - 1);
    }

    private int totalPages() {
    	if (isExcess()) {
            return ((totalRows / limit) + 1);
        }else {
            return totalRows / limit;
        }
    }

    @Override
    public void firstPage() {
        gotoPage(1);
    }

    @Override
    public void gotoPage(int page) {
    	this.currentPage = adjustToPageLimits(page);
        doLoad((currentPage * limit) - limit, limit);
    }
    /** Adjusts the input page number to the available page range  */
    private int adjustToPageLimits(int page) {
	    if(page <= 1) {
    		return 1;
    	} else if (page > totalPages()) {
    		return totalPages();
    	}

		return page;
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
        dataSource.load(new LoadConfig<>(offset, limit, table.getView().getSortContext(),
                table.getView().getOpenCategories()), new LoadCallback<T>() {
            @Override
            public void onSuccess(LoadResult<T> loadResult) {
                setOffset(loadResult.getOffset());
                totalRows = loadResult.getTotalLength();
                table.setVisibleRange(loadResult.getOffset(), loadResult.getData().size());
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
        pageNumberSelection.updatePageNumber(currentPage);

        // Action label (current selection) in either the form "x-y of z" or "y of z" (when page has only 1 record)
        int firstRow = offset + 1;
        int lastRow = (isExcess() & isLastPage()) ? totalRows : (offset + limit);
        actionsPanel.getActionLabel().setText((firstRow == lastRow ? lastRow : firstRow + "-" + lastRow) + " of " + totalRows);

        actionsPanel.getIconNext().setEnabled(true);
        actionsPanel.getIconPrev().setEnabled(true);

        if (!isNext()) {
            actionsPanel.getIconNext().setEnabled(false);
        }

        if (!isPrevious()) {
            actionsPanel.getIconPrev().setEnabled(false);
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

    public void setOffset(int offset) {
        this.offset = offset;
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

    public PageActionsPanel getActionsPanel() {
        return actionsPanel;
    }

    public void setActionsPanel(PageActionsPanel actionsPanel) {
        this.actionsPanel = actionsPanel;
    }

    public PageNumberSelection getPageNumberSelection() {
        return pageNumberSelection;
    }

    public void setPageNumberSelection(PageNumberSelection pageNumberSelection) {
        this.pageNumberSelection = pageNumberSelection;
    }

    public PageRowSelection getPageRowSelection() {
        return pageRowSelection;
    }

    public void setPageRowSelection(PageRowSelection pageRowSelection) {
        this.pageRowSelection = pageRowSelection;
    }
}