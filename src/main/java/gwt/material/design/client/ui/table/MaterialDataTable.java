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
package gwt.material.design.client.ui.table;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.*;
import gwt.material.design.client.data.events.InsertColumnEvent;
import gwt.material.design.client.data.events.InsertColumnHandler;
import gwt.material.design.client.data.events.RemoveColumnEvent;
import gwt.material.design.client.data.events.RemoveColumnHandler;
import gwt.material.design.client.data.events.SetupHandler;
import gwt.material.design.client.ui.table.events.StretchEvent;
import gwt.material.design.client.ui.table.events.StretchHandler;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.js.Js;
import gwt.material.design.client.js.JsTableElement;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.table.cell.Column;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * The standard Material data table with custom "outer" components such as,
 * table icon, table title, stretch functionality, column toggling, etc.
 *
 * @author Ben Dol
 *
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/#datatable">Material Data Table</a>
 * @see <a href="https://material.io/guidelines/components/data-tables.html">Material Design Specification</a>
 */
public class MaterialDataTable<T> extends AbstractDataTable<T> implements InsertColumnHandler<T>, RemoveColumnHandler {

    private JQueryElement stretchContainer;

    // Global Configs
    private static String defaultBlankPlaceholder;
    private static DateTimeFormat defaultDateFormat;
    private static NumberFormat defaultIntegerFormat;
    private static NumberFormat defaultLongFormat;
    private static NumberFormat defaultDoubleFormat;
    private static NumberFormat defaultFloatFormat;
    private static NumberFormat defaultBigDecimalFormat;
    private static NumberFormat defaultShortFormat;

    // Interface
    private MaterialIcon tableIcon;
    private Span tableTitle;
    private MaterialIcon stretchIcon;
    private MaterialIcon columnMenuIcon;
    private MaterialDropDown menu;

    public MaterialDataTable() {
        super();
        loadInternalEvents();
    }

    public MaterialDataTable(DataView<T> dataView) {
        super(dataView);
        loadInternalEvents();
    }

    public MaterialDataTable(TableScaffolding scaffolding) {
        super(scaffolding);
        loadInternalEvents();
    }

    public MaterialDataTable(DataView<T> dataView, TableScaffolding scaffolding) {
        super(dataView, scaffolding);
        loadInternalEvents();
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // Attempt to rebuild encase the widgets have been unloaded.
        // In some use cases (like GWTP) the child widgets aren't unloaded.
        // So we make sure we check the state of each Widget.
        build();
    }

    @Override
    protected void build() {
        super.build();

        Panel infoPanel = scaffolding.getInfoPanel();
        Panel toolPanel = scaffolding.getToolPanel();

        if(tableIcon == null || !tableIcon.isAttached()) {
            // table icon
            tableIcon = new MaterialIcon(IconType.VIEW_LIST);
            infoPanel.add(tableIcon);
        }

        if(tableTitle == null || !tableTitle.isAttached()) {
            // table title
            tableTitle = new Span("Table Title");
            tableTitle.addStyleName(TableCssName.TITLE);
            infoPanel.add(tableTitle);
        }

        if(stretchIcon == null || !stretchIcon.isAttached()) {
            // stretch icon
            stretchIcon = new MaterialIcon(IconType.FULLSCREEN);
            stretchIcon.setWaves(WavesType.LIGHT);
            stretchIcon.setCircle(true);
            stretchIcon.setId("stretch");
            stretchIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            toolPanel.add(stretchIcon);
            getView().getAccessibilityControl().registerWidget(stretchIcon);
        }

        if(columnMenuIcon == null || !columnMenuIcon.isAttached()) {
            // menu icon
            columnMenuIcon = new MaterialIcon(IconType.MORE_VERT);
            columnMenuIcon.setHideOn(HideOn.HIDE_ON_SMALL_DOWN);
            columnMenuIcon.setWaves(WavesType.LIGHT);
            columnMenuIcon.setCircle(true);
            columnMenuIcon.setId("columnToggle");
            columnMenuIcon.getElement().getStyle().setCursor(Style.Cursor.POINTER);
            toolPanel.add(columnMenuIcon);
            getView().getAccessibilityControl().registerWidget(columnMenuIcon);
        }

        if(stretchContainer == null) {
            // stretch container
            stretchContainer = $("body");
        }

        setupToolPanel();
        setupMenu();
    }

