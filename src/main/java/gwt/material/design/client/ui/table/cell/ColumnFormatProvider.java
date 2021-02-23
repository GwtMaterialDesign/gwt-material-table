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

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import gwt.material.design.client.ui.table.MaterialDataTable;

public class ColumnFormatProvider {

    private DateTimeFormat dateFormat;
    private NumberFormat integerFormat;
    private NumberFormat longFormat;
    private NumberFormat doubleFormat;
    private NumberFormat floatFormat;
    private NumberFormat bigDecimalFormat;
    private NumberFormat shortFormat;

    public DateTimeFormat getDateFormat() {
        if (dateFormat == null) {
            dateFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getDateFormat();
        }
        return dateFormat;
    }

    public ColumnFormatProvider setDateFormat(DateTimeFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public NumberFormat getIntegerFormat() {
        if (integerFormat == null) {
            integerFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getIntegerFormat();
        }
        return integerFormat;
    }

    public ColumnFormatProvider setIntegerFormat(NumberFormat integerFormat) {
        this.integerFormat = integerFormat;
        return this;
    }

    public NumberFormat getLongFormat() {
        if (longFormat == null) {
            longFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getLongFormat();
        }
        return longFormat;
    }

    public ColumnFormatProvider setLongFormat(NumberFormat longFormat) {
        this.longFormat = longFormat;
        return this;
    }

    public NumberFormat getDoubleFormat() {
        if (doubleFormat == null) {
            doubleFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getDoubleFormat();
        }
        return doubleFormat;
    }

    public ColumnFormatProvider setDoubleFormat(NumberFormat doubleFormat) {
        this.doubleFormat = doubleFormat;
        return this;
    }

    public NumberFormat getFloatFormat() {
        if (floatFormat == null) {
            floatFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getFloatFormat();
        }
        return floatFormat;
    }

    public ColumnFormatProvider setFloatFormat(NumberFormat floatFormat) {
        this.floatFormat = floatFormat;
        return this;
    }

    public NumberFormat getBigDecimalFormat() {
        if (bigDecimalFormat == null) {
            bigDecimalFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getBigDecimalFormat();
        }
        return bigDecimalFormat;
    }

    public ColumnFormatProvider setBigDecimalFormat(NumberFormat bigDecimalFormat) {
        this.bigDecimalFormat = bigDecimalFormat;
        return this;
    }

    public NumberFormat getShortFormat() {
        if (shortFormat == null) {
            shortFormat = MaterialDataTable.getGlobals().getDefaultFormatProvider().getShortFormat();
        }
        return shortFormat;
    }

    public ColumnFormatProvider setShortFormat(NumberFormat shortFormat) {
        this.shortFormat = shortFormat;
        return this;
    }
}
