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
package gwt.material.design.client.ui.table.cell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.base.constants.StyleName;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.DataView;
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
public abstract class Column<T, C> implements HasCell<T, C> {

    /**
     * The {@link Cell} responsible for rendering items in the column.
     */
    private final Cell<C> cell;
    private int index;
    private boolean dynamicWidth;

    /**
     * The {@link FieldUpdater} used for updating values in the column.
     */
    private FieldUpdater<T, C> fieldUpdater;

    private boolean defaultSortAscending = true;
    private boolean numeric = false;
    private boolean autoSort = false;
    private String name;
    private String width;
    private HideOn hideOn;
    private TextAlign textAlign;
    private FrozenProperties frozenProps;
    private Map<StyleName, String> styleProps;
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
        ValueUpdater<C> valueUpdater = (fieldUpdater == null) ? null : value -> {
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
    public Column<T, C> render(Context context, T object, SafeHtmlBuilder sb) {
        cell.render(context, getValue(object), sb);
        return this;
    }

    /**
     * Sets a string that identifies this column in a data query.
     *
     * @param name name of the column from the data store's perspective
     */
    public Column<T, C> name(String name) {
        this.name = name;
        return this;
    }

    /**
     * @return the database name of the column, or null if it's never been set
     */
    public final String name() { return this.name; }

    /**
     * Check if the default sort order of the column is ascending or descending.
     *
     * @return true if default sort is ascending, false if not
     */
    public final boolean defaultSortAscending() {
        return defaultSortAscending;
    }

    /**
     * Set whether or not the default sort order is ascending.
     * 
     * @param defaultSortAscending true to set the default order to ascending, false for descending.
     */
    public Column<T, C> defaultSortAscending(boolean defaultSortAscending) {
        this.defaultSortAscending = defaultSortAscending;
        return this;
    }

    /**
     * Is this column auto sorting when rendered.
     * @return true if this column is auto sorted.
     */
    public final boolean autoSort() {
        return autoSort;
    }

    /**
     * Make this column auto sort on rendering, if multiple columns are auto
     * sorting it will be based on the first one set to auto sort.
     */
    public Column<T, C> autoSort(boolean autoSort) {
        this.autoSort = autoSort;
        return this;
    }

    public final Comparator<? super RowComponent<T>> sortComparator() {
        return sortComparator;
    }

    public Column<T, C> sortComparator(Comparator<? super RowComponent<T>> sortComparator) {
        this.sortComparator = sortComparator;
        return this;
    }

    /**
     * Check if the column is sortable.
     *
     * @return true if sortable, false if not
     */
    public boolean isSortable() {
        return sortComparator() != null;
    }

    @Override
    public final FieldUpdater<T, C> getFieldUpdater() {
        return fieldUpdater;
    }

    public final FieldUpdater<T, C> fieldUpdater() {
        return fieldUpdater;
    }

    public Column<T, C> fieldUpdater(FieldUpdater<T, C> fieldUpdater) {
        this.fieldUpdater = fieldUpdater;
        return this;
    }

    public final boolean numeric() {
        return numeric;
    }

    public Column<T, C> numeric(boolean numeric) {
        this.numeric = numeric;
        return this;
    }

    public Column<T, C> hideOn(HideOn hideOn) {
        this.hideOn = hideOn;
        return this;
    }

    public final HideOn hideOn() {
        return hideOn;
    }

    public Column<T, C> textAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
        return this;
    }

    public final TextAlign textAlign() {
        return textAlign;
    }

    /**
     * Set a style property using its name as the key. Please ensure the style name and value
     * are appropriately configured or it may result in unexpected behavior.
     *
     * @param styleName the style name as seen here {@link Style#STYLE_Z_INDEX} for example.
     * @param value the string value required for the given style property.
     */
    public Column<T, C> styleProperty(StyleName styleName, String value) {
        if(styleProps == null) {
            styleProps = new HashMap<>();
        }
        styleProps.put(styleName, value);
        return this;
    }

    /**
     * Get a styles property.
     * @param styleName the styles name as represented in a {@link Style} class.
     * @return null if the style property is not set.
     */
    public String styleProperty(StyleName styleName) {
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
    public Column<T, C> styleProperties(Map<StyleName, String> styleProps) {
        this.styleProps = styleProps;
        return this;
    }

    public Column<T, C> frozenProperties(FrozenProperties frozenProps) {
        if(frozenProps != null) {
            // Width is a required property for frozen columns
            width(frozenProps.getStyleProperty(StyleName.WIDTH));
        }

        this.frozenProps = frozenProps;
        return this;
    }

    /**
     * The row cells that are made frozen will no longer follow the rules of the tables row.
     * This means that all the cells positioning of content needs to be manually set up.
     * <br><br>
     * Note that the frozen columns does not support {@link DataView#setUseStickyHeader(boolean)},
     * this will automatically be disabled if frozen columns are detected.
     * <br><br>
     * Like so:
     * <pre>{@code table.addColumn(new TextColumn<Person>() {
        @Override
        public FrozenProperties frozenProperties() {
            return new FrozenProperties("200px", "60px")
                .setStyleProperty(StyleName.PADDING_LEFT, "20px")
                .setStyleProperty(StyleName.PADDING_TOP, "10px")
                .setHeaderStyleProperty(StyleName.PADDING_TOP, "21px");
        }
        @Override
        public String getValue(Person object) {
            return object.getName();
        }
    }, "Name");
     * }</pre>
     *
     * Columns must be aligned with each other without any unfrozen columns in between.
     */
    public final FrozenProperties frozenProperties() { return frozenProps; }

    public final boolean isFrozenColumn() {
        return frozenProps != null;
    }

    /**
     * Set the columns header width.
     */
    public Column<T, C> width(String width) {
        this.width = width;
        this.dynamicWidth = width.contains("%");
        return this;
    }

    /**
     * Get the columns header width.
     * @return null if not defined.
     */
    public final String width() {
        return width;
    }

    public final void setIndex(int index) {
        this.index = index;
    }

    public final int getIndex() {
        return index;
    }

    /**
     * Is our width configuration a dynamic value (i.e. percentage %)
     * @return false by default but will update to true when % width is set
     */
    public boolean isDynamicWidth() {
        return dynamicWidth;
    }

    @Override
    public String toString() {
        return "Column{" +
                "cell=" + cell +
                ", index=" + index +
                ", fieldUpdater=" + fieldUpdater +
                ", defaultSortAscending=" + defaultSortAscending +
                ", numeric=" + numeric +
                ", autoSort=" + autoSort +
                ", name='" + name + '\'' +
                ", width='" + width + '\'' +
                ", hideOn=" + hideOn +
                ", textAlign=" + textAlign +
                ", frozenProps=" + frozenProps +
                ", styleProps=" + styleProps +
                ", sortComparator=" + sortComparator +
                '}';
    }
}
