package gwt.material.design.client.ui.table.cell;

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

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Ben Dol
 */
public abstract class WidgetCell<V, C extends Widget> extends AbstractCell<C> {

    @Override
    public void render(Context context, C value, SafeHtmlBuilder sb) {
        throw new UnsupportedOperationException("Use WidgetCell#render(context, value, cell)");
    }

    public abstract void render(Context context, V value, C widget);
}
