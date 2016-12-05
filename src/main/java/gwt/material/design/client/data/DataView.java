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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.HasKeyProvider;
import com.google.gwt.view.client.HasRows;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.factory.CategoryComponentFactory;
import gwt.material.design.client.data.factory.RowComponentFactory;
import gwt.material.design.client.js.JsTableSubHeaders;
import gwt.material.design.client.ui.MaterialProgress;
import gwt.material.design.client.ui.table.TableScaffolding;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.List;

/**
 * A data view is in charge of rendering the data within the data table.
 *
 * @author Ben Dol
 */
public interface DataView<T> extends HasRows, HasKeyProvider<T> {

    /**
     * Render the data view components to DOM.
     */
    void render(Components<Component<?>> components);

    /**
     * Setup the data view.
     */
    void setup(TableScaffolding scaffolding) throws Exception;

    /**
     * Destroy the data view.
     */
    void destroy();

    /**
     * Data views loader has loaded data for use.
     */
    void loaded(int startIndex, List<T> data);

    /**
     * Invoke a refresh on the dataview.
     */
    void refreshView();

    /**
     * Set a {@link DataSource} for data loading.
     */
    void setDataSource(DataSource<T> dataSource);

    /**
     * Get the configured data source.
     */
    DataSource<T> getDataSource();

    /**
     * Set your own custom data view renderer.
     * See {@link BaseRenderer} for the standard rendering.
     */
    void setRenderer(Renderer<T> renderer);

    /**
     * Get the maximum row height.
     */
    int getRowHeight();

    /**
     * Set maximum row height. The minimum row height is
     * 33 pixels as a material design specification.
     */
    void setRowHeight(int rowHeight);

    /**
     * Get the displays main container.
     */
    Panel getContainer();

    /**
     * Get the data views id.
     */
    String getViewId();

    /**
     * Set the data views display view.
     */
    void setDisplay(DataView<T> display);

    /**
     * Check if a header with the given index is visible.
     */
    boolean isHeaderVisible(int colIndex);

    /**
     * Add a new column to the data view.
     *
     * @param column the column object
     */
    void addColumn(Column<T, ?> column);

    /**
     * Add a new column to the data view.
     *
     * @param column the column object
     */
    void addColumn(Column<T, ?> column, String header);

    /**
     * Inserts a column into the table at the specified index.
     *
     * @param beforeIndex the index to insert the column
     * @param col the column to be added
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void insertColumn(int beforeIndex, Column<T, ?> col, String header);

    /**
     * Remove an existing column by index.
     * @param colIndex the columns placement index
     */
    void removeColumn(int colIndex);

    /**
     * Get the current defined columns.
     */
    List<Column<T, ?>> getColumns();

    /**
     * Get the offset column index (used when selection mode is on).
     */
    int getColumnOffset();

    /**
     * Set the data views row selection type.
     */
    void setSelectionType(SelectionType selectionType);

    /**
     * Get the data views row selection type.
     */
    SelectionType getSelectionType();

    /**
     * Enable the use of sticky table header bar.
     */
    void setUseStickyHeader(boolean stickyHeader);

    /**
     * True if we are using sticky table header.
     */
    boolean isUseStickyHeader();

    /**
     * Get the subheader library API.
     */
    JsTableSubHeaders getSubheaderLib();

    /**
     * Select or deselect all rows.
     * @param select true will select, false with deselect
     */
    void selectAllRows(boolean select);

    /**
     * Select or deselect all rows.
     *
     * @param select true will select, false with deselect
     * @param fireEvent fire the '{@value gwt.material.design.client.ui.table.TableEvents#SELECT_ALL}' event.
     */
    void selectAllRows(boolean select, boolean fireEvent);

    /**
     * Select a row by given element.
     *
     * @param row element of the row selection
     */
    void toggleRowSelect(Element row);

    /**
     * Select a row by given element.
     *
     * @param row element of the row selection
     * @param fireEvent fire the '{@value gwt.material.design.client.ui.table.TableEvents#ROW_SELECT}' event.
     */
    void toggleRowSelect(Element row, boolean fireEvent);

