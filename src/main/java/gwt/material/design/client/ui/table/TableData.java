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

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import gwt.material.design.client.base.MaterialWidget;

public class TableData extends MaterialWidget {

    public TableData(Element element) {
        super(element);
    }

    public TableData() {
        super(DOM.createTD());
    }

    public TableData(Element element, String classNames) {
        super(element, classNames);
    }

    public void setDataTitle(String title) {
        setDataAttribute("data-title", title);
    }

    public String getDataTitle() {
        return getDataAttribute("data-title");
    }
}
