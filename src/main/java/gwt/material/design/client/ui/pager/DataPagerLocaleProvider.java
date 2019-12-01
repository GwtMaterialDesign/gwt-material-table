package gwt.material.design.client.ui.pager;

public interface DataPagerLocaleProvider {

    default String RowsPerPage() {
        return "Rows per page";
    }

    default String Page() {
        return "Page";
    }

    default String of() {
        return "of";
    }
}
