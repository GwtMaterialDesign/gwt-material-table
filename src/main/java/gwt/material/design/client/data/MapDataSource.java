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
package gwt.material.design.client.data;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.factory.Category;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Map {@link DataSource} that supports infinite loading with categories.
 */
public class MapDataSource<T> implements DataSource<T>, HasDataView<T> {

    private final static Logger logger = Logger.getLogger(ListDataSource.class.getName());

    private Map<Object, List<T>> dataMap = new HashMap<>();
    private DataView<T> dataView;

    @Override
    public void load(LoadConfig<T> loadConfig, LoadCallback<T> callback) {
        try {
            List<T> flatData = new ArrayList<>();
            List<CategoryComponent> categories = loadConfig.getOpenCategories();
            if(dataView.isUseCategories() && categories != null) {
                for (CategoryComponent category : categories) {
                    List<T> data = dataMap.get(category.getId());
                    if (data != null) {
                        flatData.addAll(data);
                    }
                }
            } else {
                for(Map.Entry<Object, List<T>> entry : dataMap.entrySet()) {
                    flatData.addAll(entry.getValue());
                }
            }

            List<T> data = new ArrayList<>();
            int offset = loadConfig.getOffset();
            for(int i = offset; i < (offset + loadConfig.getLimit()); i++) {
                try {
                    data.add(flatData.get(i));
                } catch (IndexOutOfBoundsException e) {
                    // ignored.
                }
            }
            callback.onSuccess(new LoadResult<>(data, loadConfig.getOffset(), flatData.size(), cacheData()));
        }
        catch (IndexOutOfBoundsException ex) {
            // Silently ignore index out of bounds exceptions
            logger.log(Level.FINE, "ListDataSource threw index out of bounds.", ex);
            callback.onFailure(ex);
        }
    }

    public void add(Collection<T> list) {
        for(T item : list) {
            Category category = null;
            if(dataView.isUseCategories()) {
                category = dataView.getRowFactory().getCategory(item);
            }
            if(category == null) {
                category = new Category(AbstractDataView.ORPHAN_PATTERN);
            }
            List<T> data = dataMap.computeIfAbsent(category.getId(), k -> new ArrayList<>());
            data.add(item);
        }
    }

    public void clear() {
        if (dataMap != null) {
            dataMap.clear();
        }
    }

    public boolean cacheData() {
        return true;
    }

    @Override
    public boolean useRemoteSort() {
        return false;
    }

    @Override
    public final void setDataView(DataView<T> dataView) {
        this.dataView = dataView;
    }

    @Override
    public final DataView<T> getDataView() {
        return dataView;
    }
}
