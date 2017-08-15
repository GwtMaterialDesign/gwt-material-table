package gwt.material.design.client.data;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.loader.LoadCallback;
import gwt.material.design.client.data.loader.LoadConfig;
import gwt.material.design.client.data.loader.LoadResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Map {@link DataSource} that supports infinite loading with categories.
 */
public class MapDataSource<T> implements DataSource<T>, HasDataView<T> {

    private Logger logger = Logger.getLogger(ListDataSource.class.getName());

    private Map<String, List<T>> dataMap = new HashMap<>();
    private DataView<T> dataView;

    @Override
    public void load(LoadConfig<T> loadConfig, LoadCallback<T> callback) {
        try {
            List<T> flatMap = new ArrayList<>();
            if(dataView.isUseCategories()) {
                for (CategoryComponent category : loadConfig.getOpenCategories()) {
                    flatMap.addAll(dataMap.get(category.getName()));
                }
            } else {
                flatMap.addAll(dataMap.get(AbstractDataView.ORPHAN_PATTERN));
            }

            callback.onSuccess(new LoadResult<>(flatMap, loadConfig.getOffset(), flatMap.size(), cacheData()));
        } catch (IndexOutOfBoundsException ex) {
            // Silently ignore index out of bounds exceptions
            logger.log(Level.FINE, "ListDataSource threw index out of bounds.", ex);
            callback.onFailure(ex);
        }
    }

    public void add(Collection<T> list) {
        for(T item : list) {
            String category = null;
            if(dataView.isUseCategories()) {
                category = dataView.getRowFactory().getCategory(item);
            }
            if(category == null) {
                category = AbstractDataView.ORPHAN_PATTERN;
            }
            List<T> data = dataMap.computeIfAbsent(category, k -> new ArrayList<>());
            data.add(item);
        }
    }

    public boolean cacheData() {
        return true;
    }

    @Override
    public boolean useRemoteSort() {
        return false;
    }

    @Override
    public final void setDataView(DataView<T> dataView) {
        this.dataView = dataView;
    }

    @Override
    public final DataView<T> getDataView() {
        return dataView;
    }
}
