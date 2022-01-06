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
