package gwt.material.design.client.ui.pager;

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.html.Span;

/**
 * Widget for building the page rows selection - where you can set the range of row count into listbox
 *
 * @author kevzlou7979
 */
public class PageRowSelection extends MaterialWidget {

    private final MaterialDataPager pager;
    protected Span rowsPerPageLabel = new Span("Rows per page");
    protected MaterialListValueBox<Integer> listPageRows = new MaterialListValueBox<>();

    public PageRowSelection(MaterialDataPager pager) {
        super(Document.get().createDivElement(), TableCssName.ROWS_PER_PAGE_PANEL);
        this.pager = pager;
    }

    @Override
    protected void onLoad() {
        super.onLoad();

        setGrid("s12 m4 l3");

        add(listPageRows);
        add(rowsPerPageLabel);

        listPageRows.clear();
        for (int limitOption : pager.getLimitOptions()) {
            listPageRows.addItem(limitOption, true);
        }
        registerHandler(listPageRows.addValueChangeHandler(valueChangeEvent -> {
            pager.setLimit(valueChangeEvent.getValue());
            pager.updateRowsPerPage(valueChangeEvent.getValue());
        }));
    }
}
