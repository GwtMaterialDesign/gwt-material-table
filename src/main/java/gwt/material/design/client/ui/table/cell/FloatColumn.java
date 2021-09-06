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
package gwt.material.design.client.ui.table.cell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.i18n.client.NumberFormat;

/**
 * A column that displays its contents with a {@link NumberCell} and does not make
 * use of view data.
 *
 * @param <T> the row type
 * @author Ben Dol
 */
public class FloatColumn<T> extends NumberColumn<T, Float> {

    public FloatColumn() {
    }

    public FloatColumn(NumberFormat format) {
        super(format);
    }

    public FloatColumn(Cell<Float> cell) {
        super(cell);
    }

    public FloatColumn(Cell<Float> cell, Float defaultValue) {
        super(cell, defaultValue);
    }

    public FloatColumn(Cell<Float> cell, Value<T, Float> delegate) {
        super(cell, delegate);
    }

    public FloatColumn(Cell<Float> cell, Value<T, Float> delegate, Float defaultValue) {
        super(cell, delegate, defaultValue);
    }

    @Override
    public NumberFormat getDefaultFormat() {
        return getDataView().getFormatProvider().getFloatFormat();
    }
}
