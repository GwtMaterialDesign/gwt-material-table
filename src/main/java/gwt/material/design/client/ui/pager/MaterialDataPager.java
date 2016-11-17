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
public class MaterialDataPager<T> extends MaterialDataPagerBase<T> {

    private int[] rowCountOptions = new int[] {5, 10, 15};
    private int numPages, firstRow, lastRow, totalRows;

    private boolean useRowCountOptions = true;

    public MaterialDataPager(MaterialDataTable<T> table, DataSource<T> dataSource) {
        super(table, dataSource);
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if(!useRowCountOptions) {
            getRowsPerPagePanel().setVisible(false);
            getNumPagePanel().setOffset("l6");
        } else {
            setRowCount(getRowCountOptions()[0]);
            getRowsPerPagePanel().setVisible(true);
        }

        if (getDataSource().useRemoteSort()) {
            table.addSortColumnHandler((e, sortContext, columnIndex) -> {
                this.refresh();
                return true;
            });
        }

        getListRowsPerPage().clear();
        for(int i : rowCountOptions) {
            getListRowsPerPage().addItem(String.valueOf(i));
        }

        getListRowsPerPage().addValueChangeHandler(valueChangeEvent -> {
            goToPage(Integer.parseInt(valueChangeEvent.getValue()), getCurrentPage());
        });

        getListPages().addValueChangeHandler(valueChangeEvent -> {
            goToPage(getRowCount(), Integer.parseInt(valueChangeEvent.getValue()));
        });

        getIconNext().addClickHandler(clickEvent -> next());
        getIconPrev().addClickHandler(clickEvent -> previous());
        goToPage(getRowCount(), 1);
    }

    /**
     * Update the pager properties.
     */
    private void goToPage(int rowCount, int currentPage) {

        // Check if the row's num page is less than the current page selection
        // If yes, then we will navigate to last page
        if(getNumPages() < currentPage) {
            lastPage();
            return;
        }

        // Check if data pager is on first page, if yes then the previous button will be disabled
        if(currentPage >= getNumPages()) {
            getIconNext().setEnabled(false);
            getIconPrev().setEnabled(true);
        } else {
            getIconNext().setEnabled(true);
        }

        // Check if data pager is on last page, if yes then the next button will be disabled.
        if(currentPage <= 1) {
            getIconNext().setEnabled(true);
            getIconPrev().setEnabled(false);
        } else {
            getIconPrev().setEnabled(true);
        }

        // Check if data pager has only one page, if yes then the next and previous button will be disabled.
        if(getNumPages() == 1) {
            getIconNext().setEnabled(false);
            getIconPrev().setEnabled(false);
        }
        setRowCount(rowCount);
        setFirstRow(((Math.max(0, currentPage - 1)) * rowCount) + 1);
        setLastRow(currentPage * rowCount);
        updateDataTable();
        setCurrentPage(currentPage);
    }

    public void refresh() {
        updateDataTable();
    }

    /**
     * Refresh and redraw the table and set the visible range with the given params.
     */
    private void updateDataTable() {
        getListPages().clear();
        for(int i = 1; i <= getNumPages(); i++) {
            getListPages().addItem(String.valueOf(i));
        }
        getActionLabel().setText(getFirstRow() + "-" + getLastRow() + " of " + getTotalRows());
        table.setVisibleRange(getFirstRow(), getRowCount());

        // Check if the first row and row count  is bigger than the getTotalRows
        if(getFirstRow() + getRowCount() > getTotalRows()) {
            if(getRowCount() > getTotalRows()) {
                doLoad(getFirstRow(), getTotalRows());
            } else {
                doLoad(getFirstRow(), getTotalRows() - (getFirstRow() - 1));
            }
        } else {
            doLoad(getFirstRow(), getRowCount());
        }
    }

    private void doLoad(int offset, int limit){
        getDataSource().load(new LoadConfig<T>() {
            @Override
            public int getOffset() {
                return offset;
            }
            @Override
            public int getLimit() {
                return limit;
            }
            @Override
            public SortContext<T> getSortContext() {
                return table.getSortContext();
            }
            @Override
            public List<CategoryComponent> getOpenCategories() {
                return table.getOpenCategories();
            }
        }, new LoadCallback<T>() {
            @Override
            public void onSuccess(LoadResult<T> loadResult) {
                setTotalRows(loadResult.getTotalLength());
                table.loaded(loadResult.getOffset(), loadResult.getData());
            }
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Load failure", caught);
                //TODO: What we need to do on failure? May be clear table?
            }
        });
    }

    /**
     * Get the row count options.
     * @return an array of integers for the series of options (How many rows should be displayed)
     */
    public int[] getRowCountOptions() {
        return rowCountOptions;
    }

    /**
     * Set how many rows should be displayed into the data table,
     * a series of options (e.g 5, 10, 15) which will be added to listbox options
     */
    public void setRowCountOptions(int... rowCountOptions) {
        this.rowCountOptions = rowCountOptions;
    }

    /**
     * Get the specific selected row count list box option.
     * @return the row count
     */
    public int getRowCount() {
        if(getListRowsPerPage().getSelectedItemText() != null && isUseRowCountOptions()) {
            return Integer.parseInt(getListRowsPerPage().getSelectedItemText());
        }
        return table.getVisibleRange().getLength();
    }

    /**
     * Set directly the selected row count
     */
    public void setRowCount(int rowCount) {
        getListRowsPerPage().setSelectedValue(String.valueOf(rowCount));
    }

    /**
     * Get the current page.
     * @return currentPage
     */
    public int getCurrentPage() {
        if(getListPages().getSelectedItemText() != null) {
            return Integer.parseInt(getListPages().getSelectedItemText());
        }
        return 0;
    }

    /**
     * Set the current page.
     */
    public void setCurrentPage(int currentPage) {
        getListPages().setSelectedValue(String.valueOf(currentPage));
    }

    /**
     * Get the total number of pages.
     */
    public int getNumPages() {
        if(getTotalRows() % getRowCount() > 0) {
            return (totalRows / getRowCount()) + 1;
        }
        return totalRows / getRowCount();
    }

    /**
     * Advance the starting row by 'pageSize' rows.
     */
    public void next() {
        goToPage(getRowCount(), getCurrentPage() + 1);
    }

    /**
     * Move the starting row back by 'pageSize' rows.
     */
    public void previous() {
        goToPage(getRowCount(), getCurrentPage() - 1);
    }

    /**
     * Go to the first page.
     */
    public void firstPage() {
        goToPage(getRowCount(), 1);
    }

    /**
     * Go to the last page.
     */
    public void lastPage() {
        goToPage(getRowCount(), getNumPages());
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getLastRow() {
        if(lastRow > getTotalRows()) {
            lastRow = getTotalRows();
        }
        return lastRow;
    }

    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public boolean isUseRowCountOptions() {
        return useRowCountOptions;
    }

    public void setUseRowCountOptions(boolean useRowCountOptions) {
        this.useRowCountOptions = useRowCountOptions;
    }
}