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
package gwt.material.design.client.ui.table;

import com.google.gwt.dom.client.Style;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.ui.html.Div;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * Y scroll bar emulation for {@link gwt.material.design.client.data.infinite.InfiniteDataView}.
 */
public class YScrollPanel extends Div {

    private String height;

    private Div scrollBar = new Div();

    public YScrollPanel(String height, double top, double right) {
        super("y-scroll");
        this.height = height;

        add(scrollBar);

        setTop(top);
        setRight(right);
        setLayoutPosition(Style.Position.ABSOLUTE);
        setWidth(JQueryExtension.scrollBarWidth() + "px");
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if(height != null) {
            setHeight(height);
        }
    }

    public void setScrollHeight(String height) {
        scrollBar.setHeight(height);
    }

    @Override
    public void setHeight(String height) {
        this.height = height;

        if(isAttached()) {
            $this().height(height);
        }
    }

    public String getHeight() {
        return height;
    }

    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        scrollBar.setWidth(width);
    }
}
