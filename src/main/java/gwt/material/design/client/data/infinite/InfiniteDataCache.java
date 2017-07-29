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
package gwt.material.design.client.data.infinite;

import java.util.ArrayList;
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
public class InfiniteDataCache<T> extends ArrayList<T> {

    public List<T> getCache(int startIndex, int viewSize) {
        int size = size();
        int end = (startIndex + viewSize);

        List<T> cache = new ArrayList<>();
        if(startIndex < size && end <= size) {
            for(int i = startIndex; i < end; i++) {
                T data = get(i);
                if(data != null) {
                    cache.add(data);
                } else {
                    // If any data record is null we
                    // need to invoke a full data request
                    cache.clear();
                    break;
                }
            }
        }
        return cache;
    }

    public void addCache(int startIndex, List<T> data) {
        // Create placeholders up to the specified index.
        int cacheOffset = Math.max(0, startIndex - size());
        for (int i = 0; i < cacheOffset; i++) {
            add(null);
        }

        // Insert the new values into the data array.
        for (int i = startIndex; i < startIndex + data.size(); i++) {
            T value = data.get(i - startIndex);
            if (i < size()) {
                set(i, value);
            } else {
                add(value);
            }
        }
    }
}
