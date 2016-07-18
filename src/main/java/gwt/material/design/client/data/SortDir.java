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

import java.util.Comparator;

public enum SortDir {
    /**
     * Ascending.
     */
    ASC {
        @Override
        public <X> Comparator<X> comparator(final Comparator<X> c) {
            return c::compare;
        }
    },

    /**
     * Descending.
     */
    DESC {
        @Override
        public <X> Comparator<X> comparator(final Comparator<X> c) {
            return (X o1, X o2) -> c.compare(o2, o1);
        }
    };

    public SortDir reverse() {
        return this.equals(ASC) ? DESC : ASC;
    }

    public abstract <X> Comparator<X> comparator(Comparator<X> c);
}