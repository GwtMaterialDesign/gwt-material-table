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
package gwt.material.design.client.ui.pager.actions;

import com.google.gwt.event.dom.client.KeyCodes;
import gwt.material.design.client.ui.MaterialIntegerBox;
import gwt.material.design.client.ui.pager.MaterialDataPager;

/**
 * A {@link PageSelection} component that provides a {@link MaterialIntegerBox} display text field.
 * This can be set with {@link MaterialDataPager#setPageSelection(PageSelection)}, see {@link PageListBox} as another
 * display option.
 *
 * @author kevzlou7979
 */
public class PageNumberBox extends AbstractPageSelection<MaterialIntegerBox> implements PageSelection {

    protected MaterialIntegerBox integerBox = new MaterialIntegerBox();

    public PageNumberBox() {
        super();
    }

    public PageNumberBox(MaterialDataPager<?> pager) {
        super(pager);
    }

    @Override
    protected void setup() {
        integerBox.setMin("1");
        registerHandler(addKeyDownHandler(keyDownEvent -> {
            if (keyDownEvent.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                pager.gotoPage(integerBox.getValue());
            }
        }));
    }

    @Override
    public MaterialIntegerBox getComponent() {
        return integerBox;
    }
}
