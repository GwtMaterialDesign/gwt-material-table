package gwt.material.design.client.ui.accessibility;

import gwt.material.design.client.base.MaterialWidget;
import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.component.CategoryComponent;
import gwt.material.design.client.data.component.RowComponent;
import gwt.material.design.client.ui.pager.MaterialDataPager;
import gwt.material.design.client.ui.table.TableHeader;

import static gwt.material.design.jquery.client.api.JQuery.$;

/**
 * Controls the datatable's accessibility features including it's component focused states.
 */
public class DataTableAccessibilityControl {

    protected AbstractDataView<?> dataView;
    protected AccessibilityOption option = new AccessibilityOption();

    protected DataTableAccessibilityControl() {
    }

    public DataTableAccessibilityControl(AbstractDataView<?> dataView) {
        this.dataView = dataView;
    }

    public DataTableAccessibilityControl(AbstractDataView<?> dataView, AccessibilityOption option) {
        this.dataView = dataView;
        this.option = option;
    }

    public void registerRowTrigger(RowComponent<?> rowComponent) {
        register(rowComponent.getWidget(), option.getKeys().getRowTrigger());
    }

    public void registerCategoryTrigger(CategoryComponent categoryComponent) {
        register(categoryComponent.getWidget(), option.getKeys().getCategoryTrigger());
    }

    public void registerHeaderTrigger(TableHeader tableHeader) {
        register(tableHeader, option.getKeys().getHeaderTrigger());
    }

    public void registerPageTrigger(MaterialDataPager pager) {
        register(pager, option.getKeys().getPageNext(), event -> pager.next());
        register(pager, option.getKeys().getPagePrevious(), event -> pager.previous());
    }

    public void register(MaterialWidget widget, int key) {
        register(widget, key, null);
    }

    public void register(MaterialWidget widget, int key, TriggerCallback callback) {
        if (widget != null) {
            widget.setTabIndex(isEnabled() ? 0 : -1);
            widget.registerHandler(widget.addKeyUpHandler(event -> {
                if (isEnabled() && event.getNativeKeyCode() == key) {
                    if (callback != null) {
                        callback.call(event);
                    } else {
                        $(widget.getElement()).click();
                    }
                }
            }));
        }
    }

    public boolean isEnabled() {
        return option.isEnabled();
    }
}
