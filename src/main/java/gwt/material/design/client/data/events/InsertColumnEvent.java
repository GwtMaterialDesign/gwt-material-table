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
package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import gwt.material.design.client.ui.table.cell.Column;

public class InsertColumnEvent<T> extends GwtEvent<InsertColumnHandler> {

    public static final Type<InsertColumnHandler> TYPE = new Type<>();

    private final int beforeIndex;
    private final Column<T, ?> column;
    private final String header;

    public InsertColumnEvent(int beforeIndex, Column<T, ?> column, String header) {
        this.beforeIndex = beforeIndex;
        this.column = column;
        this.header = header;
    }

    public static <T> void fire(HasHandlers source, int beforeIndex, Column<T, ?> col, String header) {
        source.fireEvent(new InsertColumnEvent<T>(beforeIndex, col, header));
    }

    public int getBeforeIndex() {
        return beforeIndex;
    }

    public Column<T, ?> getColumn() {
        return column;
    }

    public String getHeader() {
        return header;
    }

    @Override
    public Type<InsertColumnHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(InsertColumnHandler handler) {
        handler.onInsertColumn(this);
    }
}
