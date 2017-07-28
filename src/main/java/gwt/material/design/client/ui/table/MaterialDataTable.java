package gwt.material.design.client.ui.table;

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


import com.gargoylesoftware.htmlunit.attachment.AttachmentHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.client.constants.Alignment;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
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
public class MaterialDataTable<T> extends AbstractDataTable<T> {

    private JQueryElement stretchContainer;

    // Interface
    private MaterialIcon tableIcon;
    private Span tableTitle;
    private MaterialIcon stretchIcon;
    private MaterialIcon columnMenuIcon;
    private MaterialDropDown menu;

    public MaterialDataTable() {
    }

    public MaterialDataTable(DataView<T> dataView) {
        super(dataView);
    }

    public MaterialDataTable(TableScaffolding scaffolding) {
        super(scaffolding);
    }

    public MaterialDataTable(DataView<T> dataView, TableScaffolding scaffolding) {
        super(dataView, scaffolding);
    }

    @Override
    public void setup(TableScaffolding scaffolding) throws Exception {
        super.setup(scaffolding);

        Panel tableBody = scaffolding.getTableBody();
        Panel infoPanel = scaffolding.getInfoPanel();
        Panel toolPanel = scaffolding.getToolPanel();

        // table icon
        tableIcon = new MaterialIcon(IconType.VIEW_LIST);
        infoPanel.add(tableIcon);

        // table title
        tableTitle = new Span("Table Title");
        tableTitle.addStyleName(TableCssName.TITLE);
        infoPanel.add(tableTitle);

        // stretch icon
        stretchIcon = new MaterialIcon(IconType.FULLSCREEN);
        stretchIcon.setWaves(WavesType.LIGHT);
        stretchIcon.setCircle(true);
        stretchIcon.setId("stretch");
        $(stretchIcon).css("cursor", "pointer");
        toolPanel.add(stretchIcon);

        // menu icon
        columnMenuIcon = new MaterialIcon(IconType.MORE_VERT);
        columnMenuIcon.setHideOn(HideOn.HIDE_ON_SMALL_DOWN);
        columnMenuIcon.setWaves(WavesType.LIGHT);
        columnMenuIcon.setCircle(true);
        columnMenuIcon.setId("columnToggle");
        $(columnMenuIcon).css("cursor", "pointer");
        toolPanel.add(columnMenuIcon);

        // stretch container
        stretchContainer = $("body");

        setupToolPanel();
        setupMenu();
    }

    protected void setupToolPanel() {
        // Stretch click handler
        $(scaffolding.getToolPanel()).find("i#stretch").on("click", e -> {
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

            String forBox = ((String) $this.attr("for")).replace(getViewId() + "-", "");
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
                getSubheaderLib().recalculate(true);
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
    public void insertColumn(int beforeIndex, Column<T, ?> col, String header) {
        super.insertColumn(beforeIndex, col, header);

        AttachEvent.Handler hander = event -> {
            int index = beforeIndex + getColumnOffset();
            String ref = getViewId() + "-col" + index;

            MaterialCheckBox toggleBox = new MaterialCheckBox(new ListItem().getElement());
            JQueryElement input = $(toggleBox).find("input");
            input.attr("id", ref);

            JQueryElement label = $(toggleBox).find("label");
            label.text(col.getName());
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

        if(isSetup()) {
            hander.onAttachOrDetach(null);
        } else {
            addAttachHandler(hander, true);
        }
    }

    @Override
    public void removeColumn(int colIndex) {
        super.removeColumn(colIndex);

        int index = colIndex + getColumnOffset();
        $(menu).find("li").get(index).removeFromParent();
        reindexToggles();
    }

    private void reindexToggles() {
        int colOffset = getColumnOffset();
        $("li", menu).each((index, e) -> {
            String ref = getViewId() + "-col" + ((Double)index + colOffset);

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

        // Update table header widths
        JsTableElement tableJs = scaffolding.getTable().getJsElement();
        tableJs.stickyTableHeaders("updateWidth");
        tableJs.stickyTableHeaders("toggleHeaders");

        // Recalculate subheaders
        getSubheaderLib().recalculate(true);

        if(fireEvent) {
            // Fire table stretch event
            $this().trigger(TableEvents.STRETCH, new Object[]{$this().hasClass(TableCssName.STRETCH)});
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
}
