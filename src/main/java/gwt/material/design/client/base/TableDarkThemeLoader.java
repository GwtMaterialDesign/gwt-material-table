package gwt.material.design.client.base;

import gwt.material.design.client.resources.MaterialTableBundle;
import gwt.material.design.client.theme.dark.DarkThemeLoader;

public class TableDarkThemeLoader extends DarkThemeLoader {

    public TableDarkThemeLoader() {
        super(MaterialTableBundle.INSTANCE.darkCss());
    }
}
