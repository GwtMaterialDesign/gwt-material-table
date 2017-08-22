package gwt.material.design.client.test;

import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.MaterialDataTableTestCase;
import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.BaseRenderer;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.SortContext;
import gwt.material.design.client.data.SortDir;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.TextColumn;

import java.util.ArrayList;
import java.util.List;

public class AbstractDataViewTest extends MaterialDataTableTestCase {

    public void testRenderRowComponents() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor(false);
        DataView<Person> dataView = table.getView();
        Components<Component<?>> components = generateRowComponents(dataView);

        table.addComponentsRenderedHandler(event -> {
            checkRowComponents(table, people.size());
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
            assertEquals(2, table.getScaffolding().getTableBody().getElement().getChildCount());
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

        table.addComponentsRenderedHandler(event -> {
            checkRowComponents(table, people.size());

            table.removeComponentsRenderedHandlers();
            table.addComponentsRenderedHandler(event1 -> {
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
        assertTrue(dataView.isRendering());
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

    public void testSortColumn() throws Exception {
        // given
        MaterialDataTable<Person> table = attachTableWithConstructor();
        DataView<Person> dataView = table.getView();

        // when
        table.sort(1);

        // then

    }

    public void testAutoSortColumn() throws Exception {
        // given
        MaterialDataTable<Person> table = createTable();
        DataView<Person> dataView = table.getView();
        RootPanel.get().add(table);

        table.addComponentsRenderedHandler(event -> {
            // First column is autoSort'ing
            TableHeader th = dataView.getHeaders().get(0);
            SortContext<Person> sortContext = dataView.getSortContext();
            assertEquals(SortDir.DESC, sortContext.getSortDir());
            assertEquals(th, sortContext.getTableHeader());
            return true;
        });

        // when
        table.setRowData(0, people);

        // then
        assertFalse(dataView.isRedraw());
        assertTrue(dataView.isRendering());
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
            components.add(dataView.getCategoryFactory().generate(dataView, person.getDataCategory()));
        }
        return components;
    }

    public void checkRowComponents(MaterialDataTable<Person> table, int size) {
        DataView<Person> dataView = table.getView();
        assertFalse(dataView.isRendering());
        assertEquals(size, dataView.getRowCount());
        assertEquals(size, dataView.getVisibleItemCount());
        assertEquals(size, table.getScaffolding().getTableBody().getElement().getChildCount());
        int index = 0;
        for(RowComponent<Person> rowComponent : dataView.getRows()) {
            assertTrue(rowComponent.getWidget().isVisible());
            assertEquals(index, rowComponent.getIndex());
            index++;
        }
    }
}
