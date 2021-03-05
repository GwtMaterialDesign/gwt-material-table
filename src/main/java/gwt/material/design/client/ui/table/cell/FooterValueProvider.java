package gwt.material.design.client.ui.table.cell;

import java.util.List;

public interface FooterValueProvider<T> {
    String getValue(List<T> entireData);
}