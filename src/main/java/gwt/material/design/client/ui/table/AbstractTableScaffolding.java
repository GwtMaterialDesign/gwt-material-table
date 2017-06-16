package gwt.material.design.client.ui.table;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.ui.MaterialPanel;

public abstract class AbstractTableScaffolding implements TableScaffolding {

    private Panel tableBody;
    private Panel topPanel;
    private Panel infoPanel;
    private Panel toolPanel;
    private Table table;

    private XScrollPanel xScrollPanel;

    @Override
    public void build() {
        tableBody = createTableBody();
        topPanel = createTopPanel();
        infoPanel = createInfoPanel();
        toolPanel = createToolPanel();
        table = createTable();
        xScrollPanel = createXScrollPanel();
    }

    abstract protected Panel createTableBody();

    @Override
    public Panel getTableBody() {
        return tableBody;
    }

    abstract protected Panel createTopPanel();

    @Override
    public Panel getTopPanel() {
        return topPanel;
    }

    abstract protected Panel createInfoPanel();

    @Override
    public Panel getInfoPanel() {
        return infoPanel;
    }

    abstract protected Panel createToolPanel();

    @Override
    public Panel getToolPanel() {
        return toolPanel;
    }

    abstract protected XScrollPanel createXScrollPanel();

    @Override
    public XScrollPanel getXScrollPanel() {
        return xScrollPanel;
    }

    abstract protected Table createTable();

    @Override
    public Table getTable() {
        return table;
    }

    protected Panel wrapInnerScroll(Panel table) {
        MaterialPanel innerScroll = new MaterialPanel();
        innerScroll.setStyleName("inner-scroll");
        innerScroll.add(table);
        return innerScroll;
    }

    /**
     * Apply the scaffolding together.
     * @param container the base container for the scaffolding.
     */
    @Override
    public void apply(HasWidgets container) {
        container.clear();
        container.add(topPanel);
        container.add(tableBody);
        container.add(xScrollPanel);

        topPanel.add(infoPanel);
        topPanel.add(toolPanel);

        tableBody.add(wrapInnerScroll(table));

        table.addHead(new MaterialWidget(DOM.createElement("thead")));
        table.addBody(new MaterialWidget(DOM.createElement("tbody")));
    }
}
