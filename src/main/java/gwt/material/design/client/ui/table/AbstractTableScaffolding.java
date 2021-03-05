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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialPanel;

public abstract class AbstractTableScaffolding implements TableScaffolding {

    private Panel tableBody;
    private Panel topPanel;
    private Panel infoPanel;
    private Panel toolPanel;
    private TableFooter<?> footer;
    private Table table;

    private XScrollPanel xScrollPanel;

    @Override
    public void build(AbstractDataTable dataTable) {
        tableBody = createTableBody();
        topPanel = createTopPanel();
        infoPanel = createInfoPanel();
        toolPanel = createToolPanel();
        footer = createFooter(dataTable);
        table = createTable();
        xScrollPanel = createXScrollPanel(tableBody);
    }

    abstract protected Panel createTableBody();

    @Override
    public Panel getTableBody() {
        return tableBody;
    }

    abstract protected Panel createTopPanel();

    @Override
    public Panel getTopPanel() {
        return topPanel;
    }

    abstract protected Panel createInfoPanel();

    @Override
    public Panel getInfoPanel() {
        return infoPanel;
    }

    abstract protected Panel createToolPanel();

    @Override
    public Panel getToolPanel() {
        return toolPanel;
    }

    abstract protected <T> TableFooter<T> createFooter(AbstractDataTable<T> dataTable);

    @Override
    public <T> TableFooter<T> getFooter() {
        return (TableFooter<T>) footer;
    }

    abstract protected XScrollPanel createXScrollPanel(Panel container);

    @Override
    public XScrollPanel getXScrollPanel() {
        return xScrollPanel;
    }

    abstract protected Table createTable();

    @Override
    public Table getTable() {
        return table;
    }

    protected Panel wrapInnerScroll(Panel table) {
        MaterialPanel innerScroll = new MaterialPanel();
        innerScroll.setStyleName("inner-scroll");
        innerScroll.add(table);
        return innerScroll;
    }

    /**
     * Apply the scaffolding together.
     * @param container the base container for the scaffolding.
     */
    @Override
    public void apply(HasWidgets container) {
        container.clear();
        container.add(topPanel);
        container.add(tableBody);
        container.add(xScrollPanel);

        topPanel.add(infoPanel);
        topPanel.add(toolPanel);

        tableBody.add(wrapInnerScroll(table));

        table.addHead(new MaterialWidget(DOM.createElement("thead")));
        table.addBody(new MaterialWidget(DOM.createElement("tbody")));
        table.addFooter(footer);
    }
}
