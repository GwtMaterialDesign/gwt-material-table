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
package gwt.material.design.client.data;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableRow;
import gwt.material.design.client.ui.table.TableSubHeader;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.FrozenSide;

import java.util.List;

/**
 * Renderer interface for defining data view composition.
 * @param <T>
 */
public interface Renderer<T> {
    TableRow drawRow(DataView<T> dataView, RowComponent<T> rowComponent, Object valueKey, List<Column<T, ?>> columns, boolean redraw);

    TableSubHeader drawCategory(CategoryComponent category, int columnCount);

    TableRow drawCustom(Component<?> component);

    TableData drawSelectionCell();

    MaterialWidget drawColumn(TableRow row, Context context, T rowValue, Column<T, ?> column, int beforeIndex, boolean visible);

    TableHeader drawColumnHeader(Widget container, Column<T, ?> column, String header, int index);

    void drawSortIcon(TableHeader th, SortContext<T> sortContext);

    void drawColumnFreeze(TableData td, RowComponent<T> rowComponent, TableHeader header, Column<T, ?> column, FrozenSide side);

    int getExpectedRowHeight();

    void setExpectedRowHeight(int expectedRowHeight);

    void calculateRowHeight(RowComponent<T> row);

    int getCalculatedRowHeight();

    void copy(Renderer<T> renderer);

    IconType getSortAscIcon();

    void setSortAscIcon(IconType sortAscIcon);

    IconType getSortDescIcon();

    void setSortDescIcon(IconType sortDescIcon);

    IconSize getSortIconSize();

    void setSortIconSize(IconSize sortIconSize);

    void setVisibleHeaderIndexes(List<Integer> visibleHeaders);
}
