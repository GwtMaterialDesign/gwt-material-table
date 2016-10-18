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

import com.google.gwt.view.client.Range;
import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.renderer.CustomRenderer;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.TableEvents;
import junit.framework.TestCase;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class TableBaseTest extends TestCase {

    public <T extends MaterialDataTable<Person>> void checkTable(T table) {
        checkEventHandlers(table);
        setup(table);
    }

    public <T extends MaterialDataTable<Person>> void setup(T table) {
        table.setRowFactory(new PersonRowFactory());
        table.setCategoryFactory(new CustomCategoryFactory());
        table.setRenderer(new CustomRenderer<>());
        table.setVisibleRange(0, 1000);
        Range range = table.getVisibleRange();
        assertEquals(range.getStart(), 0);
        assertEquals(range.getLength(), 1000);
    }

    public <T extends MaterialDataTable<Person>> void checkEventHandlers(T table) {
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
        $(table).trigger(TableEvents.ROW_SELECT, null);
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
        $(table).trigger(TableEvents.SELECT_ALL, null);
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
        assertTrue(isStretchFired[0]);
    }
}
