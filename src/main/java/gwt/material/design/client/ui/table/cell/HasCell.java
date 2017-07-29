package gwt.material.design.client.ui.table.cell;

import com.google.gwt.cell.client.Cell;

/**
 * An interface for extracting a value of type C from an underlying data value
 * of type T, provide a {@link Cell} to render that value.
 * 
 * @param <T> the underlying data type
 * @param <C> the cell data type
 */
public interface HasCell<T, C> {

    /**
     * Returns the {@link Cell} of type C.
     *
     * @return a Cell
     */
    Cell<C> getCell();

    /**
     * Returns the value of type C extracted from the record of type T.
     *
     * @param object a record of type T
     * @return a value of type C suitable for passing to the cell
     */
    C getValue(T object);
}
