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
import gwt.material.design.client.resources.MaterialTableDebugBundle;
import gwt.material.design.client.ui.pager.MaterialDataPagerClientBundle;

/**
 * @author Ben Dol
 */
public class MaterialTableDebugBase extends MaterialDesignBase {

    @Override
    public void load() {
        injectDebugJs(MaterialTableDebugBundle.INSTANCE.jQueryExt());
        injectDebugJs(MaterialTableDebugBundle.INSTANCE.stickyth());
        injectDebugJs(MaterialTableDebugBundle.INSTANCE.tableSubHeaders());
        injectDebugJs(MaterialTableDebugBundle.INSTANCE.greedyScroll());
        injectDebugJs(MaterialTableDebugBundle.INSTANCE.mutateEvents());
        injectDebugJs(MaterialTableDebugBundle.INSTANCE.mutate());
        injectCss(MaterialTableDebugBundle.INSTANCE.style());
        injectCss(MaterialDataPagerClientBundle.INSTANCE.dataPagerDebugCss());
    }
}
