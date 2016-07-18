package gwt.material.design.client.ui.table.cell;

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


import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.base.HasHideOn;
import gwt.material.design.client.base.HasTextAlign;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.component.RowComponent;

import java.util.Comparator;

/**
 * A representation of a column in a table.
 * 
 * @param <T> the row type
 * @param <C> the column type
 *
 * @author Ben Dol
 */
public abstract class Column<T, C> implements HasCell<T, C>, HasHideOn, HasTextAlign {

    /**
     * The {@link Cell} responsible for rendering items in the column.
     */
    private final Cell<C> cell;

    /**
     * The {@link FieldUpdater} used for updating values in the column.
     */
    private FieldUpdater<T, C> fieldUpdater;

    private boolean isDefaultSortAscending = true;
    private boolean isNumeric = false;
    private String name = null;
    private HideOn hideOn;
    private TextAlign textAlign;

    private Comparator<? super RowComponent<T>> sortComparator;

    /**
     * Construct a new Column with a given {@link Cell}.
     * 
     * @param cell the Cell used by this Column
     */
    public Column(Cell<C> cell) {
        this.cell = cell;
    }

    /**
     * Handle a browser event that took place within the column.
     *
     * @param context the cell context
     * @param elem the parent Element
     * @param object the base object to be updated
     * @param event the native browser event
     */
    public void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event) {
        final int index = context.getIndex();
        ValueUpdater<C> valueUpdater = (fieldUpdater == null) ? null : (ValueUpdater<C>) value -> {
            fieldUpdater.update(index, object, value);
        };
        cell.onBrowserEvent(context, elem, getValue(object), event, valueUpdater);
    }

    /**
     * Returns the {@link Cell} responsible for rendering items in the column.
     * 
     * @return a Cell
     */
    @Override
    public Cell<C> getCell() {
        return cell;
    }

    /**
     * @return the database name of the column, or null if it's never been set
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the column value from within the underlying data object.
     */
    @Override
    public abstract C getValue(T object);

    /**
     * Render the object into the cell.
     * 
     * @param object the object to render
     * @param sb the buffer to render into
     */
    public void render(Context context, T object, SafeHtmlBuilder sb) {
        cell.render(context, getValue(object), sb);
    }

    /**
     * Sets a string that identifies this column in a data query.
     * 
     * @param name name of the column from the data store's perspective
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if the default sort order of the column is ascending or descending.
     *
     * @return true if default sort is ascending, false if not
     */
    public boolean isDefaultSortAscending() {
        return isDefaultSortAscending;
    }

    /**
     * Set whether or not the default sort order is ascending.
     * 
     * @param isAscending true to set the default order to ascending, false for
     *                    descending
     */
    public void setDefaultSortAscending(boolean isAscending) {
        this.isDefaultSortAscending = isAscending;
    }

    /**
     * Check if the column is sortable.
     *
     * @return true if sortable, false if not
     */
    public boolean isSortable() {
        return getSortComparator() != null;
    }

    public void setSortComparator(Comparator<? super RowComponent<T>> sortComparator) {
        this.sortComparator = sortComparator;
    }

    public Comparator<? super RowComponent<T>> getSortComparator() {
        return sortComparator;
    }

    @Override
    public FieldUpdater<T, C> getFieldUpdater() {
        return fieldUpdater;
    }

    public void setFieldUpdater(FieldUpdater<T, C> fieldUpdater) {
        this.fieldUpdater = fieldUpdater;
    }

    public boolean isNumeric() {
        return isNumeric;
    }

    public void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    @Override
    public void setHideOn(HideOn hideOn) {
        this.hideOn = hideOn;
    }

    @Override
    public HideOn getHideOn() {
        return hideOn;
    }

    @Override
    public void setTextAlign(TextAlign align) {
        this.textAlign = align;
    }

    @Override
    public TextAlign getTextAlign() {
        return textAlign;
    }

    @Override
    public String toString() {
        return "Column{" +
          "cell=" + cell +
          ", isDefaultSortAscending=" + isDefaultSortAscending +
          ", isSortable=" + isSortable() +
          ", name='" + name + '\'' +
          '}';
    }
}
