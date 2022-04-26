/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2022 GwtMaterialDesign
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

import gwt.material.design.client.base.helper.ScrollHelper;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.MaterialToast;

import static gwt.material.design.client.js.JsMaterialElement.$;

public class TableHelp extends MaterialPanel {

    protected String help;
    protected MaterialIcon helpIcon;
    protected MaterialLabel helpLabel;

    public TableHelp() {
        super("table-help");

        helpIcon = new MaterialIcon(IconType.HELP_OUTLINE);
        helpLabel = new MaterialLabel();
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        if (help != null) {
            helpLabel.setText(help);
            helpIcon.add(helpLabel);
            add(helpIcon);
        }
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }
}
