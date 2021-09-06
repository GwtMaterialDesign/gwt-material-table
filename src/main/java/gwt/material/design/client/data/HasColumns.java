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

import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.ColumnFormatProvider;
import gwt.material.design.client.ui.table.cell.ColumnValueProvider;

import java.util.List;

public interface HasColumns<T> {

    /**
     * Add a new column to the data view.
     *
     * @param column the column object
     */
    <X extends Column<T, ?>> X addColumn(X column);

    /**
     * Add a new column to the data view.
     *
     * @param column the column object
     */
    <X extends Column<T, ?>> X addColumn(String header, X column);

    /**
     * Inserts a column into the table at the specified index.
     *
     * @param beforeIndex the index to insert the column
     * @param col         the column to be added
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    <X extends Column<T, ?>> X insertColumn(String header, int beforeIndex, X col);

    /**
     * Add a new column to the data view.
     */
    Column<T, ?> addColumn(ColumnValueProvider<T> provider, String columnName);

    /**
     * Remove an existing column by index.
     *
     * @param colIndex the columns placement index
     */
    void removeColumn(int colIndex);

    /**
     * Remove all columns.
     */
    void removeColumns();

    /**
     * Get the current defined columns.
     */
    List<Column<T, ?>> getColumns();

    /**
     * Get the offset column index (used when selection mode is on).
     */
    int getColumnOffset();

    /**
     * Sort a column matching the given index (the index excludes the selection box row).
     *
     * @param columnIndex valid index to the user added {@link Column}s.
     */
    void sort(int columnIndex);

    /**
     * Sort a column matching the given index (the index excludes the selection box row).
     *
     * @param columnIndex valid index to the user added {@link Column}s.
     * @param dir         the sort direction or null for auto reversing.
     */
    void sort(int columnIndex, SortDir dir);

    /**
     * Sort a column.
     *
     * @param column matching column that was added via {@link ##addColumn(Column)}.
     */
    void sort(Column<T, ?> column);

    /**
     * Sort a column.
     *
     * @param column matching column that was added via {@link ##addColumn(Column)}.
     * @param dir    the sort direction or null for auto reversing.
     */
    void sort(Column<T, ?> column, SortDir dir);

    /**
     * Get the number of left frozen columns.
     */
    int getLeftFrozenColumns();

    /**
     * Get the number of right frozen columns.
     */
    int getRightFrozenColumns();

    ColumnFormatProvider getFormatProvider();

    /**
     * Set the datable default column format provider on this instance.
     */
    void setDefaultFormatProvider(ColumnFormatProvider defaultFormatProvider);

    String getBlankPlaceholder();

    /**
     * Set the datable default column blank placeholder provider on this instance.
     */
    void setDefaultBlankPlaceholder(String defaultBlankPlaceholder);
}
