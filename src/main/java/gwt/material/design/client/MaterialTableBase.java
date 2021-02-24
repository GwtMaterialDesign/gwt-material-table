package gwt.material.design.client;

/*
 * #%L
 * GwtMaterialDesign
 * %%
 * Copyright (C) 2015 GwtMaterial
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

import com.google.gwt.dom.client.StyleInjector;
import gwt.material.design.client.resources.MaterialTableBundle;
import gwt.material.design.client.ui.pager.MaterialDataPagerClientBundle;

/**
 * @author Ben Dol
 */
public class MaterialTableBase extends MaterialDesignBase {

    @Override
    public void load() {
        // Scripts
        injectJs(MaterialTableBundle.INSTANCE.jQueryExt());
        injectJs(MaterialTableBundle.INSTANCE.stickyth());
        injectJs(MaterialTableBundle.INSTANCE.tableSubHeaders());
        injectJs(MaterialTableBundle.INSTANCE.greedyScroll());
        injectJs(MaterialTableBundle.INSTANCE.mutateEvents());
        injectJs(MaterialTableBundle.INSTANCE.mutate());

        // Styles
        injectCss(MaterialTableBundle.INSTANCE.style());
        injectCss(MaterialDataPagerClientBundle.INSTANCE.dataPagerCss());
    }
}
