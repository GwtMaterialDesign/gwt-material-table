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
package gwt.material.design.client.data.loader;

import gwt.material.design.client.data.SortContext;
import gwt.material.design.client.data.component.CategoryComponent;

import java.util.List;

public interface LoadConfig<T> {

    /**
     * Get load offset.
     */
    int getOffset();

    /**
     * Get load limit. Set to "0" for no limit.
     */
    int getLimit();

    /**
     * Get sorters to use when loading. Usually used for remote data sources.
     * Return null if no sort context exists.
     */
    SortContext<T> getSortContext();

    /**
     * Get categories to load.
     * Return null if no categories exists.
     * FIXME: CategoryComponent belongs to table package, need to make similar class in data package
     */
    List<CategoryComponent> getOpenCategories();
}
