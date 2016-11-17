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
package gwt.material.design.client.data;

import java.util.Comparator;

/**
 * @author Ben Dol
 */
public class DataSort<M> implements Comparator<M> {
    private SortDir direction;
    private ValueProvider<? super M, ?> property;
    private final Comparator<? super M> comparator;

    public DataSort(Comparator<? super M> itemComparator, SortDir direction) {
        this.comparator = itemComparator;
        this.direction = direction;
    }

    public <V> DataSort(final ValueProvider<? super M, V> property, final Comparator<? super V> itemComparator, SortDir direction) {
        this.property = property;
        this.direction = direction;
        this.comparator = (o1, o2) -> itemComparator.compare(property.getValue(o1), property.getValue(o2));
    }

    public <V extends Comparable<V>> DataSort(final ValueProvider<? super M, V> property, SortDir direction) {
        this.property = property;
        this.direction = direction;
        this.comparator = (o1, o2) -> {
            V v1 = property.getValue(o1);
            V v2 = property.getValue(o2);

            if ((v1 == null & v2 != null) || (v1 != null && v2 == null)) {
                return v1 == null ? -1 : 1;
            }
            if (v1 == null & v2 == null) {
                return 0;
            }
            return v1.compareTo(v2);
        };
    }

    @Override
    public int compare(M o1, M o2) {
        int val = comparator.compare(o1, o2);
        return direction == SortDir.ASC ? val : -val;
    }

    /**
     * Returns the current sort direction for this sort info.
     */
    public SortDir getDirection() {
        return direction;
    }

    public String getPath() {
        return property != null ? property.getPath() : "";
    }

    public ValueProvider<? super M, ?> getProperty() {
        return property;
    }

    /**
     * Sets a new sort direction.
     */
    public void setDirection(SortDir direction) {
        this.direction = direction;
    }
}
