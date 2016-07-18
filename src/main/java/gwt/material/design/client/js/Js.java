package gwt.material.design.client.js;

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


import com.google.gwt.core.client.JavaScriptObject;

import java.util.List;

/**
 * @author Ben Dol
 */
public class Js {

    @SuppressWarnings("unchecked")
    public static <T> T createJsObject() {
        return (T) JavaScriptObject.createObject();
    }

    @SuppressWarnings("unchecked")
    public static <T> T createJsArray() {
        return (T) JavaScriptObject.createArray();
    }

    /**
     * Box a native JS array in a Java List. It does not have any performance
     * penalty because we directly change the native array of super ArrayList
     * implementation.
     */
    public static native <T> List<T> asList(JavaScriptObject o) /*-{
        var l = @java.util.ArrayList::new()();
        l.@java.util.ArrayList::array = o;
        return l;
    }-*/;

    public static native boolean isPrimitiveType(Object dataItem) /*-{
        return Object(dataItem) !== dataItem;
    }-*/;

    public static native boolean isUndefinedOrNull(Object o) /*-{
        return o === undefined || o === null;
    }-*/;

    public static native boolean isObject(Object o) /*-{
        return typeof o === "object" && o !== null;
    }-*/;

    public static native JavaScriptObject getError(String msg) /*-{
        return new Error(msg || '');
    }-*/;

    public static native JavaScriptObject getUndefined() /*-{
        return undefined;
    }-*/;

    public static native boolean isTrue(Object o) /*-{
        return o;
    }-*/;
}
