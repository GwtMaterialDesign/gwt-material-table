package gwt.material.design.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.ui.RootPanel;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.factory.CustomCategoryFactory;
import gwt.material.design.client.factory.PersonRowFactory;
import gwt.material.design.client.model.Person;
import gwt.material.design.client.renderer.CustomRenderer;
import gwt.material.design.client.resources.MaterialResources;
import gwt.material.design.client.resources.MaterialTableBundle;
import gwt.material.design.client.resources.WithJQueryResources;
import gwt.material.design.client.ui.MaterialBadge;
import gwt.material.design.client.ui.table.MaterialDataTable;
import gwt.material.design.client.ui.table.cell.TextColumn;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class MaterialDataTableTestCase extends GWTTestCase {

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
        MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.mutate());
        MaterialDesign.injectJs(MaterialTableBundle.INSTANCE.mutateEvents());
        // gwt-material-jquery Test
        assertNotNull($("body"));
    }

    protected MaterialDataTable<Person> attachTableWithConstructor() throws Exception {
        // given
        MaterialDataTable<Person> table = new MaterialDataTable<>();
        table.setRowFactory(new PersonRowFactory());
        table.setCategoryFactory(new CustomCategoryFactory());
        table.setRenderer(new CustomRenderer<>());

        // when
        try {
            addSampleColumns(table);
            table.setRowData(0, people);

            // then
        } catch (final AssertionError ae) {
            throw ae;
        } catch (final Throwable t) {
            throw new AssertionError("Could not load table in constructor.", t);
        }

        RootPanel.get().add(table);
        return table;
    }

    protected MaterialDataTable<Person> attachTableWithOnLoad() throws Exception {
        // given
        MaterialDataTable<Person> table = new MaterialDataTable<>();
        table.setRowFactory(new PersonRowFactory());
        table.setCategoryFactory(new CustomCategoryFactory());
        table.setRenderer(new CustomRenderer<>());

        table.addAttachHandler(event -> {
            // when
            try {
                addSampleColumns(table);
                table.setRowData(0, people);

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

    protected void addSampleColumns(MaterialDataTable<Person> table) {
        table.addColumn(new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getFirstName();
            }
        }, "First Name");

        table.addColumn(new TextColumn<Person>() {
            @Override
            public String getValue(Person object) {
                return object.getLastName();
            }
        }, "Last Name");

        table.addColumn(new WidgetColumn<Person, MaterialBadge>() {
            @Override
            public TextAlign textAlign() {
                return TextAlign.CENTER;
            }
            @Override
            public MaterialBadge getValue(Person object) {
                MaterialBadge badge = new MaterialBadge();
                badge.setText("badge " + object.getId());
                badge.setBackgroundColor(Color.BLUE);
                badge.setLayoutPosition(Style.Position.RELATIVE);
                return badge;
            }
        });
    }
}
