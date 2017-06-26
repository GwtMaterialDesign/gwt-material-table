package gwt.material.design.client.ui.table;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
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


import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.component.CategoryComponent;

public class TableRow extends MaterialWidget {

    protected CategoryComponent category;

    public TableRow() {
        this(DOM.createTR());
    }

    public TableRow(Element element) {
        super(element);
    }

    public TableRow(CategoryComponent category) {
        this();
        setDataCategory(category);
    }

    public void add(TableData tableData) {
        super.add(tableData);
    }

    public void insert(TableData child, int beforeIndex) {
        super.insert(child, beforeIndex);
    }

    public void clearColumnsFromIndex(int colIndex) {
        for(int i = colIndex; i < getChildren().size(); i++) {
            TableData column = getColumn(i);
            if(column != null) {
                column.clear();
            }
        }
    }

    public TableData getColumn(int index) {
        try {
            return (TableData) getWidget(index);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public void setDataCategory(CategoryComponent category) {
        this.category = category;
        setDataAttribute("data-category", category.getCategory());
    }

    public CategoryComponent getDataCategory() {
        return category;
    }

    public void copy(TableRow tableRow) {
        if(tableRow != null) {
            this.category = tableRow.category;
        }
    }

    public boolean hasExpansionColumn() {
        for(Widget column : getChildren()) {
            if(column.getElement().getId().equals("colex")) {
                return true;
            }
        }
        return false;
    }

    public void removeExpansionColumn() {
        for(Widget column : getChildren()) {
            if(column.getElement().getId().equals("colex")) {
                column.removeFromParent();
            }
        }
    }
}
