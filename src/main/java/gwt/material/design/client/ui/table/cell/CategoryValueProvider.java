package gwt.material.design.client.ui.table.cell;

import gwt.material.design.client.data.component.CategoryComponent;

public interface CategoryValueProvider<T> {

    String getValue(CategoryComponent<T> categoryComponent);
}