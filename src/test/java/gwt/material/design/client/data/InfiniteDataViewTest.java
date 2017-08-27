package gwt.material.design.client.data;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.data.infinite.InfiniteDataView;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.ui.table.MaterialInfiniteDataTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class InfiniteDataViewTest extends AbstractDataViewTest<MaterialInfiniteDataTable<Person>> {

    private static final Logger logger = Logger.getLogger(InfiniteDataViewTest.class.getName());

    public static List<String> categories = new ArrayList<>();
    public static Map<String, List<Person>> peopleMap = new HashMap<>();
    static {
        categories.add("People");
        categories.add("Animal");
        categories.add("Plant");
        categories.add("Other");
        categories.add("Next");

        int index = 0;
        for(String category : categories) {
            for(int i = 0; i < 100; i++, index++) {
                List<Person> data = peopleMap.computeIfAbsent(category, k -> new ArrayList<>());
                data.add(new Person(index,
                    "http://joashpereira.com/templates/material_one_pager/img/avatar1.png",
                    category+index,
                    "Last"+index,
                    "Phone"+index,
                    category));
            }
        }
    }

    @Override
    protected MaterialInfiniteDataTable<Person> constructTable() {
        return new MaterialInfiniteDataTable<>(20, 20, new MapDataSource<>());
    }

    @Override
    protected MaterialInfiniteDataTable<Person> createTable() {
        MaterialInfiniteDataTable<Person> table = super.createTable();
        table.setUseCategories(false);
        return table;
    }

    @Override
    protected void addSampleRows(MaterialInfiniteDataTable<Person> table) {
        List<Person> flatData = new ArrayList<>();
        for(Map.Entry<String, List<Person>> entry : peopleMap.entrySet()) {
            flatData.addAll(entry.getValue());
        }
        ((MapDataSource)table.getDataSource()).add(flatData);
    }

    @Override
    public void testSelectRow() throws Exception {
        // given
        MaterialInfiniteDataTable<Person> table = attachTableWithConstructor();
        table.setSelectionType(SelectionType.SINGLE);
        AbstractDataView<Person> dataView = (AbstractDataView<Person>)table.getView();

        dataView.refresh();
        logger.severe(table.getRows().toString());

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
}
