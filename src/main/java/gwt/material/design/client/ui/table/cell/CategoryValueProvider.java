package gwt.material.design.client.ui.table.cell;

import gwt.material.design.client.data.component.CategoryComponent;

import java.util.List;

public interface CategoryValueProvider<T> {
    String getValue(List<T> entireData, CategoryComponent<T> categoryComponent);
}