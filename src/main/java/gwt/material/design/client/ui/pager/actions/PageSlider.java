/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2023 GwtMaterialDesign
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

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.pager.MaterialDataPager;

public class PageSlider extends MaterialPanel {

    protected MaterialDataPager pager;
    protected MaterialButton next = new MaterialButton();
    protected MaterialButton previous = new MaterialButton();

    public PageSlider() {
        next.addStyleName("slide-next");
        previous.addStyleName("slide-previous");
        next.setIconType(IconType.KEYBOARD_ARROW_RIGHT);
        next.setWaves(WavesType.DEFAULT);
        previous.setIconType(IconType.KEYBOARD_ARROW_LEFT);
        previous.setWaves(WavesType.DEFAULT);
    }

    public <T> PageSlider(MaterialDataPager pager) {
        this();

        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        next.addClickHandler(clickEvent -> pager.next());
        previous.addClickHandler(clickEvent -> pager.previous());

        add(next);
        add(previous);
    }

    public MaterialButton getNext() {
        return next;
    }

    public MaterialButton getPrevious() {
        return previous;
    }
}
