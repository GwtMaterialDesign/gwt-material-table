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
package gwt.material.design.client.data.factory;

import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.HasDataCategory;
import gwt.material.design.client.data.component.ComponentFactory;
import gwt.material.design.client.data.component.RowComponent;

/**
 * Default row {@link ComponentFactory} factory used by the {@link AbstractDataView}.
 *
 * @author Ben Dol
 */
public class RowComponentFactory<M> implements ComponentFactory<RowComponent<M>, M> {

    @Override
    public RowComponent<M> generate(DataView dataView, M model) {
        return new RowComponent<>(model, dataView, getCategory(model));
    }

    public String getCategory(M model) {
        return model != null && model instanceof HasDataCategory ? ((HasDataCategory) model).getDataCategory() : null;
    }
}
