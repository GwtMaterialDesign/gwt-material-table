package gwt.material.design.client.data;

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


import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import gwt.material.design.client.base.constants.StyleName;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.HideOn;
import gwt.material.design.client.constants.IconSize;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.TextAlign;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Component;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.table.TableData;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableRow;
import gwt.material.design.client.ui.table.TableSubHeader;
import gwt.material.design.client.ui.table.cell.Column;
import gwt.material.design.client.ui.table.cell.WidgetColumn;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Base Component Renderer used by {@link AbstractDataView}.
 * <br><br>
 * This can be extended upon or can be replaced all together.
 *
 * @author Ben Dol
 */
public class BaseRenderer<T> implements Renderer<T> {

    private final Logger logger = Logger.getLogger(BaseRenderer.class.getName());

    // Configurations
    private int calculatedRowHeight = 55;
    private int expectedRowHeight = calculatedRowHeight;

    private IconType sortAscIcon = IconType.ARROW_UPWARD;
    private IconType sortDescIcon = IconType.ARROW_DOWNWARD;
    private IconSize sortIconSize = IconSize.TINY;

    @Override
    public TableRow drawRow(DataView<T> dataView, RowComponent<T> rowComponent, Object valueKey,
                            List<Column<T, ?>> columns, boolean redraw) {
        T data = rowComponent.getData();
        TableRow row = rowComponent.getWidget();
        if(row == null) {
            // Create a new row element
            row = new TableRow();
            row.getElement().getStyle().setDisplay(Display.NONE);
            row.getElement().getStyle().setProperty("height", getExpectedRowHeight() + "px");
            row.getElement().getStyle().setProperty("maxHeight", getExpectedRowHeight() + "px");
            row.getElement().getStyle().setProperty("minHeight", getExpectedRowHeight() + "px");
            row.setStyleName(TableCssName.DATA_ROW);
            rowComponent.setWidget(row);

            if(!dataView.getSelectionType().equals(SelectionType.NONE)) {
                row.add(drawSelectionCell());
            }

            // Build the columns
            int colOffset = dataView.getColumnOffset();
            for(int c = 0; c < columns.size(); c++) {
                int colIndex = c + colOffset;
                Context context = new Context(rowComponent.getIndex(), colIndex, valueKey);
                drawColumn(row, context, data, columns.get(c), colIndex, dataView.isHeaderVisible(colIndex));
            }
        } else {
            if(redraw || rowComponent.isRedraw()) {
                // Rebuild the columns
                int colOffset = dataView.getColumnOffset();
                for(int c = 0; c < columns.size(); c++) {
                    int colIndex = c + colOffset;
                    Context context = new Context(rowComponent.getIndex(), colIndex, valueKey);
                    drawColumn(row, context, rowComponent.getData(), columns.get(c), colIndex,
                        dataView.isHeaderVisible(colIndex));
                }
                rowComponent.setRedraw(false);
            }
        }

        if(dataView.isUseRowExpansion()) {
            if(!row.hasExpansionColumn()) {
                TableData expand = new TableData();
                expand.setId("colex");
                MaterialIcon expandIcon = new MaterialIcon();
                expandIcon.setId("expand");
                expandIcon.setWidth("100%");
                expandIcon.setIconType(IconType.KEYBOARD_ARROW_DOWN);
                expandIcon.setWaves(WavesType.LIGHT);
                expandIcon.getElement().getStyle().setCursor(Cursor.POINTER);
                expand.add(expandIcon);
                row.add(expand);
            }
        } else if(row.hasExpansionColumn()) {
            row.removeExpansionColumn();
        }

        Scheduler.get().scheduleDeferred(() -> {
            calculateRowHeight(rowComponent);
        });
        return row;
    }

    @Override
    public TableSubHeader drawCategory(CategoryComponent category) {
        if(category != null) {
            TableSubHeader subHeader = category.getWidget();
            if(subHeader == null) {
                subHeader = category.render();
            }
            return subHeader;
        }

        // No subheader was added
        return null;
    }

    @Override
    public TableRow drawCustom(Component<?> component) {
        return new TableRow();
    }

