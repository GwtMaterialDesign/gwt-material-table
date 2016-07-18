package gwt.material.design.client.ui.table;

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


import gwt.material.design.client.data.infinite.InfiniteDataSource;
import gwt.material.design.client.data.infinite.InfiniteDataView;

/**
 * "Infinite" Material data table extending {@link MaterialDataTable}.
 *
 * @author Ben Dol
 */
public class MaterialInfiniteDataTable<T> extends MaterialDataTable<T> {

    public MaterialInfiniteDataTable() {
    }

    public MaterialInfiniteDataTable(int totalRows, int viewSize, InfiniteDataSource<T> dataSource) {
        super(new InfiniteDataView<>(totalRows, viewSize, dataSource));
    }

    public MaterialInfiniteDataTable(TableScaffolding scaffolding, int totalRows, int viewSize, InfiniteDataSource<T> dataSource) {
        super(new InfiniteDataView<>(totalRows, viewSize, dataSource), scaffolding);
    }

    public int getIndexOffset() {
        return ((InfiniteDataView<T>)dataView).getIndexOffset();
    }

    /**
     * The amount of data that will buffer your start index and view size.
     * This can be useful in removing and loading delays. Defaults to 10.
     */
    public void setIndexOffset(int indexOffset) {
        ((InfiniteDataView<T>)dataView).setIndexOffset(indexOffset);
    }
}
