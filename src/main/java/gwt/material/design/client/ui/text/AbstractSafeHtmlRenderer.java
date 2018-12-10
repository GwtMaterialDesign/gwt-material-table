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

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

public abstract class AbstractSafeHtmlRenderer<T> implements SafeHtmlRenderer<T> {

    private SafeHtml defaultHtml = SafeHtmlUtils.EMPTY_SAFE_HTML;

    public AbstractSafeHtmlRenderer() {
    }

    public AbstractSafeHtmlRenderer(SafeHtml defaultHtml) {
        this.defaultHtml = defaultHtml;
    }

    @Override
    public SafeHtml render(T object) {
        return (object == null) ? defaultHtml : safeRender(object);
    }

    @Override
    public void render(T object, SafeHtmlBuilder builder) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public abstract SafeHtml safeRender(T object);

    public SafeHtml getDefaultHtml() {
        return defaultHtml;
    }

    public void setDefaultHtml(SafeHtml defaultHtml) {
        this.defaultHtml = defaultHtml;
    }
}
