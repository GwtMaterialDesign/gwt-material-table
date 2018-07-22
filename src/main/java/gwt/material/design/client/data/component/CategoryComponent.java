/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
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
package gwt.material.design.client.data.component;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.data.HasCategories;
import gwt.material.design.client.ui.table.TableHeader;
import gwt.material.design.client.ui.table.TableSubHeader;

/**
 * @author Ben Dol
 */
public class CategoryComponent extends Component<TableSubHeader> {

    public static class OrphanCategoryComponent extends CategoryComponent {
        public OrphanCategoryComponent(HasCategories parent) {
            super(parent, null);
        }

        @Override
        protected void render(TableSubHeader subheader, int columnCount) {
            super.render(subheader, columnCount);

            subheader.setName("No Category");
        }
    }

    private String name;
    private String height;
    private boolean openByDefault;
    private boolean hideName;

    private int currentIndex = -1;
    private int rowCount = 0;

    private HasCategories parent;

    public CategoryComponent(HasCategories parent, String name) {
        this(parent, name, false);
    }

    public CategoryComponent(HasCategories parent, String name, boolean openByDefault) {
        this.parent = parent;
        this.name = name;
        this.openByDefault = openByDefault;
    }

    /**
     * Open this category if we are rendered.
     */
    public void open() {
        if(isRendered()) {
            parent.openCategory(this);
        }
    }

    /**
     * Close this category if we are rendered.
     */
    public void close() {
        if(isRendered()) {
            parent.closeCategory(this);
        }
    }

    /**
     * Toggle the open/close state of this category.
     */
    public void toggle() {
        if(isOpen()) {
            close();
        } else {
            open();
        }
    }

    public String getName() {
        return name;
    }

    /**
     * Render the data category row element.
     * Customization to the element can be performed here.
     */
    protected void render(TableSubHeader subheader, int columnCount) {
        // Do nothing by default
    }

    /**
     * Render the data category row element.
     *
     * @return a fully formed {@link TableSubHeader} object.
     */
    public final TableSubHeader render(int columnCount) {
        TableSubHeader element = getWidget();
        if(element == null) {
            element = new TableSubHeader(this, columnCount);
            setWidget(element);
        }

        render(element, columnCount);
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

        if(isRendered() && openByDefault && !isOpen()) {
            open();
        }
    }

    public boolean isHideName() {
        return hideName;
    }

    public void setHideName(boolean hideName) {
        this.hideName = hideName;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;

        Widget widget = getWidget();
        if(widget != null && widget.isAttached()) {
            widget.setHeight(height);
        }
    }

    public TableHeader getHeader(int index) {
        return getWidget().getHeader(index);
    }

    public TableHeader getHeader(String name) {
        return getWidget().getHeader(name);
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
