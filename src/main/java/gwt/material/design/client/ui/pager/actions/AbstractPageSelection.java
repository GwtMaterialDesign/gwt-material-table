/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2018 GwtMaterialDesign
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
package gwt.material.design.client.ui.pager.actions;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import gwt.material.design.client.base.AbstractValueWidget;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.pager.MaterialDataPager;

/**
 * An abstract implementation of {@link PageSelection}, please check also {@link PageNumberBox} and {@link PageListBox}.
 *
 * @author kevzlou7979
 */
public abstract class AbstractPageSelection<T extends AbstractValueWidget<Integer>> extends MaterialWidget
        implements PageSelection {

    protected MaterialDataPager<?> pager;
    protected Span pageLabel = new Span();

    public AbstractPageSelection() {
        super(Document.get().createDivElement(), TableCssName.NUM_PAGE_PANEL);
        setLabel("Page");
    }

    public AbstractPageSelection(MaterialDataPager<?> pager) {
        this();
        setPager(pager);
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        load();
    }

    @Override
    public void load() {
        add(pageLabel);
        add(getComponent());
        registerHandler(addValueChangeHandler(valueChangeEvent -> pager.gotoPage(valueChangeEvent.getValue())));
        setup();
    }

    protected abstract void setup();

    public abstract T getComponent();

    @Override
    public Integer getValue() {
        return getComponent().getValue();
    }

    @Override
    public void setValue(Integer value) {
        getComponent().setValue(value);
    }

    @Override
    public void setValue(Integer value, boolean fireEvents) {
        getComponent().setValue(value, fireEvents);
    }

    public void updatePageNumber(int currentPage) {
        getComponent().setValue(currentPage, false);
    }

    @Override
    public void clearPageNumber() {
        getComponent().clear();
    }

    @Override
    public void setPager(MaterialDataPager<?> pager) {
        this.pager = pager;
    }

    @Override
    public void setLabel(String label) {
        pageLabel.setText(label);
    }

    @Override
    public void updateTotalPages(int totalPages) {
        // Do nothing for now
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> valueChangeHandler) {
        return getComponent().addValueChangeHandler(valueChangeHandler);
    }
}
