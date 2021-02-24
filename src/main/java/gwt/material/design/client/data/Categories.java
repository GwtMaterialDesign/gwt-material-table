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
package gwt.material.design.client.data;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Components;
import gwt.material.design.client.data.factory.Mode;

import java.util.Collection;

public class Categories<T> extends Components<CategoryComponent<T>> {

    public Categories() {
    }

    public Categories(int maxSize) {
        super(maxSize);
    }

    public Categories(Collection<CategoryComponent<T>> components) {
        super(components);
    }

    public Categories(Collection<CategoryComponent<T>> components, Clone<CategoryComponent<T>> clone) {
        super(components, clone);
    }

    public void setEmptyMode(Mode mode) {
        forEach(categoryComponent -> categoryComponent.setMode(mode));
    }

    public void openAll() {
        forEach(CategoryComponent::open);
    }

    public void closeAll() {
        forEach(CategoryComponent::close);
    }
}
