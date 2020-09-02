/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2020 GwtMaterialDesign
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
package gwt.material.design.client.ui.accessibility;

import gwt.material.design.client.accessibility.AccessibilityControl;
import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.TableHeader;

/**
 * Controls the datatable's accessibility features including it's component focused states.
 */
public class DataTableAccessibilityControl extends AccessibilityControl {

    protected AbstractDataView<?> dataView;
    protected AccessibilityOption option = new AccessibilityOption();


    protected DataTableAccessibilityControl() {
    }

    public DataTableAccessibilityControl(AbstractDataView<?> dataView) {
        this.dataView = dataView;
    }

    public DataTableAccessibilityControl(AbstractDataView<?> dataView, AccessibilityOption option) {
        this.dataView = dataView;
        this.option = option;
    }

    public void registerRowTrigger(RowComponent<?> rowComponent) {
        registerWidget(rowComponent.getWidget(), option.getKeys().getRowTrigger());
    }

    public void registerCategoryTrigger(CategoryComponent categoryComponent) {
        registerWidget(categoryComponent.getWidget(), option.getKeys().getCategoryTrigger());
    }

    public void registerHeaderTrigger(TableHeader tableHeader) {
        registerWidget(tableHeader, option.getKeys().getHeaderTrigger());
    }

    public void registerPageTrigger(MaterialDataPager pager) {
        registerWidget(pager, option.getKeys().getPageNext(), event -> pager.next());
        registerWidget(pager, option.getKeys().getPagePrevious(), event -> pager.previous());
    }
}
