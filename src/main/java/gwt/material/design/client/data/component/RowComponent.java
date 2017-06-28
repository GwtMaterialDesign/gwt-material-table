package gwt.material.design.client.data.component;

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


import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.client.ui.table.TableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ben Dol
 */
public class RowComponent<T> extends Component<TableRow> {
    private T data;
    private int index;
    private String category;
    private int leftFrozenColumns, rightFrozenColumns;

    public RowComponent(RowComponent<T> clone) {
        super(clone.getWidget(), clone.isRedraw());
        data = clone.data;
        category = clone.category;
    }

    public RowComponent(T data, String category) {
        this.data = data;
        this.category = category;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getDataCategory() {
        return category;
    }

    public int getLeftFrozenColumns() {
        return leftFrozenColumns;
    }

    public void setLeftFrozenColumns(int leftFrozenColumns) {
        this.leftFrozenColumns = leftFrozenColumns;
    }

    public int getRightFrozenColumns() {
        return rightFrozenColumns;
    }

    public void setRightFrozenColumns(int rightFrozenColumns) {
        this.rightFrozenColumns = rightFrozenColumns;
    }

    public boolean hasFrozenColumns() {
        return hasLeftFrozen() || hasRightFrozen();
    }

    public boolean hasLeftFrozen() {
        return leftFrozenColumns > 0;
    }

    public boolean hasRightFrozen() {
        return rightFrozenColumns > 0;
    }

    @Override
    protected void clearElement() {
        TableRow row = getWidget();
        if(row != null) {
            clearRowExpansion();
        }
        super.clearElement();
    }

    public void clearRowExpansion() {
        JQueryElement next = JQuery.$(getWidget()).next();
        if(next.is("tr.expansion")) {
            next.remove();
        }
    }

    public static <T> List<T> extractData(List<RowComponent<T>> rows) {
        List<T> data = new ArrayList<>();
        for(RowComponent<T> row : rows) {
            if(row != null) {
                data.add(row.getData());
            }
        }
        return data;
    }
}