    protected void setupToolPanel() {
        // Stretch click handler
        $(scaffolding.getToolPanel()).find("i#stretch").off("click").on("click", e -> {
            stretch();

            e.preventDefault();
            return true;
        });
    }

    protected void setupMenu() {
        // Setup menu checkboxes
        // This will allow the user to toggle columns

        if(menu == null) {
            // dropdown structure
            menu = new MaterialDropDown(columnMenuIcon);
            scaffolding.getToolPanel().add(menu);

            // Menu initialization
            menu.setInDuration(300);
            menu.setOutDuration(225);
            menu.setConstrainWidth(false);
            menu.setHover(false);
            menu.setGutter(0);
            menu.setBelowOrigin(false);
            menu.setAlignment(Alignment.LEFT);
            menu.setHideOn(HideOn.HIDE_ON_SMALL_DOWN);
            menu.getElement().getStyle().setProperty("minWidth", "200px");
        }

        JQueryElement $menu = $(menu);
        $menu.find("li label").off("tap click");
        $menu.find("li label").on("tap click", e -> {
            JQueryElement $this = $(e.getCurrentTarget());

            String forBox = ((String) $this.attr("for")).replace(getView().getId() + "-", "");
            if(Js.isTrue(forBox)) {
                JQueryElement thd = $("th#" + forBox + ",td#" + forBox, this);
                boolean checked = $this.prev().is(":checked");

                thd.each((index, el) -> {
                    JQueryElement cell = $(el);
                    if(checked) {
                        cell.hide();
                    } else {
                        cell.show();
                    }
                });

                // Update the sticky table header widths
                scaffolding.getTable().getJsElement().stickyTableHeaders("updateWidth");

                // Recalculate the subheader
                getView().getSubheaderLib().recalculate(true);
            }
            return true;
        });

        // Stop each menu item from closing the dropdown
        $menu.find("li").off("touchstart click");
        $menu.find("li").on("touchstart click", e -> {
            e.stopPropagation();
            return true;
        });
    }

    @Override
    public void onInsertColumn(InsertColumnEvent<T> event) {
        int beforeIndex = event.getBeforeIndex();
        Column<T, ?> column = event.getColumn();
        String header = event.getHeader();

        SetupHandler handler = e -> {
            int index = beforeIndex + getView().getColumnOffset();
            String ref = getView().getId() + "-col" + index;

            MaterialCheckBox toggleBox = new MaterialCheckBox(new ListItem().getElement());
            toggleBox.setType(CheckBoxType.FILLED);
            JQueryElement input = $(toggleBox).find("input");
            input.attr("id", ref);

            JQueryElement label = $(toggleBox).find("label");
            label.text(column.name());
            label.attr("for", ref);

            toggleBox.setValue(true);
            menu.add(toggleBox);

            // We will hide the empty header menu items
            if (header.isEmpty()) {
                toggleBox.setVisible(false);
            }

            setupMenu();
            reindexToggles();
        };

        if(getView().isSetup()) {
            handler.onSetup(null);
        } else {
            addSetupHandler(handler);
        }
    }

    @Override
    public void onRemoveColumn(RemoveColumnEvent event) {
        SetupHandler handler = e -> {
            int index = event.getIndex() + getView().getColumnOffset();
            $(menu).find("li input#col" + index).parent().remove();
            reindexToggles();
        };

        if(getView().isSetup()) {
            handler.onSetup(null);
        } else {
            addSetupHandler(handler);
        }
    }

    private void reindexToggles() {
        int colOffset = getView().getColumnOffset();
        $("li", menu).each((index, e) -> {
            String ref = getView().getId() + "-col" + ((Double)index + colOffset);

            JQueryElement input = $(e).find("input");
            input.attr("id", ref);

            JQueryElement label = $(e).find("label");
            label.attr("for", ref);
        });
    }

    public void stretch() {
        stretch(true);
    }

    public void stretch(boolean fireEvent) {
        $this().toggleClass(TableCssName.STRETCH);

        // Make sure the body doesn't display scrollbar
        body().toggleClass(TableCssName.OVERFLOW_HIDDEN);

        // Update table header width
        JsTableElement tableJs = scaffolding.getTable().getJsElement();
        tableJs.stickyTableHeaders("updateWidth");
        tableJs.stickyTableHeaders("toggleHeaders");

        // Recalculate subheaders
        getView().getSubheaderLib().recalculate(true);

        if(fireEvent) {
            // Fire table stretch event
            StretchEvent.fire(this, $this().hasClass(TableCssName.STRETCH));
        }
    }