    @Override
    public TableData drawSelectionCell() {
        TableData checkBox = new TableData();
        checkBox.setId("col0");
        checkBox.setStyleName(TableCssName.SELECTION);
        new MaterialCheckBox(checkBox.getElement());
        return checkBox;
    }

    @Override
    @SuppressWarnings("unchecked")
    public TableData drawColumn(TableRow row, Context context, T rowValue, Column<T, ?> column,
                                int beforeIndex, boolean visible) {
        TableData data = null;
        if(row != null && rowValue != null) {
            data = row.getColumn(beforeIndex);
            if(data == null) {
                data = new TableData();
                row.insert(data, beforeIndex);
            } else {
                data.clear();
            }

            Div wrapper = new Div();

            // Render the column cell
            if(column instanceof WidgetColumn) {
                wrapper.setStyleName(TableCssName.WIDGET_CELL);
                wrapper.add(((WidgetColumn) column).render(context, rowValue));
            } else {
                SafeHtmlBuilder sb = new SafeHtmlBuilder();
                column.render(context, rowValue, sb);
                wrapper.getElement().setInnerHTML(sb.toSafeHtml().asString());
                wrapper.setStyleName(TableCssName.CELL);
            }

            data.add(wrapper);

            data.setId("col" + beforeIndex);
            data.setDataTitle(column.getName());
            HideOn hideOn = column.getHideOn();
            if(hideOn != null) {
                data.setHideOn(hideOn);
            }
            TextAlign textAlign = column.getTextAlign();
            if(textAlign != null) {
                data.setTextAlign(textAlign);
            }
            if(column.isNumeric()) {
                data.addStyleName(TableCssName.NUMERIC);
            }

            // Apply the style properties
            Style style = data.getElement().getStyle();
            Map<StyleName, String> styleProps = column.getStyleProperties();
            if(styleProps != null) {
                styleProps.forEach((s, v) -> style.setProperty(s.styleName(), v));
            }

            // Hide if defined as not visible
            // This can be the case when a header is toggled off.
            if(!visible) {
                data.$this().hide();
            }
        }
        return data;
    }

    @Override
    public TableHeader drawColumnHeader(Column<T, ?> column, String header, int index) {
        MaterialIcon sortIcon = new MaterialIcon();
        sortIcon.setIconSize(sortIconSize);

        TableHeader th = new TableHeader(sortIcon);
        th.setId("col" + index);
        th.setHeader(header);
        HideOn hideOn = column.getHideOn();
        if(hideOn != null) {
            th.setHideOn(hideOn);
        }
        TextAlign textAlign = column.getTextAlign();
        if(textAlign != null) {
            th.setTextAlign(textAlign);
        }
        if(column.isNumeric()) {
            th.addStyleName(TableCssName.NUMERIC);
        }

        // Apply the style properties
        Style style = th.getElement().getStyle();
        Map<StyleName, String> styleProps = column.getStyleProperties();
        if(styleProps != null) {
            styleProps.forEach((s, v) -> style.setProperty(s.styleName(), v));
        }

        // Set the headers width
        String width = column.getWidth();
        if(width != null) {
            th.setWidth(width);
        }
        th.setVisible(true);
        return th;
    }

    @Override
    public void drawSortIcon(TableHeader th, SortContext<T> sortContext) {
        if(sortContext.getSortDir().equals(SortDir.ASC)) {
            th.getSortIcon().setIconType(sortAscIcon);
        } else {
            th.getSortIcon().setIconType(sortDescIcon);
        }
    }

    @Override
    public int getExpectedRowHeight() {
        return expectedRowHeight;
    }

    @Override
    public void setExpectedRowHeight(int expectedRowHeight) {
        if(expectedRowHeight < 33) {
            logger.warning("Expected row height must be 33px or higher, setting row height to 33px.");
            this.expectedRowHeight = 33;
        } else {
            this.expectedRowHeight = expectedRowHeight;
        }
    }

    @Override
    public void calculateRowHeight(RowComponent<T> row) {
        TableRow element = row.getWidget();
        if(element != null) {
            int rowHeight = element.$this().outerHeight(true);
            if (rowHeight > 0 && rowHeight != calculatedRowHeight) {
                calculatedRowHeight = rowHeight;
            }
        }
    }

    @Override
    public int getCalculatedRowHeight() {
        return calculatedRowHeight;
    }
}
