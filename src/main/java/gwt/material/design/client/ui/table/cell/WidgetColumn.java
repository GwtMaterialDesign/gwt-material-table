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
package gwt.material.design.client.ui.table.cell;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.Widget;

/**
 * A column that displays its contents with a {@link TextCell} and does not make
 * use of view data.
 *
 * @param <T> the row type
 * @author Ben Dol
 */
public abstract class WidgetColumn<T, C extends Widget> extends Column<T, C> {

    static class BlankWidgetCell<T, C extends Widget> extends WidgetCell<T, C> {
        @Override
        public void render(Context context, T value, C widget) {}
    }

    public WidgetColumn() {
        super(new BlankWidgetCell<T, C>());
    }

    public WidgetColumn(WidgetCell<T, C> cell) {
      super(cell);
    }

    @Override
    public WidgetColumn<T, C> render(Context context, T object, SafeHtmlBuilder sb) {
      throw new UnsupportedOperationException("Use WidgetColumn#render(context, object).");
    }

    public C render(Context context, T object) {
      C widget = getValue(object);
      ((WidgetCell<T,C>)getCell()).render(context, object, widget);
      return widget;
    }
}