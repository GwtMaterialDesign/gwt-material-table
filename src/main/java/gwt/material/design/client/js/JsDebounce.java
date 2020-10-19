package gwt.material.design.client.js;

import com.google.gwt.dom.client.Element;
import gwt.material.design.jquery.client.api.Functions;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL, name = "$")
public class JsDebounce {

    @JsMethod
    public static native Functions.EventFunc debounce(int throttle, Functions.Func func);

    @JsMethod
    public static native Functions.Func2<Element, MutateData> debounce(int throttle, Functions.Func2<Element, MutateData> func);
}
