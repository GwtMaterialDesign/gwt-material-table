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

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.MaterialDesign;
import gwt.material.design.client.base.constants.StyleName;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.resources.MaterialResources;
import gwt.material.design.client.resources.MaterialTableBundle;
import gwt.material.design.client.resources.WithJQueryResources;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;
import gwt.material.design.jquery.client.api.JQueryElement;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static gwt.material.design.jquery.client.api.JQuery.$;

@Ignore
public class DataTableTestCase<T extends AbstractDataTable<Person>> extends GWTTestCase {

    private static final Logger logger = Logger.getLogger(DataTableTestCase.class.getName());

    protected T table;

    protected static final List<Person> people = new ArrayList<>();
    static {
        people.add(new Person(1, "Bill", "Jones", "123456", "Male"));
        people.add(new Person(2, "John", "Doe", "123456", "Male"));
        people.add(new Person(3, "Jim", "Smith", "123456", "Male"));
        people.add(new Person(4, "Beth", "Smith", "123456", "Female"));
        people.add(new Person(5, "Laura", "Doe", "123456", "Female"));
        people.add(new Person(6, "Anna", "Jones", "123456", "Female"));
    };

    @Override
    public String getModuleName() {
        return "gwt.material.design.GwtMaterialTable";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();

        if(!MaterialDesign.isjQueryLoaded()) {
            WithJQueryResources jQueryResources = GWT.create(WithJQueryResources.class);
            // Test JQuery
            MaterialDesign.injectJs(jQueryResources.jQuery());
            assertTrue(MaterialDesign.isjQueryLoaded());
            // Test Materialize
            MaterialDesign.injectJs(MaterialResources.INSTANCE.materializeJs());
            assertTrue(MaterialDesign.isMaterializeLoaded());
            // Inject Resources
            MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.jQueryExt());
            MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.stickyth());
            MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.tableSubHeaders());
            MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.greedyScroll());
            MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.mutateEvents());
            MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.mutate());
            // gwt-material-jquery Test
            assertNotNull($("body"));
        }
    }

    @Override
    protected void gwtTearDown() throws Exception {
        super.gwtTearDown();

        table = null;
        RootPanel.get().clear();
    }

    protected T attachTableWithConstructor() throws Exception {
        return attachTableWithConstructor(true);
    }

    protected T attachTableWithConstructor(boolean includeData) throws Exception {
        // given
        T table = createTable();

        // when
        try {
            addSampleColumns(table);
            if(includeData) {
                addSampleRows(table);
            }

        // then
        } catch (final AssertionError ae) {
            throw ae;
        } catch (final Throwable t) {
            throw new AssertionError("Could not load table in constructor.", t);
        }

        RootPanel.get().add(table);
        return table;
    }

    protected T attachTableWithOnLoad() throws Exception {
        return attachTableWithOnLoad(true);
    }

    protected T attachTableWithOnLoad(boolean includeData) throws Exception {
        // given
        T table = createTable();

        table.addAttachHandler(event -> {
            // when
            try {
                addSampleColumns(table);
                if(includeData) {
                    addSampleRows(table);
                }

            // then
            } catch (final AssertionError ae) {
                throw ae;
            } catch (final Throwable t) {
                throw new AssertionError("Could not load table in onLoad.", t);
            }
        });

        RootPanel.get().add(table);
        return table;
    }

    protected T constructTable() {
        return (T) new MaterialDataTable<Person>();
    }

    protected T createTable() {
        table = constructTable();
        table.setVisibleRange(0, 100);
        table.getView().setRowFactory(new PersonRowFactory());
        table.getView().setCategoryFactory(new CustomCategoryFactory());
        return table;
    }

    protected void addSampleRows(T table) {
        table.setRowData(0, people);
    }

    protected void addSampleColumns(T table) {
        Map<StyleName, String> styleProps = new HashMap<>();
        styleProps.put(StyleName.COLOR, "white");

        table.addColumn("First Name", new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        }
        .textAlign(TextAlign.CENTER)
        .numeric(true)
        .width("200px")
        .hideOn(HideOn.HIDE_ON_MED)
        .autoSort(true)
        .styleProperties(styleProps));

        table.addColumn("Last Name", new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getLastName();
            }
        });

        table.addColumn(new WidgetColumn<Person, MaterialBadge>() {
            @Override
            public MaterialBadge getValue(Person object) {
                MaterialBadge badge = new MaterialBadge();
                badge.setText("badge " + object.getId());
                badge.setBackgroundColor(Color.BLUE);
                badge.setLayoutPosition(Style.Position.RELATIVE);
                return badge;
            }
        }.textAlign(TextAlign.CENTER));
    }

    public static void assertValidJQueryElement(JQueryElement element) {
        assertNotNull(element);
        assertTrue(element.get().length > 0);
    }

    public static <T> void printRows(Components<RowComponent<T>> rowComponents) {
        StringBuilder msg = new StringBuilder("[");
        for(RowComponent<T> row : rowComponents) {
            msg.append(row.getData().toString()).append(", ");
        }
        logger.severe(msg + "]");
    }
}
