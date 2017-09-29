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
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.CategoryComponent.OrphanCategoryComponent;
import gwt.material.design.client.data.component.ComponentFactory;

/**
 * Default category {@link ComponentFactory} factory used by the {@link AbstractDataView}.
 *
 * @author Ben Dol
 */
public class CategoryComponentFactory implements ComponentFactory<CategoryComponent, String> {

    @Override
    public CategoryComponent generate(DataView dataView, String categoryName) {
        return categoryName != null ? new CategoryComponent(categoryName) : new OrphanCategoryComponent();
    }
}
