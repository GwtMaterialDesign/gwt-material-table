    /*
 * Copyright 2014 Cristian Rinaldi & Andres Testi.
 *
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
 */
package gwt.material.design.client.jquery;

/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
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

import com.google.gwt.dom.client.Element;
import gwt.material.design.jquery.client.api.Event;
import gwt.material.design.jquery.client.api.Functions.MouseEventFunc;
import gwt.material.design.jquery.client.api.Functions.FuncRet2;
import gwt.material.design.jquery.client.api.JQueryElement;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

/**
 * Represent materials JQuery Extension API.
 *
 * @author Ben Dol
 */
@JsType(name = "jQuery", isNative = true)
public class JQueryExtension extends JQueryElement {

    @JsMethod(name = "$", namespace = JsPackage.GLOBAL)
    public static native JQueryExtension $(JQueryElement element);

    @JsMethod(name = "$", namespace = JsPackage.GLOBAL)
    public static native JQueryExtension $(Element element);

    public native JQueryExtension insertAt(int index, Element element);

    public native JQueryExtension longpress(MouseEventFunc longCallback, MouseEventFunc shortCallback, int duration);

    public native boolean hasVerticalScrollBar();

    public native boolean hasHorizontalScrollBar();

    public native boolean hasScrollBar();

    public native int scrollBarWidth();

    public native boolean isScrollStart();

    public native boolean isScrollEnd();

    public native boolean scrollHandler(Object dir, String name, FuncRet2<Event, String> handler);

    public native boolean smartScroll(String name, FuncRet2<Event, String> handler);

    public native boolean scrollY(String name, FuncRet2<Event, String> handler);

    public native boolean scrollX(String name, FuncRet2<Event, String> handler);

    public native boolean onScrollUp(String name, FuncRet2<Event, String> handler);

    public native boolean onScrollDown(String name, FuncRet2<Event, String> handler);

    public native boolean onScrollLeft(String name, FuncRet2<Event, String> handler);

    public native boolean onScrollRight(String name, FuncRet2<Event, String> handler);
}
