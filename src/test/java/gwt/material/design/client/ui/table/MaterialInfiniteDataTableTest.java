package gwt.material.design.client.ui.table;

import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;

public class MaterialInfiniteDataTableTest extends MaterialDataTableTest {

    @Override
    protected MaterialDataTable<Person> createTable() {
        MaterialInfiniteDataTable<Person> table = new MaterialInfiniteDataTable<>();
        table.getView().setRowFactory(new PersonRowFactory());
        table.getView().setCategoryFactory(new CustomCategoryFactory());
        return table;
    }
}
