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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.ui.MaterialIntegerBox;
import gwt.material.design.client.ui.html.Span;

/**
 * Widget for building the page numbers selection - where user can change the page number into a listbox
 *
 * @author kevzlou7979
 */
public class PageNumberSelection extends MaterialWidget implements HasValue<Integer> {

    private final MaterialDataPager<?> pager;
    protected Span pageLabel = new Span("Page");
    protected MaterialIntegerBox pages = new MaterialIntegerBox();

    public PageNumberSelection(MaterialDataPager<?> pager) {
        super(Document.get().createDivElement(), TableCssName.NUM_PAGE_PANEL);
        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // Label
        add(pageLabel);

        // Input
        pages.setMin("1");
        add(pages);

        registerHandler(addValueChangeHandler(valueChangeEvent -> pager.gotoPage(valueChangeEvent.getValue())));
        registerHandler(addKeyDownHandler(keyDownEvent -> {
            if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                pager.gotoPage(pages.getValue());
            }
        }));
    }

    @Override
    public Integer getValue() {
        return pages.getValue();
    }

    @Override
    public void setValue(Integer value) {
    	pages.setValue(value);
    }

    @Override
    public void setValue(Integer value, boolean fireEvents) {
    	pages.setValue(value, fireEvents);
    }

    public void updatePageNumber(int currentPage) {
    	pages.setValue(currentPage, false);
	}

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Integer> valueChangeHandler) {
        return pages.addValueChangeHandler(valueChangeHandler);
    }
}
