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

import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ben Dol
 */
public class ListDataSource<T> implements DataSource<T> {

    private Logger logger = Logger.getLogger(ListDataSource.class.getName());

    private List<T> data;

    public ListDataSource() {
        data = new ArrayList<>();
    }

    public ListDataSource(List<T> data) {
        this.data = data;
    }

    @Override
    public void load(LoadConfig<T> loadConfig, LoadCallback<T> callback) {
        try {
            int size = loadConfig.getOffset() + loadConfig.getLimit();
            if(size > data.size()){
                size = data.size();
            }
            final List<T> subList = size == 0 ? new ArrayList<>() : data.subList(loadConfig.getOffset(), size);
            callback.onSuccess(new LoadResult<>(subList, loadConfig.getOffset(), data.size(), cacheData()));
        } catch (IndexOutOfBoundsException ex) {
            // Silently ignore index out of bounds exceptions
            logger.log(Level.FINE, "ListDataSource threw index out of bounds.", ex);
            callback.onFailure(ex);
        }
    }

    public void add(int startIndex, List<T> list) {
        data.addAll(startIndex, list);
    }

    public void remove(List<T> list) {
        data.removeAll(list);
    }

    public boolean cacheData() {
        return true;
    }

    @Override
    public boolean useRemoteSort() {
        return false;
    }
}
