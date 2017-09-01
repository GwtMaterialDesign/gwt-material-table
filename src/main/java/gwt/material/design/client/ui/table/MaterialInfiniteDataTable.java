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
package gwt.material.design.client.ui.table;

import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.infinite.HasLoader;
import gwt.material.design.client.data.infinite.InfiniteDataView;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;

/**
 * "Infinite" Material data table extending {@link MaterialDataTable}.
 *
 * @author Ben Dol
 *
 * @see <a href="http://gwtmaterialdesign.github.io/gwt-material-demo/#datatable">Material Data Table</a>
 * @see <a href="https://material.io/guidelines/components/data-tables.html">Material Design Specification</a>
 */
public class MaterialInfiniteDataTable<T> extends MaterialDataTable<T> implements HasLoader {

    public MaterialInfiniteDataTable() {
        super(new InfiniteDataView<>(100, new DataSource<T>() {
            @Override
            public void load(LoadConfig<T> loadConfig, LoadCallback<T> callback) {}
            @Override
            public boolean useRemoteSort() {
                return false;
            }
        }));
    }

    public MaterialInfiniteDataTable(String name, int totalRows, int viewSize, DataSource<T> dataSource) {
        super(new InfiniteDataView<>(name, totalRows, viewSize, dataSource));
    }

    public MaterialInfiniteDataTable(int totalRows, int viewSize, DataSource<T> dataSource) {
        super(new InfiniteDataView<>(totalRows, viewSize, dataSource));
    }

    public MaterialInfiniteDataTable(TableScaffolding scaffolding, int totalRows, int viewSize,
                                     DataSource<T> dataSource) {
        super(new InfiniteDataView<>(totalRows, viewSize, dataSource), scaffolding);
    }

    @Override
    public int getLoaderDelay() {
        return ((InfiniteDataView<T>) view).getLoaderDelay();
    }

    @Override
    public void setLoaderDelay(int loaderDelay) {
        ((InfiniteDataView<T>) view).setLoaderDelay(loaderDelay);
    }

    @Override
    public int getLoaderBuffer() {
        return ((InfiniteDataView<T>) view).getLoaderBuffer();
    }

    @Override
    public void setLoaderBuffer(int loaderBuffer) {
        ((InfiniteDataView<T>) view).setLoaderBuffer(loaderBuffer);
    }

    @Override
    public boolean isLoading() {
        return ((InfiniteDataView<T>) view).isLoading();
    }
}
