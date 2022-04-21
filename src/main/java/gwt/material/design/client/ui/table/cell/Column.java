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
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.base.constants.StyleName;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.table.MaterialDataTable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * A representation of a column in a table.
 *
 * @param <T> the row type
 * @param <C> the column type
 * @author Ben Dol
 */
public abstract class Column<T, C> implements HasCell<T, C> {

    public interface Value<T, C> {
        C value(T obj);
    }

    /**
     * The {@link Cell} responsible for rendering items in the column.
     */
    protected final Cell<C> cell;
    private int index;
    private int widthPixels;
    private boolean dynamicWidth;

    /**
     * The {@link FieldUpdater} used for updating values in the column.
     */
    private FieldUpdater<T, C> fieldUpdater;

    private boolean defaultSortAscending = true;
    private boolean numeric;
    private boolean autoSort;
    private boolean sortable;
    private boolean useRemoteSort;
    private boolean widthToPercent;
    private boolean hidden;
    private boolean hideable = true;
    private Boolean helpEnabled;
    private String name;
    private String help;
    private String width;
    private HideOn hideOn;
    private TextAlign textAlign;
    private FrozenProperties frozenProps;
    private Map<StyleName, String> styleProps;
    private Comparator<? super RowComponent<T>> sortComparator;
    private DataView<T> dataView;
    private FooterColumn<T> footer;

    protected C defaultValue;
    protected String blankPlaceholder;
    private Value<T, C> delegate;

    /**
     * Construct a new Column with a given {@link Cell}.
     *
     * @param cell the Cell used by this Column
     */
    public Column(Cell<C> cell) {
        this.cell = cell;
    }

    public Column(Cell<C> cell, C defaultValue) {
        this.cell = cell;
        this.defaultValue = defaultValue;
    }

    public Column(Cell<C> cell, Value<T, C> delegate) {
        this(cell);
        this.delegate = delegate;
    }

    public Column(Cell<C> cell, Value<T, C> delegate, C defaultValue) {
        this(cell);
        this.delegate = delegate;
        this.defaultValue = defaultValue;
    }