    /**
     * Select a given row.
     */
    void selectRow(Element row, boolean fireEvent);

    /**
     * Unselect a selected row.
     */
    void unselectRow(Element row, boolean fireEvent);

    /**
     * Does this view have unselected rows.
     *
     * @param visibleOnly should we restrict this check to visible rows only.
     * @return true if there are unselected rows in this view.
     */
    boolean hasUnselectedRows(boolean visibleOnly);

    /**
     * Does this view have selected rows.
     *
     * @param visibleOnly should we restrict this check to visible rows only.
     * @return true if there are selected rows in this view.
     */
    boolean hasSelectedRows(boolean visibleOnly);

    /**
     * Get all the selected row models.
     * @param visibleOnly should we restrict this check to visible rows only.
     * @return list of the currently selected row models.
     */
    List<T> getSelectedRowModels(boolean visibleOnly);

    /**
     * Is the data view setup.
     */
    boolean isSetup();

    /**
     * Set values associated with the rows in the visible range.
     *
     * @param start the start index of the data
     * @param values the values within the range
     */
    void setRowData(int start, List<? extends T> values);

    /**
     * Get the visible item count of the data view.
     */
    int getVisibleItemCount();

    /**
     * Get all registered category components.
     */
    List<CategoryComponent> getCategories();

    /**
     * Get all the open {@link CategoryComponent}'s.
     */
    List<CategoryComponent> getOpenCategories();

    /**
     * Check if a category has data to provide.
     */
    boolean isCategoryEmpty(CategoryComponent category);

    /**
     * Explicitly add a {@link CategoryComponent}, which will be drawn to the table.
     * If the category already exists then it will be ignored.
     * @param category The category data defined.
     */
    void addCategory(CategoryComponent category);

    /**
     * Has this data view got an existing {@link CategoryComponent} with given name.
     */
    boolean hasCategory(String categoryName);

    /**
     * Disable a data view category.
     */
    void disableCategory(String categoryName);

    /**
     * Get the data views current sort context, or null if no sort context is applied.
     */
    SortContext<T> getSortContext();

    /**
     * Is redrawing the data view.
     */
    boolean isRedraw();

    /**
     * Set redraw data view elements.
     */
    void setRedraw(boolean redraw);

    /**
     * Set your own custom {@link RowComponentFactory} to generate your row components.
     */
    void setRowFactory(RowComponentFactory<T> rowFactory);

    /**
     * Set your own custom {@link CategoryComponentFactory} to generate your categories.
     */
    void setCategoryFactory(CategoryComponentFactory categoryFactory);

    /**
     * Set the data views loading mask.
     */
    void setLoadMask(boolean loadMask);

    /**
     * Is the data views loading mask applied.
     */
    boolean isLoadMask();

    /**
     * Get the data views progress widget.
     */
    MaterialProgress getProgressWidget();

    /**
     * Get the configured total rows count.
     */
    int getTotalRows();

    /**
     * Set the total row count.
     */
    void setTotalRows(int totalRows);

    /**
     * Is the data view using categories where possible.
     */
    boolean isUseCategories();

    /**
     * Allow the data view to generate categories,
     * or use added categories for row data.
     */
    void setUseCategories(boolean useCategories);

    /**
     * Is the data view using loading overlay mask.
     */
    boolean isUseLoadOverlay();

    /**
     * Use the loading overlay mask.
     */
    void setUseLoadOverlay(boolean useLoadOverlay);

    /**
     * Is the data view using row expansion.
     */
    boolean isUseRowExpansion();

    /**
     * Use row expansion functionality, giving the user
     * a way to expand rows for extra information.
     */
    void setUseRowExpansion(boolean useRowExpansion);

    /**
     * Get the long press duration requirement.
     */
    int getLongPressDuration();

    /**
     * Set the long press duration requirement.
     */
    void setLongPressDuration(int longPressDuration);
}
