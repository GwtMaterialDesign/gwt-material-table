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
package gwt.material.design.client.ui.table.cell;

public class FooterColumn<T> {

    private Column<T, ?> column;
    private final FooterValueProvider<T> valueProvider;

    public FooterColumn(FooterValueProvider<T> valueProvider) {
        this.valueProvider = valueProvider;
    }

    public FooterColumn(Column<T, ?> column, FooterValueProvider<T> valueProvider) {
        this.column = column;
        this.valueProvider = valueProvider;
    }

    public void setColumn(Column<T, ?> columnName) {
        this.column = columnName;
    }

    public Column<T, ?> getColumn() {
        return column;
    }

    public FooterValueProvider<T> getValueProvider() {
        return valueProvider;
    }
}
