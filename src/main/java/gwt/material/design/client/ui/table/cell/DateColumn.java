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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.ui.table.MaterialDataTable;

import java.util.Date;

/**
 * A column that displays its contents with a {@link DateCell} and does not make
 * use of view data.
 *
 * @param <T> the row type
 * @author Ben Dol
 */
public class DateColumn<T> extends Column<T, Date> {

    protected DateTimeFormat format;

    public DateColumn() {
        super(new DateCell());
    }

    public DateColumn(DateTimeFormat format) {
        this();
        this.format = format;
    }

    public DateColumn(Cell<Date> cell) {
        super(cell);
    }

    public DateColumn(Cell<Date> cell, Date defaultValue) {
        super(cell, defaultValue);
    }

    public DateColumn(Cell<Date> cell, Value<T, Date> delegate) {
        super(cell, delegate);
    }

    public DateColumn(Cell<Date> cell, Value<T, Date> delegate, Date defaultValue) {
        super(cell, delegate, defaultValue);
    }

    public DateColumn<T> format(DateTimeFormat format) {
        this.format = format;
        return this;
    }

    @Override
    public Column<T, Date> render(Cell.Context context, T object, SafeHtmlBuilder sb) {
        if (cell instanceof DateCell) {
            DateCell dateCell = (DateCell) cell;
            dateCell.setFormat(getFormat());
        }
        super.render(context, object, sb);
        return this;
    }

    public DateTimeFormat getFormat() {
        if (format == null) {
            format = MaterialDataTable.getGlobalFormatProvider().getDateFormat();
        }
        return format;
    }
}
