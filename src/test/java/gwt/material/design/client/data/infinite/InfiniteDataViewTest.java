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
package gwt.material.design.client.data.infinite;

import com.google.gwt.view.client.Range;
import gwt.material.design.client.data.AbstractDataViewTest;
import gwt.material.design.client.data.MapDataSource;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.ui.table.MaterialInfiniteDataTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static gwt.material.design.jquery.JQuery.$;

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
        table.setLoaderDelay(0); // for tests we wont async load.
        table.setUseCategories(false);
        return table;
    }

    @Override
    protected MaterialInfiniteDataTable<Person> attachTableWithConstructor(boolean includeData) throws Exception {
        MaterialInfiniteDataTable<Person> table = super.attachTableWithConstructor(includeData);
        table.getView().refresh();
        return table;
    }

    @Override
    protected MaterialInfiniteDataTable<Person> attachTableWithOnLoad(boolean includeData) throws Exception {
        MaterialInfiniteDataTable<Person> table = super.attachTableWithOnLoad(includeData);
        table.getView().refresh();
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

    public void testConstructed() throws Exception {
        // given / when
        MaterialInfiniteDataTable<Person> table = createTable();

        // then
        assertTrue(((InfiniteDataView<Person>)table.getView()).getRenderer() instanceof InfiniteRenderer);
    }

    @Override
    public void testSetup() throws Exception {
        // given / when
        super.testSetup();

        InfiniteDataView<Person> dataView = (InfiniteDataView<Person>) table.getView();

        // then
        assertFalse(dataView.isDynamicView());
        assertEquals(new Range(0, 100), dataView.getVisibleRange());
        assertEquals(1, $(dataView.getContainer()).find(".bufferTop").length());
        assertEquals(1, $(dataView.getContainer()).find(".bufferBottom").length());
    }

    @Override
    public void testSortColumnByColumn() throws Exception {
        // given / when
        super.testSortColumnByColumn();
        InfiniteDataView<Person> dataView = (InfiniteDataView<Person>) table.getView();

        // then
        assertTrue(dataView.dataCache.isEmpty());
    }
}
