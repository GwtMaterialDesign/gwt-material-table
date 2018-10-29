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
package gwt.material.design.client.ui.table.cell;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

import java.util.Set;

public abstract class AbstractSafeHtmlCell<C> extends com.google.gwt.cell.client.AbstractSafeHtmlCell<C> {

    private String emptyValue;

    public AbstractSafeHtmlCell(SafeHtmlRenderer<C> renderer, String... consumedEvents) {
        super(renderer, consumedEvents);
    }

    public AbstractSafeHtmlCell(SafeHtmlRenderer<C> renderer, Set<String> consumedEvents) {
        super(renderer, consumedEvents);
    }

    @Override
    public void render(Context context, C data, SafeHtmlBuilder sb) {
        if (data == null) {
            render(context, SafeHtmlUtils.fromString(emptyValue), sb);
        } else {
            render(context, getRenderer().render(data), sb);
        }
    }

    public String getEmptyValue() {
        return emptyValue;
    }

    public void setEmptyValue(String emptyValue) {
        this.emptyValue = emptyValue;
    }
}
