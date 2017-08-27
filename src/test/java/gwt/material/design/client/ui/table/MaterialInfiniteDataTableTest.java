package gwt.material.design.client.ui.table;

import gwt.material.design.client.model.Person;

public class MaterialInfiniteDataTableTest extends MaterialDataTableTest<MaterialInfiniteDataTable<Person>> {

    @Override
    protected MaterialInfiniteDataTable<Person> constructTable() {
        return new MaterialInfiniteDataTable<>();
    }
}
