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
package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import gwt.material.design.client.data.DataView;

public class ColumnResetSortEvent<T> extends GwtEvent<ColumnResetSortHandler<T>> {

    public static final Type<ColumnResetSortHandler> TYPE = new Type<>();

    public static <T> void fire(DataView<T> source) {
        source.fireEvent(new ColumnResetSortEvent<>());
    }

    public ColumnResetSortEvent() {
    }

    @Override
    @SuppressWarnings("unchecked")
    public Type<ColumnResetSortHandler<T>> getAssociatedType() {
        return (Type) TYPE;
    }

    @Override
    protected void dispatch(ColumnResetSortHandler<T> handler) {
        handler.onColumnResetSort(this);
    }
}
