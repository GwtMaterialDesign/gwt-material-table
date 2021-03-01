package gwt.material.design.client.ui.table;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;

public class TableFooter extends MaterialWidget {

    public TableFooter() {
        super(Document.get().createTFootElement());
    }
}
