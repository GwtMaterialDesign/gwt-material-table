package gwt.material.design.client.test;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.MaterialDataTableTestCase;
import gwt.material.design.client.base.constants.StyleName;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.BaseRenderer;
import gwt.material.design.client.data.SelectionType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableRow;
import gwt.material.design.client.ui.table.TableSubHeader;
import gwt.material.design.client.ui.table.cell.Column;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class BaseRendererTest extends MaterialDataTableTestCase {

    private static final Logger logger = Logger.getLogger(BaseRendererTest.class.getName());

    public void testDrawRow() throws Exception {
        // given
        BaseRenderer<Person> renderer = new BaseRenderer<>();
        MaterialDataTable<Person> table = createTable();
        table.setUseRowExpansion(true);
        addSampleColumns(table);
        table.setSelectionType(SelectionType.SINGLE);
        table.setRenderer(renderer);

        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        List<Column<Person, ?>> columns = dataView.getColumns();

        Person person = new Person(1, "John", "Doe", "123456", "Catergory");
        RowComponent<Person> rowComponent = dataView.getRowFactory().generate(dataView, person);

        // when
        TableRow tableRow = renderer.drawRow(dataView, rowComponent, dataView.getValueKey(person), columns, true);

        // then
        assertNotNull("TableRow should not be null.", tableRow);

        Style style = tableRow.getElement().getStyle();
        assertEquals(Style.Display.NONE.getCssName(), style.getDisplay());

        assertEquals(renderer.getExpectedRowHeight() + "px", style.getProperty("height"));
        assertEquals(renderer.getExpectedRowHeight() + "px", style.getProperty("maxHeight"));
        assertEquals(renderer.getExpectedRowHeight() + "px", style.getProperty("minHeight"));

        assertTrue(tableRow.getStyleName().contains(TableCssName.DATA_ROW));

        assertEquals("RowComponent widget is not the rendered TableRow", tableRow, rowComponent.getWidget());

        try {
            TableData selectionCell = (TableData)tableRow.getWidget(0);
            assertEquals("col0", selectionCell.getId());
            assertTrue(selectionCell.getStyleName().contains(TableCssName.SELECTION));
        } catch (ClassCastException ex) {
            throw new AssertionError("Could not cast selection cell to TableData");
        }

        int colOffset = dataView.getColumnOffset();
        assertEquals(4 + colOffset, tableRow.getWidgetCount());

        assertTrue(tableRow.hasExpansionColumn());
        TableData expandRow = tableRow.getExpansionColumn();
        assertEquals("colex", expandRow.getId());
        MaterialIcon expandIcon = (MaterialIcon)expandRow.getWidget(0);
        assertEquals("expand", expandIcon.getId());
        assertEquals("100%", expandIcon.getElement().getStyle().getWidth());
        assertEquals(IconType.KEYBOARD_ARROW_DOWN, expandIcon.getIconType());
        assertEquals(WavesType.LIGHT, expandIcon.getWaves());
        assertEquals(Style.Cursor.POINTER.getCssName(), expandIcon.getElement().getStyle().getCursor());
    }

    public void testDrawColumn() throws Exception {
        // given
        BaseRenderer<Person> renderer = new BaseRenderer<>();
        MaterialDataTable<Person> table = createTable();
        addSampleColumns(table);
        table.setSelectionType(SelectionType.SINGLE);
        table.setRenderer(renderer);

        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        List<Column<Person, ?>> columns = dataView.getColumns();

        Person person = new Person(1, "John", "Doe", "123456", "Catergory");

        // when
        TableRow row = new TableRow();
        TableData columnText = renderer.drawColumn(row,
            new Cell.Context(1, 0, dataView.getValueKey(person)), person, columns.get(0), 0, true);
        TableData columnWidget = renderer.drawColumn(row,
            new Cell.Context(1, 1, dataView.getValueKey(person)), person, columns.get(2), 1, true);

        // then

        try {
            Div wrapper = (Div)columnText.getWidget(0);
            assertEquals("John", wrapper.getElement().getInnerHTML());
            assertTrue(wrapper.getStyleName().contains(TableCssName.CELL));

        } catch (ClassCastException ex) {
            throw new AssertionError("There was an issue casting Text cell.", ex);
        } catch (IndexOutOfBoundsException ex) {
            throw new AssertionError("There was an issue indexing.", ex);
        }

        try {
            Div wrapper = (Div)columnWidget.getWidget(0);
            assertTrue(wrapper.getStyleName().contains(TableCssName.WIDGET_CELL));

            MaterialBadge widget = (MaterialBadge)wrapper.getWidget(0);
            assertEquals("badge 1", widget.getText());
            assertEquals(Color.BLUE, widget.getBackgroundColor());
            assertEquals(Style.Position.RELATIVE.getCssName(), widget.getElement().getStyle().getPosition());

        } catch (ClassCastException ex) {
            throw new AssertionError("There was an issue casting Widget cell.", ex);
        } catch (IndexOutOfBoundsException ex) {
            throw new AssertionError("There was an issue indexing.", ex);
        }
    }

    public void testDrawColumnHeader() {
        // given
        BaseRenderer<Person> renderer = new BaseRenderer<>();
        MaterialDataTable<Person> table = createTable();
        addSampleColumns(table);
        table.setRenderer(renderer);

        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        Column<Person, ?> column = dataView.getColumns().get(0);

        // when
        TableHeader th = renderer.drawColumnHeader(column, column.getName(), 0);

        // then
        assertEquals("col0", th.getId());
        assertEquals(column.getName(), th.getHeader());
        assertEquals(column.getHideOn(), th.getHideOn());
        assertEquals(column.getTextAlign(), th.getTextAlign());
        assertEquals(column.isNumeric(), th.getStyleName().contains(TableCssName.NUMERIC));
        assertEquals(column.getWidth(), th.getElement().getStyle().getWidth());

        for(Map.Entry<StyleName, String> entry : column.getStyleProperties().entrySet()) {
            assertEquals(entry.getValue(), th.getElement().getStyle().getProperty(entry.getKey().styleName()));
        }
        assertEquals(renderer.getSortIconSize(), th.getSortIcon().getIconSize());
    }

    public void testCalculateRowHeight() throws Exception {
        // given
        BaseRenderer<Person> renderer = new BaseRenderer<>();

        // when
        MaterialDataTable<Person> table = attachTableWithConstructor();
        table.setRenderer(renderer);

        // then
        assertEquals(table.getRowHeight(), renderer.getCalculatedRowHeight());
    }

    public void testDrawCategory() throws Exception {
        // given
        BaseRenderer<Person> renderer = new BaseRenderer<>();
        MaterialDataTable<Person> table = createTable();
        table.setUseRowExpansion(true);
        addSampleColumns(table);
        table.setSelectionType(SelectionType.SINGLE);
        table.setRenderer(renderer);

        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();
        List<Column<Person, ?>> columns = dataView.getColumns();

        Person person = new Person(1, "John", "Doe", "123456", "Catergory");
        CategoryComponent category = dataView.getCategoryFactory().generate(dataView, person.getDataCategory());

        // when
        TableSubHeader subheader = renderer.drawCategory(category);

        // then
        assertNotNull(subheader);
    }
}
