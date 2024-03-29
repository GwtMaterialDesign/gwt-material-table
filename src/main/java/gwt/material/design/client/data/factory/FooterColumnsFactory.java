/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2021 GwtMaterialDesign
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
package gwt.material.design.client.data.factory;

import com.google.gwt.core.client.GWT;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.FooterColumn;
import gwt.material.design.client.ui.table.cell.FooterValueProvider;

import java.util.HashMap;
import java.util.Map;

public class FooterColumnsFactory<T>  {

    private Map<String, FooterValueProvider<T>> map = new HashMap<>();

    public void addFooterColumn(FooterColumn<T> footerColumn) {
        if (footerColumn != null) {
            Column<T, ?> column = footerColumn.getColumn();
            FooterValueProvider<T> valueProvider = footerColumn.getValueProvider();
            if (column != null && column.name() != null && valueProvider != null) {
                map.put(column.name(), valueProvider);
            }
        } else {
            GWT.log("Footer Column must not be null", new NullPointerException());
        }
    }

    public FooterValueProvider<T> get(String columnName) {
        return map.get(columnName);
    }

    public Map<String, FooterValueProvider<T>> getMap() {
        return map;
    }

    public void clear() {
        map.clear();
    }
}
