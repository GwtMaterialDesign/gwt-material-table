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
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.base.HasHideOn;
import gwt.material.design.client.base.HasTextAlign;
import gwt.material.design.client.base.constants.StyleName;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.component.RowComponent;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
    private String name;
    private String width;
    private HideOn hideOn;
    private TextAlign textAlign;

    private Map<StyleName, String> styleProps;

    private Comparator<? super RowComponent<T>> sortComparator;

    /**
     * Construct a new Column with a given {@link Cell}.
     * 
     * @param cell the Cell used by this Column
     */
    public Column(Cell<C> cell) {
        this.cell = cell;

        setFieldUpdater(fieldUpdater());
        setDefaultSortAscending(defaultSortAscending());
        setNumeric(numeric());
        setName(name());
        setWidth(width());
        setHideOn(hideOn());
        setTextAlign(textAlign());
        setStyleProperties(styleProperties());
        setSortComparator(sortComparator());
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
    public final Cell<C> getCell() {
        return cell;
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
    public final void setName(String name) {
        this.name = name;
    }

    /**
     * @return the database name of the column, or null if it's never been set
     */
    public final String getName() {
        return name;
    }

    /**
     * @return the database name of the column, or null if it's never been set
     */
    public String name() { return ""; }

    /**
     * Check if the default sort order of the column is ascending or descending.
     *
     * @return true if default sort is ascending, false if not
     */
    public final boolean isDefaultSortAscending() {
        return isDefaultSortAscending;
    }

    /**
     * Set whether or not the default sort order is ascending.
     * 
     * @param isAscending true to set the default order to ascending, false for
     *                    descending
     */
    public final void setDefaultSortAscending(boolean isAscending) {
        this.isDefaultSortAscending = isAscending;
    }

    /**
     * Check if the default sort order of the column is ascending or descending.
     *
     * @return true if default sort is ascending, false if not
     */
    public boolean defaultSortAscending() { return true; }

    /**
     * Check if the column is sortable.
     *
     * @return true if sortable, false if not
     */
    public boolean isSortable() {
        return getSortComparator() != null;
    }

    public final void setSortComparator(Comparator<? super RowComponent<T>> sortComparator) {
        this.sortComparator = sortComparator;
    }

    public final Comparator<? super RowComponent<T>> getSortComparator() {
        return sortComparator;
    }

    public Comparator<? super RowComponent<T>> sortComparator() { return null; }

    @Override
    public final FieldUpdater<T, C> getFieldUpdater() {
        return fieldUpdater;
    }

    public final void setFieldUpdater(FieldUpdater<T, C> fieldUpdater) {
        this.fieldUpdater = fieldUpdater;
    }

    public FieldUpdater<T, C> fieldUpdater() { return null; }

    public final boolean isNumeric() {
        return isNumeric;
    }

    public final void setNumeric(boolean numeric) {
        isNumeric = numeric;
    }

    public boolean numeric() { return false; }

    @Override
    public final void setHideOn(HideOn hideOn) {
        this.hideOn = hideOn;
    }

    @Override
    public final HideOn getHideOn() {
        return hideOn;
    }

    public HideOn hideOn() { return null; }

    @Override
    public final void setTextAlign(TextAlign align) {
        this.textAlign = align;
    }

    @Override
    public final TextAlign getTextAlign() {
        return textAlign;
    }

    public TextAlign textAlign() { return null; }

    /**
     * Set a style property using its name as the key. Please ensure the style name and value
     * are appropriately configured or it may result in unexpected behavior.
     *
     * @param styleName the style name as seen here {@link Style#STYLE_Z_INDEX} for example.
     * @param value the string value required for the given style property.
     */
    public final void setStyleProperty(StyleName styleName, String value) {
        if(styleProps == null) {
            styleProps = new HashMap<>();
        }
        styleProps.put(styleName, value);
    }

    /**
     * Get a styles property.
     * @param styleName the styles name as represented in a {@link Style} class.
     * @return null if the style property is not set.
     */
    public final String getStyleProperty(StyleName styleName) {
        return styleProps!=null ? styleProps.get(styleName) : null;
    }

    /**
     * Return the registered style properties.
     * @return null if no styles are added.
     */
    public final Map<StyleName, String> getStyleProperties() {
        return styleProps;
    }

    /**
     * Set the style properties map.
     */
    public void setStyleProperties(Map<StyleName, String> styleProps) {
        this.styleProps = styleProps;
    }

    public Map<StyleName, String> styleProperties() { return null;}

    /**
     * Set the columns header width.
     */
    public final void setWidth(String width) {
        this.width = width;
    }

    /**
     * Get the columns header width.
     * @return null if not defined.
     */
    public final String getWidth() {
        return width;
    }

    public String width() { return null; }

    @Override
    public String toString() {
        return "Column{" +
            "cell=" + cell +
            ", fieldUpdater=" + fieldUpdater +
            ", isDefaultSortAscending=" + isDefaultSortAscending +
            ", isNumeric=" + isNumeric +
            ", name='" + name + '\'' +
            ", width='" + width + '\'' +
            ", hideOn=" + hideOn +
            ", textAlign=" + textAlign +
            ", styleProps=" + styleProps +
            ", sortComparator=" + sortComparator +
            '}';
    }
}
