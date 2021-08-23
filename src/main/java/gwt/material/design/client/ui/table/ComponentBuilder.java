package gwt.material.design.client.ui.table;


import com.google.gwt.user.client.ui.Panel;

public interface ComponentBuilder {

    default void buildActions(Panel toolPanel) {}

    default <T> void buildFooter(TableFooter<T> footer) {}
}
