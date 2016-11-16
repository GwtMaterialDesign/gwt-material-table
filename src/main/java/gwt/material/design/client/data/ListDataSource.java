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
    private DataView<T> dataView;

    public ListDataSource(DataView<T> dataView) {
        this.dataView = dataView;
        data = new ArrayList<>();
    }

    public ListDataSource(DataView<T> dataView, List<T> data) {
        this.dataView = dataView;
        this.data = data;
    }

    public void load(int startIndex, int viewSize) {
        load(dataView, startIndex, viewSize);
    }

    @Override
    public void load(DataView<T> dataView, int startIndex, int viewSize) {
        try {
            dataView.loaded(startIndex, data.subList(startIndex - 1, startIndex - 1 + viewSize));
        } catch (IndexOutOfBoundsException ex) {
            // Silently ignore index out of bounds exceptions
            logger.log(Level.FINE, "ListDataSource threw index out of bounds.", ex);
        }
    }

    public void add(int startIndex, List<T> list) {
        data.addAll(startIndex, list);
    }

    public void remove(List<T> list) {
        data.removeAll(list);
    }

    public int getDataSize(){
        return data.size();
    }

    @Override
    public boolean useRemoteSort() {
        return false;
    }
}
