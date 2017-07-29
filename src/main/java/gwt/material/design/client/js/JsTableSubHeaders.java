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

import gwt.material.design.jquery.client.api.JQueryElement;
import gwt.material.design.client.ui.table.MaterialDataTable;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;

/**
 * @author Ben Dol
 */
@JsType(name = "TableSubHeaders", isNative = true, namespace = JsPackage.GLOBAL)
public class JsTableSubHeaders {

    @JsType
    public static class SubHeaderOptions {
        @JsProperty public int marginTop = 0;
        @JsProperty public int marginLeft = 0;
        @JsProperty public int scrollThrottle = 20;
        @JsProperty public int resizeThrottle = 100;
    }

    @JsMethod
    public static native JsTableSubHeaders newInstance(JQueryElement table, String selector);

    @JsMethod public native void load();

    @JsMethod public native void load(SubHeaderOptions opts);

    @JsMethod public native void unload();

    @JsMethod public native void reload();

    @JsMethod public native void detect();

    @JsMethod public native void toggle(JQueryElement subheader);

    @JsMethod public native void open(JQueryElement subheader);

    @JsMethod public native void close(JQueryElement subheader);

    @JsMethod public native void recalculate(boolean fireEvents);

    @JsMethod public native void updateWidths();

    @JsMethod public native void updateHeights();

    @JsMethod public native void setMarginTop(int marginTop);

    @JsMethod public native void setMarginLeft(int marginLeft);

    @JsMethod public native boolean isLoaded();
}
