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
package gwt.material.design.client.ui.pager;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.html.Span;

/**
 * Widget for building the page numbers selection - where user can change the page number into a listbox
 *
 * @author kevzlou7979
 */
public class PageNumberSelection extends MaterialWidget implements HasValue<Integer> {

    private final MaterialDataPager pager;
    protected Span pageLabel = new Span("Page");
    protected MaterialListValueBox<Integer> listPages = new MaterialListValueBox<>();

    public PageNumberSelection(MaterialDataPager pager) {
        super(Document.get().createDivElement(), TableCssName.NUM_PAGE_PANEL);
        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        setGrid("s12 m4 l3");
        setOffset("l3");


        add(listPages);
        add(pageLabel);

        registerHandler(listPages.addValueChangeHandler(event -> pager.gotoPage(event.getValue())));
    }

    /**
     * Re populate the total number of pages
     */
    public void updatePageNumber(int totalRows, int limit, int currentPage) {
        listPages.clear();
        int pages = (pager.isExcess()) ? (totalRows / limit) + 1 : totalRows / limit;
        for (int i = 1; i <= pages; i++) {
            listPages.addItem(i);
        }
        listPages.setSelectedIndex(currentPage - 1);
    }

    @Override
    public Integer getValue() {
        return listPages.getValue();
    }

    @Override
    public void setValue(Integer value) {
        listPages.setValue(value);
    }

    @Override
    public void setValue(Integer value, boolean fireEvents) {
        listPages.setValue(value, fireEvents);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> valueChangeHandler) {
        return listPages.addValueChangeHandler(valueChangeHandler);
    }

    public void setSelectedIndex(int index) {
        listPages.setSelectedIndex(index);
    }

    public int getSelectedIndex() {
        return listPages.getSelectedIndex();
    }
}
