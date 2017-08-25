package gwt.material.design.client.data;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.MaterialDataTableTestCase;
import gwt.material.design.client.SortHelper;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.CssName;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.renderer.CustomRenderer;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableRow;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.jquery.client.api.KeyEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class AbstractDataViewTest extends MaterialDataTableTestCase {

    private static final Logger logger = Logger.getLogger(AbstractDataView.class.getName());

    public void testSetup() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>) table.getView();

        table.setSelectionType(SelectionType.SINGLE);
        table.setUseRowExpansion(true);

        boolean[] setup = {false};
        table.addSetupHandler(event -> setup[0] = true);

        // when
        RootPanel.get().add(table);

        // then
        assertTrue(setup[0]);
        assertTrue(dataView.isSetup());
        assertNotNull(dataView.table);
        assertNotNull(dataView.getContainer());
        assertNotNull(dataView.tbody);
        assertNotNull(dataView.thead);
        assertNotNull(dataView.headerRow);
        assertNotNull(dataView.progressWidget);

        assertValidJQueryElement(dataView.container);
        assertValidJQueryElement(dataView.tableBody);
        assertValidJQueryElement(dataView.topPanel);
        assertValidJQueryElement(dataView.$table);

        assertEquals(2, dataView.getHeaders().size());
    }

    public void testSetupPendingRows() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>) table.getView();

        table.setRowData(0, people);

        table.addRenderedHandler(e -> {
            assertFalse(dataView.isRendering());
            return true;
        });

        // when
        RootPanel.get().add(table);

        // then
        checkRowComponents(table, people.size());
    }

    public void testDestroy() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor(false);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>) table.getView();

        boolean[] destroyed = {false};
        table.addDestroyHandler(event -> destroyed[0] = true);

        // when
        dataView.destroy();

        // then
        assertTrue(destroyed[0]);
        assertTrue(dataView.getRows().isEmpty());
        assertTrue(dataView.getCategories().isEmpty());
        assertTrue(dataView.getColumns().isEmpty());
        assertTrue(dataView.getHeaders().isEmpty());
        assertTrue(dataView.headerRow.getWidgetCount() < 1);
        assertFalse(dataView.isRendering());
        assertFalse(dataView.isSetup());
    }

    public void testRenderRowComponents() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor(false);
        DataView<Person> dataView = table.getView();
        Components<Component<?>> components = generateRowComponents(dataView);

        table.addComponentsRenderedHandler(event -> {
            checkNonIndexRowComponents(table, people.size());
            return true;
        });

        // when
        dataView.render(components);

        // then
        assertFalse(dataView.isRedraw());
    }

    public void testRenderCategoryComponents() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor(false);
        table.setUseCategories(true);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        Components<Component<?>> components = generateCategoryComponents(dataView);

        table.addComponentsRenderedHandler(event -> {
            assertFalse(dataView.isRendering());
            assertEquals(0, dataView.getRowCount());
            assertEquals(0, dataView.getVisibleItemCount());
            assertEquals(2, dataView.tbody.getWidgetCount());
            return true;
        });

        // when
        dataView.render(components);

        // then
        assertFalse(dataView.isRedraw());
    }

    public void testSetRowData() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor(false);
        DataView<Person> dataView = table.getView();

        table.addRenderedHandler(event -> {
            checkRowComponents(table, people.size());

            table.removeRenderedHandlers();
            table.addRenderedHandler(event1 -> {
                checkRowComponents(table, 9);
                return true;
            });

            table.setRowData(3, people);
            return true;
        });

        // when
        table.setRowData(0, people);

        // then
        assertFalse(dataView.isRedraw());
        assertFalse(dataView.isRendering());
    }

    public void testRenderColumn() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setSelectionType(SelectionType.NONE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        RootPanel.get().add(table);

        TextColumn<Person> column = new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        };
        dataView.getColumns().add(column);

        // when
        dataView.renderColumn(column);

        // then
        List<TableHeader> header = dataView.getHeaders();
        assertTrue(!header.isEmpty());
    }

    public void testRenderColumnWithRows() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setSelectionType(SelectionType.NONE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        RootPanel.get().add(table);

        table.setRowData(0, people);

        TextColumn<Person> column = new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        };
        dataView.getColumns().add(column);

        // when
        dataView.renderColumn(column);

        // then
        List<TableHeader> header = dataView.getHeaders();
        assertTrue(!header.isEmpty());

        try {
            for (RowComponent<Person> rowComponent : dataView.getRows()) {
                assertTrue(rowComponent.getWidget().getColumn(0).isAttached());
            }
        } catch (IndexOutOfBoundsException | NullPointerException ex) {
            throw new AssertionError("Problem adding row column to rows.", ex);
        }
    }

    public void testRemoveColumnByIndex() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        DataView<Person> dataView = table.getView();

        Column<Person, ?> column = dataView.getColumns().get(0);

        // when
        dataView.removeColumn(0);

        // then
        assertFalse(dataView.getColumns().contains(column));
    }

    public void testRemoveAllColumns() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        DataView<Person> dataView = table.getView();

        // when
        dataView.removeColumns();

        // then
        assertTrue(dataView.getColumns().isEmpty());
    }

    public void testUpdateSortContext() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>) table.getView();

        Column<Person, ?> column = dataView.getColumns().get(1);
        TableHeader th = dataView.getHeaders().get(1);

        // when
        dataView.updateSortContext(th, column);

        // then
        SortContext<Person> sortContext = dataView.getSortContext();
        assertNotNull(sortContext);
        assertEquals(column, sortContext.getSortColumn());
        assertEquals(th, sortContext.getTableHeader());
        assertEquals(SortDir.ASC, sortContext.getSortDir());
    }

    public void testUpdateSortContextOnAutoSortColumn() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>) table.getView();

        Column<Person, ?> column = dataView.getColumns().get(0);
        TableHeader th = dataView.getHeaders().get(0);

        // when
        dataView.updateSortContext(th, column);

        // then
        SortContext<Person> sortContext = dataView.getSortContext();
        assertNotNull(sortContext);
        assertEquals(column, sortContext.getSortColumn());
        assertEquals(th, sortContext.getTableHeader());
        assertEquals(SortDir.DESC, sortContext.getSortDir());
    }

    public void testUpdateSortContextWithDir() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>) table.getView();

        Column<Person, ?> column = dataView.getColumns().get(0);
        TableHeader th = dataView.getHeaders().get(0);

        // when
        dataView.updateSortContext(th, column, SortDir.DESC);

        // then
        SortContext<Person> sortContext = dataView.getSortContext();
        assertNotNull(sortContext);
        assertEquals(column, sortContext.getSortColumn());
        assertEquals(th, sortContext.getTableHeader());
        assertEquals(SortDir.DESC, sortContext.getSortDir());
    }

    public void testSortColumnByIndex() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        DataView<Person> dataView = table.getView();
        Components<RowComponent<Person>> beforeRows = new Components<>(dataView.getRows(), RowComponent::new);

        boolean[] sorted = {false};
        table.addSortColumnHandler((event, sortContext, integer) -> {
            sorted[0] = true;
            return true;
        });

        // when
        table.sort(1, SortDir.DESC);
        SortContext<Person> sortContext = dataView.getSortContext();
        assertTrue(sorted[0]);
        sorted[0] = false;
        assertEquals(SortDir.DESC, sortContext.getSortDir());

        Components<RowComponent<Person>> descRows = new Components<>(dataView.getRows(), RowComponent::new);

        table.sort(1, SortDir.ASC);
        sortContext = dataView.getSortContext();
        assertTrue(sorted[0]);
        assertEquals(SortDir.ASC, sortContext.getSortDir());

        Components<RowComponent<Person>> ascRows = new Components<>(dataView.getRows(), RowComponent::new);

        // then
        assertTrue(SortHelper.isNotSame(beforeRows, descRows));
        assertTrue(SortHelper.isNotSame(descRows, ascRows));
    }

    public void testSortColumnByColumn() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        DataView<Person> dataView = table.getView();
        Components<RowComponent<Person>> beforeRows = new Components<>(dataView.getRows(), RowComponent::new);

        boolean[] sorted = {false};
        table.addSortColumnHandler((event, sortContext, integer) -> {
            sorted[0] = true;
            return true;
        });

        Column<Person, ?> column = table.getColumns().get(0);

        // when
        table.sort(column, SortDir.DESC);
        SortContext<Person> sortContext = dataView.getSortContext();
        assertTrue(sorted[0]);
        sorted[0] = false;
        assertEquals(SortDir.DESC, sortContext.getSortDir());

        Components<RowComponent<Person>> descRows = new Components<>(dataView.getRows(), RowComponent::new);

        table.sort(column, SortDir.ASC);
        sortContext = dataView.getSortContext();
        assertTrue(sorted[0]);
        assertEquals(SortDir.ASC, sortContext.getSortDir());

        Components<RowComponent<Person>> ascRows = new Components<>(dataView.getRows(), RowComponent::new);

        // then
        assertTrue(SortHelper.isNotSame(beforeRows, descRows));
        assertTrue(SortHelper.isNotSame(descRows, ascRows));
    }

    public void testAutoSortColumnBeforeAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        DataView<Person> dataView = table.getView();
        addSampleColumns(table);

        // when
        table.setRowData(0, people);

        table.addRenderedHandler(event -> {
            // First column is autoSort'ing
            checkColumnSort(dataView, 0);
            return true;
        });

        // then
        RootPanel.get().add(table);

        assertFalse(dataView.isRedraw());
        assertFalse(dataView.isRendering());
    }

    public void testAutoSortColumnAfterAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        DataView<Person> dataView = table.getView();
        addSampleColumns(table);

        // when
        RootPanel.get().add(table);

        // then
        table.addRenderedHandler(event -> {
            // First column is autoSort'ing
            checkColumnSort(dataView, 0);
            return true;
        });

        table.setRowData(0, people);

        assertFalse(dataView.isRedraw());
        assertFalse(dataView.isRendering());
    }

    public void testSetRendererBeforeAttached() {
        // given
        MaterialDataTable<Person> table = createTable();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        BaseRenderer<Person> renderer = new BaseRenderer<>();

        // when
        dataView.setRenderer(renderer);
        RootPanel.get().add(table);

        // then
        assertEquals(renderer, dataView.getRenderer());
    }

    public void testSetRendererAfterAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        BaseRenderer<Person> renderer = new BaseRenderer<>();

        // when
        dataView.setRenderer(renderer);

        // then
        assertEquals(renderer, dataView.getRenderer());
    }

    public void testSetRendererCopyProperties() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        Renderer<Person> oldRenderer = dataView.getRenderer();
        BaseRenderer<Person> renderer = new BaseRenderer<>();

        // when
        oldRenderer.setExpectedRowHeight(80);
        oldRenderer.setSortAscIcon(IconType.FULLSCREEN);
        oldRenderer.setSortDescIcon(IconType.KEYBOARD);
        oldRenderer.setSortIconSize(IconSize.LARGE);
        dataView.setRenderer(renderer);

        // then
        assertEquals(renderer, dataView.getRenderer());
        assertEquals(renderer.getExpectedRowHeight(), oldRenderer.getExpectedRowHeight());
        assertEquals(renderer.getCalculatedRowHeight(), oldRenderer.getCalculatedRowHeight());

        assertNotSame(renderer.getSortIconSize(), oldRenderer.getSortIconSize());
        assertNotSame(renderer.getSortAscIcon(), oldRenderer.getSortAscIcon());
        assertNotSame(renderer.getSortDescIcon(), oldRenderer.getSortDescIcon());
    }

    public void testColumnOffset() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.SINGLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        // when / then
        int colOffset = dataView.getColumnOffset();
        assertEquals(1, colOffset);
    }

    public void testSetSelectionTypeSingleAfterAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        // when
        table.setSelectionType(SelectionType.SINGLE);

        // then
        assertEquals(SelectionType.SINGLE, dataView.getSelectionType());
        TableHeader th = dataView.getHeaders().get(0);
        assertTrue(th.getStyleName().contains(TableCssName.SELECTION));

        checkSelectionRow(dataView.getRows());
    }

    public void testSetSelectionTypeMultipleAfterAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        // when
        table.setSelectionType(SelectionType.MULTIPLE);

        // then
        assertEquals(SelectionType.MULTIPLE, dataView.getSelectionType());
        TableHeader th = dataView.getHeaders().get(0);
        assertTrue(th.getStyleName().contains(TableCssName.SELECTION));

        checkSelectionRow(dataView.getRows());
    }

    public void testSetSelectionTypeNoneAfterAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setSelectionType(SelectionType.SINGLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        addSampleColumns(table);
        table.setRowData(0, people);
        RootPanel.get().add(table);

        int colMod = dataView.getColumnOffset();
        int totalColumns = dataView.getColumns().size() + colMod;

        // when
        table.setSelectionType(SelectionType.NONE);

        // then
        assertEquals(SelectionType.NONE, dataView.getSelectionType());
        TableHeader th = dataView.getHeaders().get(0);
        assertFalse(th.getStyleName().contains(TableCssName.SELECTION));

        for(RowComponent<Person> row : dataView.getRows()) {
            TableRow tableRow = row.getWidget();
            int count = tableRow.getWidgetCount();
            assertEquals(totalColumns, count);

            for(int i = 0; i < count; i++) {
                TableData td = tableRow.getColumn(i);
                assertEquals("col" + i, td.getId());
            }
        }

        for(int i = 0; i < dataView.headerRow.getWidgetCount(); i++) {
            TableData td = dataView.headerRow.getColumn(i);
            assertEquals("col" + i, td.getId());
        }
    }

    public void testCustomRenderer() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setRenderer(new CustomRenderer<>());
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        // when
        RootPanel.get().add(table);

        // then
        for(RowComponent<Person> row : dataView.getRows()) {
            try {
                TableData td = (TableData) row.getWidget().getWidget(0);
                assertTrue(td.getId().equals("col0"));
                assertEquals(CssName.FILLED_IN, td.getElement().getAttribute("class"));
            }
            catch (ClassCastException | IndexOutOfBoundsException ex) {
                throw new AssertionError("Issue testing selection column.", ex);
            }
        }
    }

    public void testShiftDetection() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        // when
        table.setSelectionType(SelectionType.MULTIPLE);

        // then
        assertEquals("0", dataView.tableBody.attr("tabindex"));

        KeyEvent keyEvent = new KeyEvent("keydown");
        keyEvent.shiftKey = true;

        // fake shift key down
        dataView.tableBody.trigger(keyEvent);
        assertTrue(dataView.isShiftDown());

        // fake shift key up
        keyEvent = new KeyEvent("keydown");
        keyEvent.shiftKey = false;
        dataView.tableBody.trigger(keyEvent);

        assertFalse(dataView.isShiftDown());
    }

    public void testSelectAllRows() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.MULTIPLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        int rowCount = table.getRowCount();

        boolean[] selectAll = {false};
        table.addSelectAllHandler((e, param1, param2, param3) -> {
            selectAll[0] = true;
            assertEquals(rowCount, param1.size());
            assertEquals(rowCount, param2.size());
            assertTrue(param3);
            return true;
        });

        // when
        dataView.selectAllRows(true);

        // then
        assertTrue(selectAll[0]);

        dataView.$table.find("tr.data-row").each((i, e) -> {
            JQueryElement row = $(e);
            JQueryElement input = $("td#col0 input", row);

            assertTrue((boolean)input.prop("checked"));
            assertTrue(row.hasClass("selected"));
        });
    }

    public void testDeselectAllRows() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.MULTIPLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        dataView.selectAllRows(true, false);
        int rowCount = table.getRowCount();

        boolean[] deselectAll = {false};
        table.addSelectAllHandler((e, param1, param2, param3) -> {
            deselectAll[0] = true;
            assertEquals(rowCount, param1.size());
            assertEquals(rowCount, param2.size());
            assertFalse(param3);
            return true;
        });

        // when
        dataView.selectAllRows(false);

        // then
        assertTrue(deselectAll[0]);

        dataView.$table.find("tr.data-row").each((i, e) -> {
            JQueryElement row = $(e);
            JQueryElement input = $("td#col0 input", row);

            Boolean checked = (Boolean)input.prop("checked");
            if(checked != null) {
                assertFalse(checked);
            }
            assertFalse(row.hasClass("selected"));
        });
    }

    public void testSelectRow() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.SINGLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        RowComponent<Person> rowComponent = table.getRow(3);
        Element element = rowComponent.getWidget().getElement();

        boolean[] rowSelect = {false};
        table.addRowSelectHandler((e, param1, param2, param3) -> {
            rowSelect[0] = true;
            assertEquals(rowComponent.getData(), param1);
            assertEquals(element, param2);
            assertTrue(param3);
            return true;
        });

        // when
        dataView.selectRow(element, true);

        // then
        assertTrue(rowSelect[0]);
        assertTrue(element.getClassName().contains("selected"));
        Boolean checked = (Boolean) $("td#col0 input", element).prop("checked");
        assertNotNull(checked);
        assertTrue(checked);
    }

    public void testDeselectRow() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        RowComponent<Person> rowComponent = table.getRow(3);
        Element element = rowComponent.getWidget().getElement();

        dataView.selectRow(element, false);

        boolean[] rowDeselect = {false};
        table.addRowSelectHandler((e, param1, param2, param3) -> {
            rowDeselect[0] = true;
            assertEquals(rowComponent.getData(), param1);
            assertEquals(element, param2);
            assertFalse(param3);
            return true;
        });

        // when
        dataView.deselectRow(element, true);

        // then
        assertTrue(rowDeselect[0]);
        assertFalse(element.getClassName().contains("selected"));
        String checked = (String) $("td#col0 input", element).prop("checked");
        if(checked != null) {
            assertEquals("false", checked);
        }
    }

    public void testHasSelectedRows() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.SINGLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        RowComponent<Person> rowComponent = table.getRow(3);
        Element element = rowComponent.getWidget().getElement();

        assertFalse(dataView.hasSelectedRows(true));

        dataView.selectRow(element, false);

        // when
        boolean hasSelectedRows = dataView.hasSelectedRows(true);

        // then
        assertTrue(hasSelectedRows);
    }

    public void testHasDeselectedRows() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.SINGLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        RowComponent<Person> rowComponent = table.getRow(3);
        Element element = rowComponent.getWidget().getElement();

        dataView.selectRow(element, false);

        // when
        boolean hasDeselectedRows = dataView.hasDeselectedRows(true);
        dataView.selectAllRows(true);

        // then
        assertTrue(hasDeselectedRows);
        assertFalse(dataView.hasDeselectedRows(true));
    }

    public void testAddCategory() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setUseCategories(true);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        addSampleColumns(table);

        RootPanel.get().add(table);
        String categoryName = "Test Category";

        // when
        assertFalse(dataView.hasCategory(categoryName));
        dataView.addCategory(categoryName);

        // then
        assertTrue(dataView.hasCategory(categoryName));

        CategoryComponent category = dataView.getCategory(categoryName);
        assertNotNull(category);
        assertTrue(category.isRendered());
        assertTrue(category.getWidget().isAttached());
    }

    public void testAddCategoryBeforeAttached() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setUseCategories(true);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        addSampleColumns(table);

        String categoryName = "Test Category";

        // when
        dataView.addCategory(categoryName);
        RootPanel.get().add(table);

        // then
        assertTrue(dataView.hasCategory(categoryName));

        CategoryComponent category = dataView.getCategory(categoryName);
        assertNotNull(category);
        assertTrue(category.isRendered());
        assertTrue(category.getWidget().isAttached());
    }

    public void testDisableCategory() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setUseCategories(true);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        addSampleColumns(table);

        String categoryName = "Test Category";
        dataView.addCategory(categoryName);
        RootPanel.get().add(table);

        CategoryComponent category = dataView.getCategory(categoryName);

        // when
        dataView.disableCategory(categoryName);

        // then
        assertFalse(category.getWidget().isEnabled());
    }

    public void testEnableCategory() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        table.setUseCategories(true);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        addSampleColumns(table);

        String categoryName = "Test Category";
        dataView.addCategory(categoryName);
        RootPanel.get().add(table);

        CategoryComponent category = dataView.getCategory(categoryName);
        dataView.disableCategory(categoryName);
        assertFalse(category.getWidget().isEnabled());

        // when
        dataView.enableCategory(categoryName);

        // then
        assertTrue(category.getWidget().isEnabled());
    }

    public void testUpdateRow() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        dataView.setRowData(0, people);
        RowComponent<Person> row = dataView.getRow(3);
        int index = row.getIndex();
        Person person = row.getData();

        // when
        person.setFirstName("Updated First Name");
        person.setLastName("Updated Last Name");
        dataView.updateRow(person);

        // then
        row = dataView.getRow(3);
        assertEquals(index, row.getIndex());
        assertEquals(person.getFirstName(), row.getData().getFirstName());
        assertEquals(person.getLastName(), row.getData().getLastName());
        assertEquals(person.getFirstName(), ((MaterialWidget)row.getWidget().getWidget(0)).getWidget(0).getElement().getInnerText());
        assertEquals(person.getLastName(), ((MaterialWidget)row.getWidget().getWidget(1)).getWidget(0).getElement().getInnerText());
    }

    public Components<Component<?>> generateRowComponents(DataView<Person> dataView) {
        Components<Component<?>> components = new Components<>();
        for(Person person : people) {
            components.add(new RowComponent<>(person, dataView, person.getDataCategory()));
        }
        return components;
    }

    public Components<Component<?>> generateCategoryComponents(AbstractDataView<Person> dataView) {
        Components<Component<?>> components = new Components<>();
        for(Person person : people) {
            CategoryComponent category = dataView.getCategoryFactory().generate(dataView, person.getDataCategory());
            if(!components.contains(category)) {
                components.add(category);
            }
        }
        return components;
    }

    public void checkRowComponents(MaterialDataTable<Person> table, int size) {
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        assertFalse(dataView.isRendering());
        assertEquals(size, dataView.getRowCount());
        assertEquals(size, dataView.getVisibleItemCount());
        assertEquals(size, dataView.tbody.getWidgetCount());
        int index = 0;
        for(RowComponent<Person> rowComponent : dataView.getRows()) {
            assertTrue(rowComponent.getWidget().isVisible());
            assertEquals(index, rowComponent.getIndex());
            index++;
        }
    }

    public void checkNonIndexRowComponents(MaterialDataTable<Person> table, int size) {
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        assertFalse(dataView.isRendering());
        assertEquals(size, dataView.getRowCount());
        assertEquals(size, dataView.getVisibleItemCount());
        assertEquals(size, dataView.tbody.getWidgetCount());

        for(RowComponent<Person> rowComponent : dataView.getRows()) {
            assertTrue(rowComponent.getWidget().isVisible());
        }
    }

    public void checkSelectionRow(List<RowComponent<Person>> rows) {
        for(RowComponent<Person> row : rows) {
            try {
                TableData td = (TableData) row.getWidget().getWidget(0);
                assertTrue(td.getId().equals("col0"));
                assertEquals(TableCssName.SELECTION, td.getStyleName());
            }
            catch (ClassCastException | IndexOutOfBoundsException ex) {
                throw new AssertionError("Issue testing selection column.", ex);
            }
        }
    }

    public void checkColumnSort(DataView<Person> dataView, int index) {
        TableHeader th = dataView.getHeaders().get(index);
        SortContext<Person> sortContext = dataView.getSortContext();
        assertNotNull(sortContext);
        assertEquals(SortDir.ASC, sortContext.getSortDir());
        assertEquals(th, sortContext.getTableHeader());
    }

    public Map<Column<Person, ?>, Integer> getColumnIndexes(List<Column<Person, ?>> columns) {
        Map<Column<Person, ?>, Integer> indexes = new HashMap<>();
        int i = 0;
        for(Column<Person, ?> column : columns) {
            indexes.put(column, i++);
        }
        return indexes;
    }
}
