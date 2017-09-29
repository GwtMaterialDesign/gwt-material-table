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
import gwt.material.design.client.js.JsTableElement;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * Table scaffolding that will construct the Panels for the table foundation.
 *
 * @author Ben Dol
 */
public abstract class TableScaffolding {

    private Panel tableBody;
    private Panel topPanel;
    private Panel infoPanel;
    private Panel toolPanel;
    private Table table;

    public void build() {
        tableBody = createTableBody();
        topPanel = createTopPanel();
        infoPanel = createInfoPanel();
        toolPanel = createToolPanel();
        table = createTable();
    }

    abstract protected Panel createTableBody();

    public Panel getTableBody() {
        return tableBody;
    }

    abstract protected Panel createTopPanel();

    public Panel getTopPanel() {
        return topPanel;
    }

    abstract protected Panel createInfoPanel();

    public Panel getInfoPanel() {
        return infoPanel;
    }

    abstract protected Panel createToolPanel();

    public Panel getToolPanel() {
        return toolPanel;
    }

    abstract protected Table createTable();

    public Table getTable() {
        return table;
    }

    /**
     * Apply the scaffolding together.
     * @param container the base container for the scaffolding.
     */
    protected void apply(HasWidgets container) {
        container.clear();
        container.add(topPanel);
        container.add(tableBody);

        topPanel.add(infoPanel);
        topPanel.add(toolPanel);
        tableBody.add(table);

        table.addHead(new MaterialWidget(DOM.createElement("thead")));
        table.addBody(new MaterialWidget(DOM.createElement("tbody")));
    }
}
