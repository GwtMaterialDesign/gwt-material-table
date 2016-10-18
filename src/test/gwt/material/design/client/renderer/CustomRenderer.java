package gwt.material.design.client.renderer;

import gwt.material.design.client.constants.CheckBoxType;
import gwt.material.design.client.data.BaseRenderer;
import gwt.material.design.client.ui.MaterialCheckBox;
import gwt.material.design.client.ui.table.TableData;

public class CustomRenderer<T> extends BaseRenderer<T> {

    @Override
    public TableData drawSelectionCell() {
        TableData td = new TableData();
        td.setId("col0");
        MaterialCheckBox checkBox = new MaterialCheckBox(td.getElement());
        checkBox.setType(CheckBoxType.FILLED);
        return td;
    }
}