    /**
     * Handle a browser event that took place within the column.
     *
     * @param context the cell context
     * @param elem    the parent Element
     * @param object  the base object to be updated
     * @param event   the native browser event
     */
    public void onBrowserEvent(Context context, Element elem, final T object, NativeEvent event) {
        final int index = context.getIndex();
        ValueUpdater<C> valueUpdater = (fieldUpdater == null) ? null : value -> {
            fieldUpdater.update(index, object, value);
        };
        C value = getValue(object);
        cell.onBrowserEvent(context, elem, value != null ? value : defaultValue, event, valueUpdater);
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
    public C getValue(T object) {
        if (delegate != null) {
            C value = delegate.value(object);
            return value != null ? value : defaultValue;
        } else if (defaultValue != null) {
            return defaultValue;
        } else {
            throw new UnsupportedOperationException("No value delegate defined and getValue was not overridden");
        }
    }

    public Value<T, C> delegate() {
        return delegate;
    }

    public void delegate(Value<T, C> delegate) {
        this.delegate = delegate;
    }

    /**
     * Render the object into the cell.
     *
     * @param object the object to render
     * @param sb     the buffer to render into
     */
    public Column<T, C> render(Context context, T object, SafeHtmlBuilder sb) {
        C value = getValue(object);

        if (value == null) {
            value = defaultValue;
        }

        if (value != null) {
            cell.render(context, value, sb);
        } else {
            sb.append(this::blankPlaceholder);
        }

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
    public final String name() {
        return this.name;
    }

    /**
     * Sets the help description beside the column name.
     */
    public Column<T, C> help(String help) {
        this.help = help;
        return this;
    }

    /**
     * Returns the help description of datatable's column.
     */
    public String help() {
        return help;
    }

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
     *
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

    public boolean isHidden() {
        return hidden;
    }

    /**
     * Make this column hidden by default
     */
    public Column<T, C> setHidden(boolean hidden) {
        this.hidden = hidden;
        return this;
    }

    public boolean isHideable() {
        return hideable;
    }

    /**
     * This column restricts to be hidden
     */
    public Column<T, C> setHideable(boolean hideable) {
        this.hideable = hideable;
        return this;
    }

    public final Comparator<? super RowComponent<T>> sortComparator() {
        if (sortComparator == null) {
            sortComparator = Comparator.comparing(o -> getValue(o.getData()).toString());
        }
        return sortComparator;
    }

    public Column<T, C> sortComparator(Comparator<? super RowComponent<T>> sortComparator) {
        this.sortComparator = sortComparator;
        return this;
    }

    public Column<T, C> sortable(boolean sortable) {
        this.sortable = sortable;
        return this;
    }

    /**
     * Check if the column is sortable.
     *
     * @return true if sortable, false if not
     */
    public boolean sortable() {
        return sortable;
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
     * @param value     the string value required for the given style property.
     */
    public Column<T, C> styleProperty(StyleName styleName, String value) {
        if (styleProps == null) {
            styleProps = new HashMap<>();
        }
        styleProps.put(styleName, value);
        return this;
    }

    /**
     * Get a styles property.
     *
     * @param styleName the styles name as represented in a {@link Style} class.
     * @return null if the style property is not set.
     */
    public String styleProperty(StyleName styleName) {
        return styleProps != null ? styleProps.get(styleName) : null;
    }

    /**
     * Return the registered style properties.
     *
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
        if (frozenProps != null) {
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
     * @Override
     * public FrozenProperties frozenProperties() {
     * return new FrozenProperties("200px", "60px")
     * .setStyleProperty(StyleName.PADDING_LEFT, "20px")
     * .setStyleProperty(StyleName.PADDING_TOP, "10px")
     * .setHeaderStyleProperty(StyleName.PADDING_TOP, "21px");
     * }
     * @Override
     * public String getValue(Person object) {
     * return object.getName();
     * }
     * }, "Name");
     * }</pre>
     * <p>
     * Columns must be aligned with each other without any unfrozen columns in between.
     */
    public final FrozenProperties frozenProperties() {
        return frozenProps;
    }

    public final boolean isFrozenColumn() {
        return frozenProps != null;
    }

    public Column<T, C> width(int width) {
        return width(width + "px");
    }

    /**
     * Set the columns header width.
     */
    public Column<T, C> width(String width) {
        this.width = width;

        if (widthPixelToPercent()) {
            if (!extractWidthPixelValues()) {
                // turn off width pixel percent calculation
                widthPixels = -1;
                widthPixelToPercent(false);
            }
        } else {
            this.dynamicWidth = width.contains("%");
        }
        return this;
    }

    private boolean extractWidthPixelValues() {
        try {
            if (width != null && width.contains("px")) {
                widthPixels = Integer.valueOf(width.substring(0, width.indexOf("px")));
                return true;
            }
        } catch (NumberFormatException ex) {
            GWT.log("Could not cast width to an integer.", ex);
        }
        return false;
    }

    /**
     * Get the columns header width.
     *
     * @return null if not defined.
     */
    public final String width() {
        return width;
    }

    /**
     * Automatically calculate percentages using the {@link #width(String)} pixel configuration.
     * Note that this won't work if the width configuration isn't <b>px</b>.
     */
    public Column<T, C> widthPixelToPercent(boolean widthPixelToPercent) {
        this.widthToPercent = widthPixelToPercent;

        if (this.dynamicWidth && !widthPixelToPercent && !width.contains("%")) {
            this.dynamicWidth = false;
        }

        extractWidthPixelValues();
        return this;
    }

    public boolean widthPixelToPercent() {
        return widthToPercent;
    }

    public final void setIndex(int index) {
        this.index = index;
    }

    public final int getIndex() {
        return index;
    }

    /**
     * Is our width configuration a dynamic value (i.e. percentage %)
     *
     * @return false by default but will update to true when % width is set
     */
    public boolean isDynamicWidth() {
        return dynamicWidth;
    }

    public void setWidthPixels(int widthPixels) {
        this.widthPixels = widthPixels;
    }

    public int getWidthPixels() {
        return widthPixels;
    }

    public C defaultValue() {
        return defaultValue;
    }

    public Column<T, C> defaultValue(C defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    /**
     * A blank or empty placeholder when column's value is null.
     */
    public String blankPlaceholder() {
        if (blankPlaceholder == null) {
            String defaultBlankPlaceholder = getDataView().getBlankPlaceholder();
            blankPlaceholder = defaultBlankPlaceholder != null ? defaultBlankPlaceholder : MaterialDataTable.getGlobals().getBlankPlaceholder();
        }
        return blankPlaceholder;
    }

    public Column<T, C> blankPlaceholder(String blankPlaceholder) {
        this.blankPlaceholder = blankPlaceholder;
        return this;
    }

    public Column<T, C> helpEnabled(boolean helpEnabled) {
        this.helpEnabled = helpEnabled;
        return this;
    }

    public Boolean isHelpEnabled() {
        if (helpEnabled == null) {
            Boolean defaultHelpEnabled = getDataView().isHelpEnabled();
            helpEnabled = defaultHelpEnabled != null ? defaultHelpEnabled : MaterialDataTable.getGlobals().isHelpEnabled();
        }
        return helpEnabled;
    }

    public Column<T, C> useRemoteSort(boolean useRemoteSort) {
        this.useRemoteSort = useRemoteSort;
        return this;
    }

    public Boolean isUseRemoteSort() {
        return useRemoteSort;
    }

    public Column<T, C> addFooter(FooterColumn<T> footer) {
        this.footer = footer;
        footer.setColumn(this);
        return this;
    }

    public FooterColumn<T> getFooter() {
        return footer;
    }

    public DataView<T> getDataView() {
        return dataView;
    }

    public void setDataView(DataView<T> dataView) {
        this.dataView = dataView;
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
            '}';
    }
}
