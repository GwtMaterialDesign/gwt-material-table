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

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.base.helper.EventHelper;
import gwt.material.design.client.jquery.JQueryExtension;
import gwt.material.design.client.ui.html.Div;

public class XScrollPanel extends Div {

    private Div scrollBar = new Div();

    public XScrollPanel(Panel container) {
        super("x-scroll");

        add(scrollBar);

        AttachEvent.Handler handler = event -> {
            int barSize = JQueryExtension.scrollBarWidth(container.getElement());
            if (barSize < 1) {
                barSize = 8;
            }
            super.setStyle("width: calc(100% - " + barSize + "px)");
            setHeight(barSize + "px");
        };

        if(!container.isAttached()) {
            EventHelper.onAttachOnce(container, handler);
        } else {
            handler.onAttachOrDetach(null);
        }
    }

    @Override
    public void setWidth(String width) {
        scrollBar.setWidth(width);
    }

    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        scrollBar.setHeight(height);
    }
}
