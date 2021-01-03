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
package gwt.material.design.client.factory;

import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.data.HasCategories;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.ui.table.TableSubHeader;

public class CustomCategoryComponent extends CategoryComponent {
    public CustomCategoryComponent(HasCategories parent, String name, Object id) {
        super(parent, name, id);
    }

    public CustomCategoryComponent(HasCategories parent, String name, Object id, boolean openByDefault) {
        super(parent, name, id, openByDefault);
    }

    @Override
    protected void render(TableSubHeader subheader, int columnCount) {
        super.render(subheader, columnCount);

        subheader.setOpenIcon(IconType.FOLDER_OPEN);
        subheader.setCloseIcon(IconType.FOLDER);
    }
}