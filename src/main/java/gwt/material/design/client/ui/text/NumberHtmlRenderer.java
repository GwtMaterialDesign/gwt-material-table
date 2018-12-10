/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2018 GwtMaterialDesign
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
package gwt.material.design.client.ui.text;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

public class NumberHtmlRenderer<T extends Number> extends AbstractSafeHtmlRenderer<T> {

    public NumberHtmlRenderer() {
        this(SafeHtmlUtils.fromString("-"));
    }

    public NumberHtmlRenderer(SafeHtml defaultHtml) {
        super(defaultHtml);
    }

    @Override
    public SafeHtml safeRender(T object) {
        return SafeHtmlUtils.fromString(NumberFormat.getDecimalFormat().format(object));
    }
}
