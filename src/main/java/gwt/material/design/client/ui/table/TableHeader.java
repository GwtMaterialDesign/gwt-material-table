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

import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.user.client.DOM;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.Display;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

/**
 * @author Ben Dol
 */
public class TableHeader extends TableData {

    private Span headerLbl;
    private MaterialIcon sortIcon;
    private Div headerWrap = new Div();

    public TableHeader() {
        super(DOM.createTH());
    }

    public TableHeader(String classNames) {
        super(DOM.createTH(), classNames);
    }

    public TableHeader(MaterialIcon sortIcon) {
        this();
        setSortIcon(sortIcon);
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        headerWrap.setHeight("100%");
        headerWrap.setDisplay(Display.FLEX);
        add(headerWrap);
    }

    public void setHeader(String header) {
        if(headerLbl == null) {
            headerLbl = new Span(header);
            headerLbl.setStyleName(TableCssName.TABLE_HEADER);
            headerWrap.add(headerLbl);
        } else {
            headerLbl.setText(header);
        }
    }

    public String getHeader() {
        return headerLbl != null ? headerLbl.getText() : "";
    }

    public MaterialIcon getSortIcon() {
        return sortIcon;
    }

    public void setSortIcon(MaterialIcon sortIcon) {
        removeSortIcon();
        this.sortIcon = sortIcon;

        if(sortIcon != null) {
            IconSize iconSize = this.sortIcon.getIconSize();
            if(iconSize == null) {
                this.sortIcon.setIconSize(IconSize.SMALL);
            }
            this.sortIcon.getElement().getStyle().setFloat(Float.LEFT);
            headerWrap.insert(this.sortIcon, 0);
        }
    }

    public void removeSortIcon() {
        if(sortIcon != null) {
            sortIcon.setInnerText("");
        }
    }
}
