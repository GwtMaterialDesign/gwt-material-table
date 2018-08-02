package gwt.material.design.client.data;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.Components;

import java.util.Collection;

public class Categories extends Components<CategoryComponent> {

    public Categories() {
    }

    public Categories(int maxSize) {
        super(maxSize);
    }

    public Categories(Collection<? extends CategoryComponent> components) {
        super(components);
    }

    public Categories(Collection<? extends CategoryComponent> components, Clone<CategoryComponent> clone) {
        super(components, clone);
    }

    public void openAll() {
        forEach(CategoryComponent::open);
    }

    public void closeAll() {
        forEach(CategoryComponent::close);
    }
}
