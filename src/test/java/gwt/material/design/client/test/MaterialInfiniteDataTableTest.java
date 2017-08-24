package gwt.material.design.client.test;

import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.renderer.CustomRenderer;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.MaterialInfiniteDataTable;

public class MaterialInfiniteDataTableTest extends MaterialDataTableTest {

    @Override
    protected MaterialDataTable<Person> createTable() {
        MaterialInfiniteDataTable<Person> table = new MaterialInfiniteDataTable<>();
        table.getView().setRowFactory(new PersonRowFactory());
        table.getView().setCategoryFactory(new CustomCategoryFactory());
        table.getView().setRenderer(new CustomRenderer<>());
        return table;
    }
}
