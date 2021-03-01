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
package gwt.material.design.client.js;

import com.google.gwt.core.client.JavaScriptObject;
import gwt.material.design.jquery.client.api.JQueryElement;

/**
 * @author Ben Dol
 */
public class StickyTableOptions extends JavaScriptObject {
    protected StickyTableOptions() {
    }

    //TODO: Convert to JSInterop
    public native final void setScrollableArea(JQueryElement scrollableArea) /*-{
        this.scrollableArea = scrollableArea;
    }-*/;

    public native final void setMarginTop(int marginTop) /*-{
        this.marginTop = marginTop;
    }-*/;

    public static StickyTableOptions create(JQueryElement scrollableArea) {
        return create(scrollableArea, 0);
    }

    public static StickyTableOptions create(JQueryElement scrollableArea, int marginTop) {
        StickyTableOptions obj = JavaScriptObject.createObject().cast();
        obj.setScrollableArea(scrollableArea);
        obj.setMarginTop(marginTop);
        return obj;
    }
}