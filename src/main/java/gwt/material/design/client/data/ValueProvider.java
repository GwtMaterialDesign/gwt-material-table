package gwt.material.design.client.data;

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


/**
 * Returns the value property of the target.
 */
public interface ValueProvider<T, V> {

    /**
     * Returns the property value of the given object.
     */
    V getValue(T object);

    /**
     * Sets the value of the given object.
     */
    void setValue(T object, V value);

    /**
     * Returns the path that this ValueProvider makes available.
     */
    String getPath();
}
