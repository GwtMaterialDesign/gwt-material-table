package gwt.material.design.client.data.infinite;

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

import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.data.DataView;
import gwt.material.design.client.data.component.CategoryComponent;

import java.util.List;

/**
 * A Data Source for the {@link InfiniteDataView}, supports asynchronous calls
 * expecting that {@link InfiniteDataView#loaded(int, List, int, boolean)} is called when ready.
 * <br><br>
 * <b>Note</b> that if {@link InfiniteDataView#loaded(int, List, int, boolean)} is not called
 * the data will not be populated.
 *
 * @author Ben Dol
 */
public abstract class InfiniteDataSource<T> implements DataSource<T> {

    public abstract void load(InfiniteDataView<T> dataView, int startIndex, int viewSize, List<CategoryComponent> categories);

    @Override
    public void load(DataView<T> dataView, int startIndex, int viewSize) {
        if(dataView instanceof InfiniteDataView) {
            load((InfiniteDataView<T>)dataView, startIndex, viewSize, dataView.getOpenCategories());
        } else {
            throw new UnsupportedOperationException(
                "The data view provided to the InfiniteDataSource must be an InfiniteDataView");
        }
    }

    @Override
    public int getDataSize() {
        return 0; // Not required for infinite
    }
}
