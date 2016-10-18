package gwt.material.design.client.factory;

import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.CategoryComponent.OrphanCategoryComponent;
import gwt.material.design.client.data.factory.CategoryComponentFactory;

public class CustomCategoryFactory extends CategoryComponentFactory {

    @Override
    public CategoryComponent generate(String categoryName) {
        CategoryComponent category = super.generate(categoryName);

        if (!(category instanceof OrphanCategoryComponent)) {
            category = new CustomCategoryComponent(categoryName);
        }
        return category;
    }
}
