package gwt.material.design.client.factory;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.ui.table.TableSubHeader;

public class CustomCategoryComponent extends CategoryComponent {
    public CustomCategoryComponent(String category) {
        super(category);
    }

    @Override
    protected void render(TableSubHeader subheader) {
        super.render(subheader);

        subheader.setOpenIcon(IconType.FOLDER_OPEN);
        subheader.setCloseIcon(IconType.FOLDER);
    }
}