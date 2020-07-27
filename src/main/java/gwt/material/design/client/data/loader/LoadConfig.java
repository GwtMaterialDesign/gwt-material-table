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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LoadConfig<T> {

    private final int offset;
    private final int limit;
    private final SortContext<T> sortContext;
    private final List<CategoryComponent> openCategories;

    public LoadConfig(int offset, int limit, SortContext<T> sortContext, List<CategoryComponent> openCategories) {
        this.offset = offset;
        this.limit = limit;
        this.sortContext = sortContext;
        this.openCategories = openCategories;
    }

    /**
     * Get load offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Get load limit. Set to "0" for no limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Get sorters to use when loading. Usually used for remote data sources.
     * Return null if no sort context exists.
     */
    public SortContext<T> getSortContext() {
        return sortContext;
    }

    /**
     * Get categories to load.
     * Return null if no categories exists.
     * FIXME: CategoryComponent belongs to table package, need to make similar class in data package
     */
    public List<CategoryComponent> getOpenCategories() {
        return openCategories;
    }

    public List<String> getOpenCategoryNames() {
        return openCategories != null
            ? openCategories.stream().map(CategoryComponent::getName).collect(Collectors.toList())
            : null;
    }

    @Override
    public String toString() {
        return "LoadConfig{" +
                "offset=" + offset +
                ", limit=" + limit +
                ", sortContext=" + sortContext +
                ", openCategories=" + openCategories +
                '}';
    }
}
