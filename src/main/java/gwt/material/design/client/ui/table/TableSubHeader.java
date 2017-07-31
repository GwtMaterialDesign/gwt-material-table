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
package gwt.material.design.client.ui.table;

import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.js.Js;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.html.Text;

import java.util.NoSuchElementException;

/**
 * @author Ben Dol
 */
public class TableSubHeader extends TableRow {

    private TableHeader iconTh;
    private TableHeader nameTh;

    private MaterialIcon icon;
    private Text nameLbl;

    private IconType openIcon = IconType.ADD;
    private IconType closeIcon = IconType.REMOVE;

    public TableSubHeader(TableSubHeader clone) {
        this(clone.getDataCategory());
        copy(clone);
    }

    public TableSubHeader(CategoryComponent category) {
        super(category);
        build();
    }

    @Override
    protected void build() {
        addStyleName(TableCssName.SUBHEADER);

        iconTh = new TableHeader();
        icon = new MaterialIcon(openIcon);
        iconTh.add(icon);
        add(iconTh);

        nameTh = new TableHeader();
        nameLbl = new Text("Subheader");
        nameTh.add(nameLbl);
        add(nameTh);

        setName(category.getName());
        setId(category.getName());
    }

    public void add(TableHeader tableHeader) {
        super.add(tableHeader);
    }

    public void setName(String name) {
        nameLbl.setText(name);
    }

    public String getName() {
        return nameLbl.getText();
    }

    public void hideName() {
        nameTh.setVisible(false);
    }

    public void showName() {
        nameTh.setVisible(true);
    }

    public TableData getNameTh() {
        return nameTh;
    }

    public IconType getOpenIcon() {
        return openIcon;
    }

    public void setOpenIcon(IconType openIcon) {
        this.openIcon = openIcon;

        if(!isOpen()) {
            icon.setIconType(openIcon);
        }
        setDataAttribute("data-open-icon", openIcon.getCssName());
    }

    public IconType getCloseIcon() {
        return closeIcon;
    }

    public void setCloseIcon(IconType closeIcon) {
        this.closeIcon = closeIcon;

        if(isOpen()) {
            icon.setIconType(closeIcon);
        }
        setDataAttribute("data-close-icon", closeIcon.getCssName());
    }

    public void hideIcon() {
        iconTh.setVisible(false);
    }

    public void showIcon() {
        iconTh.setVisible(true);
    }

    public TableData getIconTh() {
        return iconTh;
    }

    public MaterialIcon getIcon() {
        return icon;
    }

    public boolean isOpen() {
        return $this().hasClass(TableCssName.EXPANDED);
    }

    public int getScrollPosition() {
        int pos = Integer.valueOf(String.valueOf($this().data("pos")));
        return !Js.isUndefinedOrNull(pos) ? pos : -1;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if(visible) {
            $this().children("*").show();
        } else {
            $this().children("*").hide();
        }
    }

    public void copy(TableSubHeader tableRow) {
        if(tableRow != null) {
            super.copy(tableRow);

            icon.setIconType(tableRow.icon.getIconType());
            nameLbl.setText(tableRow.nameLbl.getText());

            setName(tableRow.getName());
            setId(tableRow.getId());

            openIcon = tableRow.openIcon;
            closeIcon = tableRow.closeIcon;
        }
    }

    @Override
    public void removeFromParent() {
        Widget parent = getParent();
        if(parent != null && parent instanceof MaterialWidget) {
            try {
                ((MaterialWidget) parent).getChildren().remove(this);
            } catch (NoSuchElementException ex) {
                // This means we have already removed this subheader.
            }
        }
        $this().parent().remove();
    }

    @Override
    public String toString() {
        return getElement().toString();
    }
}
