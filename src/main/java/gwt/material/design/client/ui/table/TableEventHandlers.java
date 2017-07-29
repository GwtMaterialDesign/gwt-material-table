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


import gwt.material.design.client.ui.table.events.RowExpansion;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.Functions.EventFunc1;
import gwt.material.design.jquery.client.api.Functions.EventFunc2;
import gwt.material.design.jquery.client.api.Functions.EventFunc3;
import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.jquery.client.api.MouseEvent;
import gwt.material.design.jscore.client.api.core.Element;
import gwt.material.design.client.data.SortContext;

import java.util.List;

/**
 * @author Ben Dol
 */
public interface TableEventHandlers<T> {

    /**
     * Remove all registered handlers.
     */
    void removeAllHandlers();

    /**
     * Add a handler that is triggered when select all rows is executed.
     * @param handler the handler consuming: List of models involved, List of row elements involved, selection state.
     */
    void addSelectAllHandler(EventFunc3<List<T>, List<JQueryElement>, Boolean> handler);

    /**
     * Remove the given select all handler.
     */
    void removeSelectAllHandler(EventFunc3<List<T>, List<JQueryElement>, Boolean> handler);

    /**
     * Remove all the select all handlers.
     */
    void removeSelectAllHandlers();

    /**
     * Adda a handler that is triggered when a row is selected.
     * @param handler the handler consuming: Model involved, Row Element involved, selection state.
     */
    void addRowSelectHandler(EventFunc3<T, Element, Boolean> handler);

    /**
     * Remove the given row select handler.
     */
    void removeRowSelectHandler(EventFunc3<T, Element, Boolean> handler);

    /**
     * Remove all the row select handlers.
     */
    void removeRowSelectHandlers();

    /**
     * Add a handler that is triggered when the table is stretched.
     * @param handler the handler consuming: Stretch state.
     */
    void addStretchHandler(EventFunc1<Boolean> handler);

    /**
     * Remove the given stretch handler.
     */
    void removeStretchHandler(EventFunc1<Boolean> handler);

    /**
     * Remove all stretch handler.
     */
    void removeStretchHandlers();

    /**
     * Add a handler that triggers when a row is expanding.
     */
    void addRowExpandHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove the given row expand handler.
     */
    void removeRowExpandHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove all row expand handlers.
     */
    void removeRowExpandHandlers();

    /**
     * Add a handler that triggers when a row has expanded.
     */
    void addRowExpandedHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove the given row expanded handler.
     */
    void removeRowExpandedHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove all row expanded handlers.
     */
    void removeRowExpandedHandlers();

    /**
     * Add a handler that triggers when a row is collapsing.
     */
    void addRowCollapseHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove the given row collapse handler.
     */
    void removeRowCollapseHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove all row collapse handlers.
     */
    void removeRowCollapseHandlers();

    /**
     * Add a handler that triggers when a row has collapsed.
     */
    void addRowCollapsedHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove the given row collapsed handler.
     */
    void removeRowCollapsedHandler(EventFunc1<RowExpansion> handler);

    /**
     * Remove all row collapsed handlers.
     */
    void removeRowCollapsedHandlers();

    /**
     * Add a handler that triggers when the row count changes.
     * @param handler the handler consuming: New count and is exact count.
     */
    void addRowCountChangeHandler(EventFunc2<Integer, Boolean> handler);

    /**
     * Remove the given row count change handler.
     */
    void removeRowCountChangeHandler(EventFunc2<Integer, Boolean> handler);

    /**
     * Remove all row count change handlers.
     */
    void removeRowCountChangeHandlers();

    /**
     * Add a handler that triggers when a row is right clicked.
     * @param handler the handler consuming: Model, Element
     */
    void addRowContextMenuHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove the given row context menu handler.
     */
    void removeRowContextMenuHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove all row context menu handlers.
     */
    void removeRowContextMenuHandlers();

    /**
     * Add a handler that triggers when a row is double clicked.
     * @param handler the handler consuming: Model, Element
     */
    void addRowDoubleClickHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove the given row double click handler.
     */
    void removeRowDoubleClickHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove all row double click handlers.
     */
    void removeRowDoubleClickHandlers();

    /**
     * Add a handler that triggers when a row is long pressed.
     * @param handler the handler consuming: Model, Element
     */
    void addRowLongPressHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove the given row long pressed handler.
     */
    void removeRowLongPressHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove all row long pressed handlers.
     */
    void removeRowLongPressHandlers();

    /**
     * Add a handler that triggers when a row is short pressed.
     * @param handler the handler consuming: Model, Element
     */
    void addRowShortPressHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove the given row short pressed handler.
     */
    void removeRowShortPressHandler(EventFunc3<MouseEvent, T, Element> handler);

    /**
     * Remove all row short pressed handlers.
     */
    void removeRowShortPressHandlers();

    /**
     * Add a handler that triggers when a column is sorted.
     * @param handler the handler consuming: Sort context and column index.
     */
    void addSortColumnHandler(EventFunc2<SortContext, Integer> handler);

    /**
     * Remove the given sort column handler.
     */
    void removeSortColumnHandler(EventFunc2<SortContext, Integer> handler);

    /**
     * Remove all sort column handlers.
     */
    void removeSortColumnHandlers();

    /**
     * Add a handler that triggers when a category is opened.
     * @param handler the handler consuming: Category name.
     */
    void addCategoryOpenedHandler(EventFunc1<String> handler);

    /**
     * Remove the given category opened handler.
     */
    void removeCategoryOpenedHandler(EventFunc1<String> handler);

    /**
     * Remove all category opened handlers.
     */
    void removeCategoryOpenedHandlers();

    /**
     * Add a handler that triggers when a category is closed.
     * @param handler the handler consuming: Category name.
     */
    void addCategoryClosedHandler(EventFunc1<String> handler);

    /**
     * Remove the given category closed handler.
     */
    void removeCategoryClosedHandler(EventFunc1<String> handler);

    /**
     * Remove all category closed handlers.
     */
    void removeCategoryClosedHandlers();

    /**
     * Add a handler that triggers when all the components have rendered,
     * this can fire multiple times depending on the table settings.<br><br>
     * Also see {@link #addRenderedHandler(Functions.EventFunc)}.
     */
    void addComponentsRenderedHandler(Functions.EventFunc handler);

    /**
     * Remove the given components rendered handler.
     */
    void removeComponentsRenderedHandler(Functions.EventFunc handler);

    /**
     * Remove all components rendered handlers.
     */
    void removeComponentsRenderedHandlers();

    /**
     * Add a handler that triggers when all the row data has rendered after calling
     * {@link AbstractDataTable#setRowData(int, List)}.<br>
     * This will only fire once per call even if the data is re-rendered in the cases of sorting, etc.
     */
    void addRenderedHandler(Functions.EventFunc handler);

    /**
     * Remove the given rendered handler.
     */
    void removeRenderedHandler(Functions.EventFunc handler);

    /**
     * Remove all rendered handlers.
     */
    void removeRenderedHandlers();
}
