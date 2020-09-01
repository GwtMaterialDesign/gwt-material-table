package gwt.material.design.client.ui.accessibility;

import com.google.gwt.event.dom.client.KeyCodes;

public class AccessibilityKeyCodes {

    private int rowTrigger = KeyCodes.KEY_ENTER;
    private int headerTrigger = KeyCodes.KEY_ENTER;
    private int categoryTrigger = KeyCodes.KEY_ENTER;
    private int pageNext = KeyCodes.KEY_RIGHT;
    private int pagePrevious = KeyCodes.KEY_LEFT;
    private int infiniteLoadNext = KeyCodes.KEY_DOWN;
    private int infiniteLoadPrevious = KeyCodes.KEY_DOWN;

    public AccessibilityKeyCodes() {
    }

    public int getRowTrigger() {
        return rowTrigger;
    }

    public void setRowTrigger(int rowTrigger) {
        this.rowTrigger = rowTrigger;
    }

    public int getHeaderTrigger() {
        return headerTrigger;
    }

    public void setHeaderTrigger(int headerTrigger) {
        this.headerTrigger = headerTrigger;
    }

    public int getCategoryTrigger() {
        return categoryTrigger;
    }

    public void setCategoryTrigger(int categoryTrigger) {
        this.categoryTrigger = categoryTrigger;
    }

    public int getPageNext() {
        return pageNext;
    }

    public void setPageNext(int pageNext) {
        this.pageNext = pageNext;
    }

    public int getPagePrevious() {
        return pagePrevious;
    }

    public void setPagePrevious(int pagePrevious) {
        this.pagePrevious = pagePrevious;
    }

    public int getInfiniteLoadNext() {
        return infiniteLoadNext;
    }

    public void setInfiniteLoadNext(int infiniteLoadNext) {
        this.infiniteLoadNext = infiniteLoadNext;
    }

    public int getInfiniteLoadPrevious() {
        return infiniteLoadPrevious;
    }

    public void setInfiniteLoadPrevious(int infiniteLoadPrevious) {
        this.infiniteLoadPrevious = infiniteLoadPrevious;
    }
}
