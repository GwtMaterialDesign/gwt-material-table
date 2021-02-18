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

//TOD: ColumnFormatProvider
public class ColumnFormatter {

    private DateTimeFormat dateFormat;
    private NumberFormat integerFormat;
    private NumberFormat longFormat;
    private NumberFormat doubleFormat;
    private NumberFormat floatFormat;
    private NumberFormat bigDecimalFormat;
    private NumberFormat shortFormat;

    public DateTimeFormat getDateFormat() {
        //TODO: move the DateColumn Default Logic (i.e DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL))
        return dateFormat;
    }

    public ColumnFormatter setDateFormat(DateTimeFormat dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public NumberFormat getIntegerFormat() {
        return integerFormat;
    }

    public ColumnFormatter setIntegerFormat(NumberFormat integerFormat) {
        this.integerFormat = integerFormat;
        return this;
    }

    public NumberFormat getLongFormat() {
        return longFormat;
    }

    public ColumnFormatter setLongFormat(NumberFormat longFormat) {
        this.longFormat = longFormat;
        return this;
    }

    public NumberFormat getDoubleFormat() {
        return doubleFormat;
    }

    public ColumnFormatter setDoubleFormat(NumberFormat doubleFormat) {
        this.doubleFormat = doubleFormat;
        return this;
    }

    public NumberFormat getFloatFormat() {
        return floatFormat;
    }

    public ColumnFormatter setFloatFormat(NumberFormat floatFormat) {
        this.floatFormat = floatFormat;
        return this;
    }

    public NumberFormat getBigDecimalFormat() {
        return bigDecimalFormat;
    }

    public ColumnFormatter setBigDecimalFormat(NumberFormat bigDecimalFormat) {
        this.bigDecimalFormat = bigDecimalFormat;
        return this;
    }

    public NumberFormat getShortFormat() {
        return shortFormat;
    }

    public ColumnFormatter setShortFormat(NumberFormat shortFormat) {
        this.shortFormat = shortFormat;
        return this;
    }
}
