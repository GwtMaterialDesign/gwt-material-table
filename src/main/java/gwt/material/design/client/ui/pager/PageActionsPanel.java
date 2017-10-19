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
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Span;

/**
 * Widget for building the action panel - contains a page detail including the arrow next / previous icons.
 *
 * @author kevzlou7979
 */
public class PageActionsPanel extends MaterialWidget {

    private final MaterialDataPager pager;
    protected Span actionLabel = new Span();
    protected MaterialIcon iconNext = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
    protected MaterialIcon iconPrev = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);

    public PageActionsPanel(MaterialDataPager pager) {
        super(Document.get().createDivElement(), TableCssName.ACTION_PAGE_PANEL);
        setGrid("s12 m4 l3");
        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        // Add the next button
        iconNext.setWaves(WavesType.DEFAULT);
        iconNext.setCircle(true);
        add(iconNext);

        // Add the action label
        add(actionLabel);

        // Add the previous button
        iconPrev.setWaves(WavesType.DEFAULT);
        iconPrev.setCircle(true);
        add(iconPrev);

        // Register the handlers
        registerHandler(iconNext.addClickHandler(clickEvent -> pager.next()));
        registerHandler(iconPrev.addClickHandler(clickEvent -> pager.previous()));
    }

    public Span getActionLabel() {
        return actionLabel;
    }

    public MaterialIcon getIconNext() {
        return iconNext;
    }

    public MaterialIcon getIconPrev() {
        return iconPrev;
    }
}
