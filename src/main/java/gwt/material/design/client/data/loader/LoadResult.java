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

import java.util.List;
import gwt.material.design.client.data.DataView;

public abstract class LoadResult<T> {

    /**
     * Return result data.
     */
    public abstract List<T> getData();

    /**
     * Return actual offset of the result. In most cases equals requested offset.
     * Starting from "0".
     */
    public abstract int getOffset();

    /**
     * Return total length of the data.
     * <br/>
     * <ul>
     *  <li>For non-paging requests equals size of the data.</li>
     *  <li>For paging requests should equals total number of records</li>
     * </ul>
     */
    public abstract int getTotalLength();

    /**
     * Should we cache the data, retrieved, it is worth noting that not all
     * {@link DataView} implementations will cache data.
     * @return true by default.
     */
    public boolean isCacheData() {
        return true;
    }
}
