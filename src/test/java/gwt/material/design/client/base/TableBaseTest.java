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
package gwt.material.design.client.base;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.MaterialDesign;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.ListDataSource;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.renderer.CustomRenderer;
import gwt.material.design.client.resources.MaterialResources;
import gwt.material.design.client.resources.MaterialTableBundle;
import gwt.material.design.client.resources.WithJQueryResources;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.TableEvents;
import gwt.material.design.client.ui.table.TableScaffolding;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class TableBaseTest extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "gwt.material.design.GwtMaterialTable";
    }

    public <T extends MaterialDataTable<Person>> void test(T table) {
        setup(table);
        checkStructure(table);
        checkStretch(table);
        checkDynamicColumn(table);
        checkPager(table);
    }

    public <T extends MaterialDataTable<Person>> void setup(T table) {
        final String HEIGHT = "calc(100vh - 131px)";
        table.setHeight(HEIGHT);
        table.setUseStickyHeader(true);
        assertTrue(table.isUseStickyHeader());
        table.setUseStickyHeader(false);
        assertFalse(table.isUseStickyHeader());
        table.setUseCategories(true);
        assertTrue(table.isUseCategories());
        table.setUseCategories(false);
        assertFalse(table.isUseCategories());
        table.setUseRowExpansion(true);
        assertTrue(table.isUseRowExpansion());
        table.setUseRowExpansion(false);
        assertFalse(table.isUseRowExpansion());
        table.setSelectionType(SelectionType.SINGLE);
        assertEquals(table.getSelectionType(), SelectionType.SINGLE);
        table.setSelectionType(SelectionType.MULTIPLE);
        assertEquals(table.getSelectionType(), SelectionType.MULTIPLE);
        table.setSelectionType(SelectionType.NONE);
        assertEquals(table.getSelectionType(), SelectionType.NONE);
        // Add the Table into the Root
        RootPanel.get().add(table);
        table.setRowFactory(new PersonRowFactory());
        table.setCategoryFactory(new CustomCategoryFactory());
        table.setRenderer(new CustomRenderer<>());
    }

    public <T extends MaterialDataTable<Person>> void checkStructure(T table) {
        final String TABLE_TITLE = "table title";
        // Table Title
        table.setTitle(TABLE_TITLE);
        assertEquals(table.getTitle(), TABLE_TITLE);
        TableScaffolding scaffolding = table.getScaffolding();
        MaterialIcon tableIcon = table.getTableIcon();
        assertEquals(tableIcon.getIconType(), IconType.VIEW_LIST);
        assertTrue(scaffolding.getInfoPanel().getElement().hasClassName(TableCssName.INFO_PANEL));
        assertTrue(scaffolding.getTable().getElement().hasClassName(TableCssName.TABLE));
        assertTrue(scaffolding.getTableBody().getElement().hasClassName(TableCssName.TABLE_BODY));
        assertTrue(scaffolding.getToolPanel().getElement().hasClassName(TableCssName.TOOL_PANEL));
        // Stretch Icon
        MaterialIcon stretchIcon = table.getStretchIcon();
        assertEquals(stretchIcon.getIconType(), IconType.FULLSCREEN);
        assertEquals(stretchIcon.getId(), "stretch");
        // Column Menu Icon
        MaterialIcon columnMenuIcon = table.getColumnMenuIcon();
        assertEquals(columnMenuIcon.getIconType(), IconType.MORE_VERT);
        assertEquals(columnMenuIcon.getId(), "columnToggle");
        assertTrue(scaffolding.getTopPanel().getElement().hasClassName(TableCssName.TOP_PANEL));
        // Dropdown Menu
        MaterialDropDown dropDown = table.getMenu();
        assertEquals(dropDown.getWidgetCount(), 9);
        int index = 0;
        for (Widget w : dropDown) {
            assertTrue(w instanceof MaterialCheckBox);
            MaterialCheckBox checkBox = (MaterialCheckBox) w;
            assertEquals(table.getColumns().get(index).getName(), checkBox.getText());
            index++;
        }
    }

    public <T extends MaterialDataTable<Person>> void checkStretch(T table) {
        table.stretch();
        assertTrue(table.getElement().hasClassName(TableCssName.STRETCH));
        assertTrue(table.body().asElement().hasClassName(TableCssName.OVERFLOW_HIDDEN));
        table.stretch();
        assertFalse(table.getElement().hasClassName(TableCssName.STRETCH));
        assertFalse(table.body().asElement().hasClassName(TableCssName.OVERFLOW_HIDDEN));
        // Return back to normal state
        table.stretch();
        // Stretch Event
        boolean[] isStretchEventFired = {false};
        table.addStretchHandler((e, param1) -> {
            isStretchEventFired[0] = true;
            return true;
        });
        table.stretch(true);
        assertTrue(isStretchEventFired[0]);
    }

    public <T extends MaterialDataTable<Person>> void checkDynamicColumn(T table) {
        // Remove Column
        final int COL_INDEX = 0;
        table.removeColumn(COL_INDEX);
        assertEquals(table.getColumns().size(), 8);
        assertEquals(table.getColumnOffset(), COL_INDEX);

        // Insert Column
        Column<Person, ?> insertedCol = new TextColumn<Person>() {
            @Override
            public Comparator<? super RowComponent<Person>> sortComparator() {
                return (o1, o2) -> o1.getData().getFirstName().compareToIgnoreCase(o2.getData().getFirstName());
            }

            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        };
        table.insertColumn(0, insertedCol, "insertedCol");
        assertEquals(table.getColumns().get(0), insertedCol);
        assertEquals(table.getColumns().size(), 9);
    }

    public <T extends MaterialDataTable<Person>> void checkPager(T table) {
        RootPanel.get().add(table);

        ListDataSource<Person> dataSource = new ListDataSource<>();
        List<Person> people = new ArrayList<>();
        int rowIndex = 1;
        for(int k = 1; k <= 5; k++){
            // Generate 100 rows
            for(int i = 1; i <= 3; i++, rowIndex++){
                people.add(new Person(i, "http://joashpereira.com/templates/material_one_pager/img/avatar1.png", "Field " + rowIndex, "Field " + i,"Category " + k));
            }
        }
        dataSource.add(0, people);
        table.setDataSource(dataSource);

        MaterialDataPager<Person> pager = new MaterialDataPager<>(table, dataSource);
        pager.setLimitOptions(5, 10, 15);

        RootPanel.get().add(pager);

        // Expected by default : 5
        assertEquals(pager.getLimit(), pager.getLimitOptions()[0]);
        assertEquals(pager.getLimit(), 5);
        // Expected false (Excess) because 15 (totalRows) % 5 (limit) > 0 which returns false
        assertFalse(pager.isExcess());

        // Pager row limit per page is 10 and the total data is 15 so we assume that we have an excess of 5 rows.
        // Current Page : Expected 1
        assertEquals(pager.getCurrentPage(), 1);
        // Total Rows : Expected 15
        assertEquals(pager.getTotalRows(), 15);
        // Set the limit to 10 to assume it will be excess
        pager.setLimit(10);
        // Limit : Expected 10
        assertEquals(pager.getLimit(), 10);
        // Excess : Expected true
        assertTrue(pager.isExcess());

        // Check Navigation
        assertEquals(pager.getCurrentPage(), 1);
        // The isPrevious() will be false because the current is currently the first page.
        assertFalse(pager.isPrevious());
        pager.next();
        // Going to page 2 which is the last page and has 5 rows.
        // Check the isNext() which will be expected to be false
        assertFalse(pager.isNext());
        assertTrue(pager.isLastPage());
        assertEquals(pager.getCurrentPage(), 2);
        pager.previous();
        assertEquals(pager.getCurrentPage(), 1);
    }

    protected List<Person> getAllPerson() {
        int rowIndex = 0;
        List<Person> people = new ArrayList<>();
        for (int k = 1; k <= 3; k++) {
            // Generate 100 rows
            for (int i = 1; i <= 3; i++, rowIndex++) {
                people.add(new Person(i, "http://joashpereira.com/templates/material_one_pager/img/avatar1.png", "Field " + rowIndex, "Field " + i, "No " + i, "Category " + k));
            }
        }
        return people;
    }
}
