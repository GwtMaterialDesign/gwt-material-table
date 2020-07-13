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
package gwt.material.design.client.jquery;

import com.google.gwt.dom.client.Element;
import gwt.material.design.jquery.client.api.Functions;
import gwt.material.design.jquery.client.api.JQueryElement;
import jsinterop.annotations.JsType;

//TODO: Object -> JSType
@JsType(name = "jQuery", isNative = true)
public class JQueryExtElement extends JQueryElement {

    public native JQueryExtElement forceRedraw();

    public native JQueryExtElement insertAt(int index, Element element);

    public native JQueryExtElement longpress(Functions.MouseEventFunc longCallback, Functions.MouseEventFunc shortCallback, int duration);

    public native boolean hasVerticalScrollBar();

    public native boolean hasHorizontalScrollBar();

    public native boolean hasScrollBar();

    public native boolean isScrollStart();

    public native boolean isScrollEnd();

    public native boolean scrollHandler(Object dir, String name, Functions.EventFunc1<Object> handler);

    public native boolean smartScroll(String name, Functions.EventFunc1<Object> handler);

    public native boolean scrollY(String name, Functions.EventFunc1<Object> handler);

    public native boolean scrollX(String name, Functions.EventFunc1<Object> handler);

    public native boolean onScrollUp(String name, Functions.EventFunc1<Object> handler);

    public native boolean onScrollDown(String name, Functions.EventFunc1<Object> handler);

    public native boolean onScrollLeft(String name, Functions.EventFunc1<Object> handler);

    public native boolean onScrollRight(String name, Functions.EventFunc1<Object> handler);
}
