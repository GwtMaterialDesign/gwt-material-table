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
package gwt.material.design.client.ui.table;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import gwt.material.design.client.ui.table.cell.ColumnFormatProvider;

public class GlobalConfig {

    private ColumnFormatProvider defaultFormatProvider = new ColumnFormatProvider();
    private String defaultBlankPlaceholder = "";
    private Boolean helpEnabled = true;
    private Boolean columnTruncate = false;
    private Boolean slidePagerEnabled = false;
    private Integer columnMaxWidth = 400;

    public GlobalConfig() {
        defaultFormatProvider.setDateFormat(DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_FULL));
        defaultFormatProvider.setIntegerFormat(NumberFormat.getFormat("#"));
        defaultFormatProvider.setLongFormat(NumberFormat.getFormat("#"));
        defaultFormatProvider.setDoubleFormat(NumberFormat.getDecimalFormat());
        defaultFormatProvider.setFloatFormat(NumberFormat.getDecimalFormat());
        defaultFormatProvider.setBigDecimalFormat(NumberFormat.getDecimalFormat());
        defaultFormatProvider.setShortFormat(NumberFormat.getFormat("#"));
    }

    public ColumnFormatProvider getFormatProvider() {
        return defaultFormatProvider;
    }

    public void setDefaultFormatProvider(ColumnFormatProvider defaultFormatProvider) {
        this.defaultFormatProvider = defaultFormatProvider;
    }

    public String getBlankPlaceholder() {
        return defaultBlankPlaceholder;
    }

    public void setDefaultBlankPlaceholder(String defaultBlankPlaceholder) {
        this.defaultBlankPlaceholder = defaultBlankPlaceholder;
    }

    public Boolean isHelpEnabled() {
        return helpEnabled;
    }

    public void setHelpEnabled(Boolean helpEnabled) {
        this.helpEnabled = helpEnabled;
    }

    public void setColumnTruncate(Boolean truncate, Integer maxWidth) {
        this.columnTruncate = truncate;
        this.columnMaxWidth = maxWidth;
    }

    public void setColumnTruncate(Boolean truncate) {
        this.columnTruncate = truncate;
    }

    public Boolean isColumnTruncate() {
        return columnTruncate;
    }

    public Integer getColumnMaxWidth() {
        return columnMaxWidth;
    }

    public Boolean isSlidePagerEnabled() {
        return slidePagerEnabled;
    }

    public void setSlidePagerEnabled(Boolean slidePagerEnabled) {
        this.slidePagerEnabled = slidePagerEnabled;
    }
}
