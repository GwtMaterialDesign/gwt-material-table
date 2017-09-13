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

public interface HasLoader {

    int getLoaderDelay();

    void setLoaderDelay(int loaderDelay);

    int getLoaderBuffer();

    /**
     * The amount of data that will buffer your start index and view size.
     * This can be useful in removing loading delays. Defaults to 10.
     */
    void setLoaderBuffer(int loaderBuffer);

    /**
     * Is the loader loading.
     */
    boolean isLoading();
}
