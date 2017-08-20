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
package gwt.material.design.client.data;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.factory.RowComponentFactory;

import java.util.List;

public interface HasRows<T> extends HasHandlers {

    /**
     * Get the range of visible rows.
     *
     * @return the visible range
     *
     * @see #setVisibleRange(Range)
     * @see #setVisibleRange(int, int)
     */
    Range getVisibleRange();

    /**
     * Set the visible range or rows. This method defers to
     * {@link #setVisibleRange(Range)}.
     *
     * @param start the start index
     * @param length the length
     *
     * @see #getVisibleRange()
     */
    void setVisibleRange(int start, int length);

    /**
     * Set the visible range or rows.
     *
     * @param range the visible range
     *
     * @see #getVisibleRange()
     */
    void setVisibleRange(Range range);

    /**
     * Get the exact count of all rows.
     *
     * @return the exact row count
     */
    int getRowCount();

    /**
     * Get the configured total rows count.
     */
    int getTotalRows();

    /**
     * Set the total row count.
     */
    void setTotalRows(int totalRows);

    /**
     * Set values associated with the rows in the visible range.
     *
     * @param start the start index of the data
     * @param values the values within the range
     */
    void setRowData(int start, List<? extends T> values);

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
     * Update a models row within the table.
     * @param model a model with a valid <code>equals</code> method.
     */
    void updateRow(T model);

    /**
     * Get a row by its representing model.
     * @param model the model assigned to a row.
     */
    RowComponent<T> getRow(T model);

    /**
     * Get a row by its rendered index.
     * @param index the value of the render index.
     */
    RowComponent<T> getRow(int index);

    /**
     * Get a models {@link RowComponent} or null if not found.
     * @param model a model with a valid <code>equals</code> method.
     * @return the models representing {@link RowComponent}.
     */
    RowComponent<T> getRowByModel(T model);

    /**
     * Clear data rows.
     *
     * @param clearData should we also clear the stored data.
     */
    void clearRows(boolean clearData);

    /**
     * Set your own custom {@link RowComponentFactory} to generate your row components.
     */
    void setRowFactory(RowComponentFactory<T> rowFactory);

    /**
     * Get the views row factory.
     */
    RowComponentFactory<T> getRowFactory();

    /**
     * Set the data views row selection type.
     */
    void setSelectionType(SelectionType selectionType);

    /**
     * Get the data views row selection type.
     */
    SelectionType getSelectionType();

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
    void deselectRow(Element row, boolean fireEvent);

    /**
     * Does this view have unselected rows.
     *
     * @param visibleOnly should we restrict this check to visible rows only.
     * @return true if there are unselected rows in this view.
     */
    boolean hasDeselectedRows(boolean visibleOnly);

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
}
