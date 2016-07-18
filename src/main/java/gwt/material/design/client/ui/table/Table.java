package gwt.material.design.client.ui.table;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
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


import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.js.JsTableElement;

/**
 * @author Ben Dol
 */
public class Table extends MaterialWidget {

    private JsTableElement element;
    private MaterialWidget thead;
    private MaterialWidget tbody;

    public Table() {
        this(DOM.createTable());
    }

    public Table(JQueryElement element) {
        this(element.asElement());
    }

    public Table(Element element) {
        super(element);

        this.element = JsTableElement.$(getElement());
    }

    public JsTableElement getJsElement() {
        return element;
    }

    public void addHead(MaterialWidget thead) {
        this.thead = thead;
        add(thead);
    }

    public MaterialWidget getHead() {
        return thead;
    }

    public void addBody(MaterialWidget tbody) {
        this.tbody = tbody;
        add(tbody);
    }

    public MaterialWidget getBody() {
        return tbody;
    }
}
