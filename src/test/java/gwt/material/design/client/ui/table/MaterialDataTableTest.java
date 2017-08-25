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

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.Range;
import gwt.material.design.client.MaterialDataTableTestCase;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.ListDataSource;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.events.DestroyEvent;
import gwt.material.design.client.data.events.InsertColumnEvent;
import gwt.material.design.client.data.events.RemoveColumnEvent;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialImage;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class MaterialDataTableTest extends MaterialDataTableTestCase {

    private static final Logger logger = Logger.getLogger(MaterialDataTableTest.class.getName());

    public void testLoadingInConstructor() throws Exception {
        // given / when / then
        MaterialDataTable<Person> table = attachTableWithConstructor();

        // Test the reattach
        table.removeFromParent();
        RootPanel.get().add(table);
    }

    public void testLoadingOnAttach() throws Exception {
        // given / when / then
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        // Test the reattach
        table.removeFromParent();
        RootPanel.get().add(table);
    }

    public void testProperties() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        // when / then
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
        assertEquals(SelectionType.SINGLE, table.getSelectionType());
        table.setSelectionType(SelectionType.MULTIPLE);
        assertEquals(SelectionType.MULTIPLE, table.getSelectionType());
        table.setSelectionType(SelectionType.NONE);
        assertEquals(SelectionType.NONE, table.getSelectionType());
    }

    public void testStructure() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        // Table Title
        table.setTitle("table title");
        assertEquals("table title", table.getTitle());
        TableScaffolding scaffolding = table.getScaffolding();
        MaterialIcon tableIcon = table.getTableIcon();
        assertEquals(IconType.VIEW_LIST, tableIcon.getIconType());
        assertTrue(scaffolding.getInfoPanel().getElement().hasClassName(TableCssName.INFO_PANEL));
        assertTrue(scaffolding.getTable().getElement().hasClassName(TableCssName.TABLE));
        assertTrue(scaffolding.getTableBody().getElement().hasClassName(TableCssName.TABLE_BODY));
        assertTrue(scaffolding.getToolPanel().getElement().hasClassName(TableCssName.TOOL_PANEL));
        // Stretch Icon
        MaterialIcon stretchIcon = table.getStretchIcon();
        assertEquals(IconType.FULLSCREEN, stretchIcon.getIconType());
        assertEquals("stretch", stretchIcon.getId());
        // Column Menu Icon
        MaterialIcon columnMenuIcon = table.getColumnMenuIcon();
        assertEquals(IconType.MORE_VERT, columnMenuIcon.getIconType());
        assertEquals("columnToggle", columnMenuIcon.getId());
        assertTrue(scaffolding.getTopPanel().getElement().hasClassName(TableCssName.TOP_PANEL));
        // Dropdown Menu
        MaterialDropDown dropDown = table.getMenu();
        assertEquals(3, dropDown.getWidgetCount());
        int index = 0;
        for (Widget w : dropDown) {
            assertTrue(w instanceof MaterialCheckBox);
            MaterialCheckBox checkBox = (MaterialCheckBox) w;
            assertEquals(checkBox.getText(), table.getColumns().get(index).getName());
            index++;
        }
    }

    public void testEvents() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();

        // SetupEvent
        final boolean[] isSetupFired = {false};
        table.addSetupHandler(event -> {
            isSetupFired[0] = true;
        });

        RootPanel.get().add(table);
        assertTrue(isSetupFired[0]);

        table.removeFromParent();
        isSetupFired[0] = false;

        // Attach again to ensure it doesn't fire
        RootPanel.get().add(table);
        assertFalse(isSetupFired[0]);

        // InsertColumnEvent
        final boolean[] isInsertedColumnFired = {false};
        table.addHandler(event -> {
            isInsertedColumnFired[0] = true;
        }, InsertColumnEvent.TYPE);

        table.addColumn(new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getLastName();
            }
        }, "Last Name");
        assertTrue(isInsertedColumnFired[0]);

        // RemoveColumnEvent
        final boolean[] isRemoveColumnFired = {false};
        table.addHandler(event -> {
            isRemoveColumnFired[0] = true;
        }, RemoveColumnEvent.TYPE);
        table.removeColumn(0);
        assertTrue(isRemoveColumnFired[0]);

        // DestroyEvent
        final boolean[] isDestroyFired = {false};
        table.addHandler(event -> {
            isDestroyFired[0] = true;
        }, DestroyEvent.TYPE);

        table.setDestroyOnUnload(true);
        table.removeFromParent();
        assertTrue(isDestroyFired[0]);
    }

    public void testJQueryEvents() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        // when / then

        // Category Closed
        final boolean[] isCategoryClosedFired = {false};
        table.addCategoryClosedHandler((e, param1) -> {
            isCategoryClosedFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.CATEGORY_CLOSED, null);
        assertTrue(isCategoryClosedFired[0]);
        // Category Opened
        final boolean[] isCategoryOpenedFired = {false};
        table.addCategoryOpenedHandler((e, param1) -> {
            isCategoryOpenedFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.CATEGORY_OPENED, null);
        assertTrue(isCategoryOpenedFired[0]);
        // Row Context Menu
        final boolean[] isRowContextMenuFired = {false};
        table.addRowContextMenuHandler((e, param1, param2, param3) -> {
            isRowContextMenuFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_CONTEXTMENU, null);
        assertTrue(isRowContextMenuFired[0]);
        // Row Count Change
        final boolean[] isRowCountChangeFired = {false};
        table.addRowCountChangeHandler((e, param1, param2) -> {
            isRowCountChangeFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_COUNT_CHANGE, null);
        assertTrue(isRowCountChangeFired[0]);
        // Row Double Click
        final boolean[] isRowDoubleClickFired = {false};
        table.addRowDoubleClickHandler((e, param1, param2, param3) -> {
            isRowDoubleClickFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_DOUBLECLICK, null);
        assertTrue(isRowDoubleClickFired[0]);
        // Row Expand
        final boolean[] isRowExpandFired = {false};
        table.addRowExpandHandler((e, param1) -> {
            isRowExpandFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_EXPAND, null);
        assertTrue(isRowExpandFired[0]);
        // Row Expanded
        final boolean[] isRowExpandedFired = {false};
        table.addRowExpandedHandler((e, param1) -> {
            isRowExpandedFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_EXPANDED, null);
        assertTrue(isRowExpandedFired[0]);
        // Row Long Press
        final boolean[] isRowLongPressFired = {false};
        table.addRowLongPressHandler((e, param1, param2, param3) -> {
            isRowLongPressFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_LONGPRESS, null);
        assertTrue(isRowLongPressFired[0]);
        // Row Select
        final boolean[] isRowSelectFired = {false};
        table.addRowSelectHandler((e, param1, param2, param3) -> {
            isRowSelectFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_SELECT, new Object[] {
            table.getRow(0), table.getRow(0).getWidget().getElement(), true
        });
        assertTrue(isRowSelectFired[0]);
        // Row Short Press
        final boolean[] isRowShortPressFired = {false};
        table.addRowShortPressHandler((e, param1, param2, param3) -> {
            isRowShortPressFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.ROW_SHORTPRESS, null);
        assertTrue(isRowShortPressFired[0]);
        // Select All
        final boolean[] isSelectAllFired = {false};
        table.addSelectAllHandler((e, param1, param2, param3) -> {
            isSelectAllFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.SELECT_ALL, new Object[] {
            table.getRows(), new ArrayList<>(), true
        });
        assertTrue(isSelectAllFired[0]);
        // Sort Column
        final boolean[] isSortColumnFired = {false};
        table.addSortColumnHandler((e, param1, param2) -> {
            isSortColumnFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.SORT_COLUMN, null);
        assertTrue(isSortColumnFired[0]);
        // Stretch
        final boolean[] isStretchFired = {false};
        table.addStretchHandler((e, param1) -> {
            isStretchFired[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.STRETCH, null);
        // Components Rendered
        final boolean[] isCompRendered = {false};
        table.addComponentsRenderedHandler(e -> {
            isCompRendered[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.COMPONENTS_RENDERED, null);
        assertTrue(isCompRendered[0]);
        // Rendered
        final boolean[] isRendered = {false};
        table.addRenderedHandler(e -> {
            isRendered[0] = true;
            return true;
        });
        $(table).trigger(TableEvents.RENDERED, null);
        assertTrue(isRendered[0]);
    }

    public void testColumnsRows() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.clearRows(true);
        table.removeColumns();

        WidgetColumn<Person, MaterialImage> col1 = new WidgetColumn<Person, MaterialImage>() {
            @Override
            public MaterialImage getValue(Person object) {
                MaterialImage profile = new MaterialImage();
                profile.setUrl(object.getPicture());
                profile.setWidth("40px");
                profile.setHeight("40px");
                profile.setPadding(4);
                profile.setMarginTop(8);
                profile.setBackgroundColor(Color.GREY_LIGHTEN_2);
                profile.setCircle(true);
                return profile;
            }
        };
        table.addColumn(col1);
        assertEquals(col1, table.getColumns().get(0));

        Column<Person, ?> col2 = new TextColumn<Person>() {
            @Override
            public Comparator<? super RowComponent<Person>> sortComparator() {
                return (o1, o2) -> o1.getData().getFirstName().compareToIgnoreCase(o2.getData().getFirstName());
            }

            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        };
        table.addColumn(col2, "First Name");
        assertEquals(col2, table.getColumns().get(1));
        assertEquals("First Name", col2.getName());
        assertTrue(col2.isSortable());

        Column<Person, ?> col3 = new TextColumn<Person>() {
            @Override
            public Comparator<? super RowComponent<Person>> sortComparator() {
                return (o1, o2) -> o1.getData().getLastName().compareToIgnoreCase(o2.getData().getLastName());
            }

            @Override
            public String getValue(Person object) {
                return object.getLastName();
            }
        };
        table.addColumn(col3, "Last Name");
        assertEquals(col3, table.getColumns().get(2));
        assertEquals("Last Name", col3.getName());
        assertTrue(col2.isSortable());

        Column<Person, ?> col4 = new TextColumn<Person>() {
            @Override
            public boolean numeric() {
                return true;
            }

            @Override
            public HideOn hideOn() {
                return HideOn.HIDE_ON_MED_DOWN;
            }

            @Override
            public Comparator<? super RowComponent<Person>> sortComparator() {
                return (o1, o2) -> o1.getData().getPhone().compareToIgnoreCase(o2.getData().getPhone());
            }

            @Override
            public String getValue(Person object) {
                return object.getPhone();
            }
        };
        table.addColumn(col4, "Phone");
        assertEquals(col4, table.getColumns().get(3));
        assertEquals("Phone", col4.getName());
        assertTrue(col4.isNumeric());

        for (int i = 0; i < 4; i++) {
            final int index = i;
            Column<Person, ?> col = new TextColumn<Person>() {
                @Override
                public Comparator<? super RowComponent<Person>> sortComparator() {
                    return (o1, o2) -> o1.getData().getPhone().compareToIgnoreCase(o2.getData().getPhone());
                }

                @Override
                public String getValue(Person object) {
                    return object.getPhone() + " " + index;
                }
            };
            table.addColumn(col, "Column " + index);
            assertEquals(col, table.getColumns().get(3 + (i + 1)));
            assertEquals("Column " + index, col.getName());
            assertTrue(col.isSortable());
        }

        Column<Person, ?> lastCol = new WidgetColumn<Person, MaterialBadge>() {
            @Override
            public TextAlign textAlign() {
                return TextAlign.CENTER;
            }

            @Override
            public MaterialBadge getValue(Person object) {
                MaterialBadge badge = new MaterialBadge();
                badge.setText("badge " + object.getId());
                badge.setBackgroundColor(Color.BLUE);
                return badge;
            }
        };
        table.addColumn(lastCol);
        assertEquals(lastCol, table.getColumns().get(8));

        assertEquals(9, table.getColumns().size());

        table.setVisibleRange(0, 1000);
        Range range = table.getVisibleRange();
        assertEquals(0, range.getStart());
        assertEquals(1000, range.getLength());

        table.setRowData(0, people);
    }

    public void testStretch() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        // when / then
        table.stretch();
        assertTrue(table.getElement().hasClassName(TableCssName.STRETCH));
        assertTrue(MaterialWidget.body().asElement().hasClassName(TableCssName.OVERFLOW_HIDDEN));
        table.stretch();
        assertFalse(table.getElement().hasClassName(TableCssName.STRETCH));
        assertFalse(MaterialWidget.body().asElement().hasClassName(TableCssName.OVERFLOW_HIDDEN));
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

    public void testDynamicColumn() throws Exception {
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        // Remove Column
        table.removeColumn(0);
        assertEquals(2, table.getColumns().size());
        assertEquals(0, table.getColumnOffset());

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
        assertEquals(insertedCol, table.getColumns().get(0));
        assertEquals(3, table.getColumns().size());
    }

    public void testPager() throws Exception {
        MaterialDataTable<Person> table = attachTableWithOnLoad();

        ListDataSource<Person> dataSource = new ListDataSource<>();
        List<Person> people = new ArrayList<>();
        for(int k = 1; k <= 5; k++) {
            // Generate 100 models
            for(int i = 1; i <= 3; i++) {
                people.add(new Person(i, "First Name", "Last Name ", "Field " + i,"Category " + k));
            }
        }
        dataSource.add(0, people);
        table.setDataSource(dataSource);

        MaterialDataPager<Person> pager = new MaterialDataPager<>(table, dataSource);
        pager.setLimitOptions(5, 10, 15);

        RootPanel.get().add(pager);

        // Expected by default : 5
        assertEquals(pager.getLimitOptions()[0], pager.getLimit());
        assertEquals(5, pager.getLimit());
        // Expected false (Excess) because 15 (totalRows) % 5 (limit) > 0 which returns false
        assertFalse(pager.isExcess());

        // Pager row limit per page is 10 and the total data is 15 so we assume that we have an excess of 5 rows.
        // Current Page : Expected 1
        assertEquals(1, pager.getCurrentPage());
        // Total Rows : Expected 15
        assertEquals(15, pager.getTotalRows());
        // Set the limit to 10 to assume it will be excess
        pager.setLimit(10);
        // Limit : Expected 10
        assertEquals(10, pager.getLimit());
        // Excess : Expected true
        assertTrue(pager.isExcess());

        // Check Navigation
        assertEquals(1, pager.getCurrentPage());
        // The isPrevious() will be false because the current is currently the first page.
        assertFalse(pager.isPrevious());
        pager.next();
        // Going to page 2 which is the last page and has 5 rows.
        // Check the isNext() which will be expected to be false
        assertFalse(pager.isNext());
        assertTrue(pager.isLastPage());
        assertEquals(2, pager.getCurrentPage());
        pager.previous();
        assertEquals(1, pager.getCurrentPage());
    }
}
