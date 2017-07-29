package gwt.material.design.client.data.component;

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

import gwt.material.design.client.ui.table.TableSubHeader;

/**
 * @author Ben Dol
 */
public class CategoryComponent extends Component<TableSubHeader> {

    public static class OrphanCategoryComponent extends CategoryComponent {
        public OrphanCategoryComponent() {
            super(null);
        }

        @Override
        protected void render(TableSubHeader subheader) {
            super.render(subheader);

            subheader.setName("No Category");
        }
    }

    private String name;
    private int currentIndex = -1;
    private int rowCount = 0;
    private boolean openByDefault;

    public CategoryComponent(String name) {
        this(name, false);
    }

    public CategoryComponent(String name, boolean openByDefault) {
        this.name = name;
        this.openByDefault = openByDefault;
    }

    public String getName() {
        return name;
    }

    /**
     * Render the data category row element.
     * Customization to the element can be performed here.
     */
    protected void render(TableSubHeader subheader) {
        // Do nothing by default
    }

    /**
     * Render the data category row element.
     *
     * @return a fully formed {@link TableSubHeader} object.
     */
    public final TableSubHeader render() {
        TableSubHeader element = getWidget();
        if(element == null) {
            element = new TableSubHeader(this);
            setWidget(element);
        }
        render(element);
        return element;
    }

    public boolean isOpen() {
        return isRendered() && getWidget().isOpen();
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public boolean isOpenByDefault() {
        return openByDefault;
    }

    public void setOpenByDefault(boolean openByDefault) {
        this.openByDefault = openByDefault;
    }

    @Override
    public void setWidget(TableSubHeader widget) {
        // copy the elements style and info
        widget.copy(getWidget());

        super.setWidget(widget);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryComponent that = (CategoryComponent) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