    public MaterialIcon getStretchIcon() {
        return stretchIcon;
    }

    public MaterialIcon getColumnMenuIcon() {
        return columnMenuIcon;
    }

    public MaterialIcon getTableIcon() {
        return tableIcon;
    }

    public MaterialDropDown getMenu() {
        return menu;
    }

    public Span getTableTitle() {
        return tableTitle;
    }

    /**
     * Add a handler that is triggered when the table is stretched.
     */
    public HandlerRegistration addStretchHandler(StretchHandler handler) {
        return addHandler(handler, StretchEvent.TYPE);
    }

    /**
     * Load events
     */
    protected void loadInternalEvents() {
        // Register data view events, these are removed onUnload.
        addInsertColumnHandler(this);
        addRemoveColumnHandler(this);
    }

    public static DateTimeFormat getDefaultDateFormat() {
        if (defaultDateFormat == null) {
            defaultDateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL);
        }
        return defaultDateFormat;
    }

    /**
     * Will set a global default date format for {@link gwt.material.design.client.ui.table.cell.DateColumn}
     */
    public static void setDefaultDateFormat(DateTimeFormat defaultDateFormat) {
        MaterialDataTable.defaultDateFormat = defaultDateFormat;
    }

    public static NumberFormat getDefaultIntegerFormat() {
        return defaultIntegerFormat;
    }

    /**
     * Will set a global default integer format for {@link gwt.material.design.client.ui.table.cell.IntegerColumn}
     */
    public static void setDefaultIntegerFormat(NumberFormat defaultIntegerFormat) {
        MaterialDataTable.defaultIntegerFormat = defaultIntegerFormat;
    }

    public static NumberFormat getDefaultLongFormat() {
        return defaultLongFormat;
    }

    /**
     * Will set a global default long format for {@link gwt.material.design.client.ui.table.cell.LongColumn}
     */
    public static void setDefaultLongFormat(NumberFormat defaultLongFormat) {
        MaterialDataTable.defaultLongFormat = defaultLongFormat;
    }

    public static NumberFormat getDefaultDoubleFormat() {
        return defaultDoubleFormat;
    }

    /**
     * Will set a global default double format for {@link gwt.material.design.client.ui.table.cell.DoubleColumn}
     */
    public static void setDefaultDoubleFormat(NumberFormat defaultDoubleFormat) {
        MaterialDataTable.defaultDoubleFormat = defaultDoubleFormat;
    }

    public static NumberFormat getDefaultFloatFormat() {
        return defaultFloatFormat;
    }

    /**
     * Will set a global default float format for {@link gwt.material.design.client.ui.table.cell.FloatColumn}
     */
    public static void setDefaultFloatFormat(NumberFormat defaultFloatFormat) {
        MaterialDataTable.defaultFloatFormat = defaultFloatFormat;
    }

    public static NumberFormat getDefaultBigDecimalFormat() {
        return defaultBigDecimalFormat;
    }

    /**
     * Will set a global default BigDecimal format for {@link gwt.material.design.client.ui.table.cell.BigDecimalColumn}
     */
    public static void setDefaultBigDecimalFormat(NumberFormat defaultBigDecimalFormat) {
        MaterialDataTable.defaultBigDecimalFormat = defaultBigDecimalFormat;
    }

    public static NumberFormat getDefaultShortFormat() {
        return defaultShortFormat;
    }

    /**
     * Will set a global default Short format for {@link gwt.material.design.client.ui.table.cell.ShortColumn}
     */
    public static void setDefaultShortFormat(NumberFormat defaultShortFormat) {
        MaterialDataTable.defaultShortFormat = defaultShortFormat;
    }

    public static String getDefaultBlankPlaceholder() {
        if (defaultBlankPlaceholder == null) {
            defaultBlankPlaceholder = "";
        }
        return defaultBlankPlaceholder;
    }

    /**
     * Will set a global default blank placeholder used within {@link Column#blankPlaceholder()} to replace
     * any null values.
     */
    public static void setDefaultBlankPlaceholder(String defaultBlankPlaceholder) {
        MaterialDataTable.defaultBlankPlaceholder = defaultBlankPlaceholder;
    }
}
