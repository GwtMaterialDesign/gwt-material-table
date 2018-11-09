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

/**
 * A column that displays its contents with a {@link TextCell} and does not make
 * use of view data.
 *
 * @param <T> the row type
 * @author Ben Dol
 */
public class TextColumn<T> extends Column<T, String> {

    public TextColumn() {
        super(new TextCell());
    }

    public TextColumn(Cell<String> cell) {
        super(cell);
    }

    public TextColumn(Cell<String> cell, String defaultValue) {
        super(cell, defaultValue);
    }

    public TextColumn(Cell<String> cell, Value<T, String> delegate) {
        super(cell, delegate);
    }

    public TextColumn(Cell<String> cell, Value<T, String> delegate, String defaultValue) {
        super(cell, delegate, defaultValue);
    }
}