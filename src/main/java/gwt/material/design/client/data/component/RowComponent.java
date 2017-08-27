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
package gwt.material.design.client.data.component;

import com.google.gwt.user.client.ui.HasEnabled;
import gwt.material.design.client.data.DataView;
import gwt.material.design.jquery.client.api.JQuery;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.client.ui.table.TableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ben Dol
 */
public class RowComponent<T> extends Component<TableRow> implements HasEnabled {
    private T data;
    private int index;
    private final String categoryName;
    private final DataView dataView;

    public RowComponent(RowComponent<T> clone) {
        super(clone.getWidget(), clone.isRedraw());
        addAll(clone.getChildren());
        data = clone.data;
        index = clone.index;
        categoryName = clone.categoryName;
        dataView = clone.dataView;
    }

    public RowComponent(T data, DataView dataView, String categoryName) {
        this.data = data;
        this.dataView = dataView;
        this.categoryName = categoryName;
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

    public DataView getDataView() {
        return dataView;
    }

    public CategoryComponent getCategory() {
        return dataView.getCategory(categoryName);
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public boolean isEnabled() {
        return getWidget().isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        getWidget().setEnabled(enabled);
    }

    @Override
    protected void clearWidget() {
        TableRow row = getWidget();
        if(row != null) {
            clearRowExpansion();
        }
        super.clearWidget();
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

    @Override
    public String toString() {
        return "RowComponent{" +
                "data=" + data +
                ", index=" + index +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
