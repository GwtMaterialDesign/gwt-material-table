package gwt.material.design.client.ui.table.cell;

import com.google.gwt.dom.client.Style;
import gwt.material.design.client.base.constants.StyleName;

import java.util.HashMap;

public class FrozenProperties extends HashMap<StyleName, String> {

    private String width;

    public FrozenProperties(String width) {
        this.width = width;
    }

    /**
     * Set a style property using its name as the key. Please ensure the style name and value
     * are appropriately configured or it may result in unexpected behavior.
     *
     * @param styleName the style name as seen here {@link Style#STYLE_Z_INDEX} for example.
     * @param value the string value required for the given style property.
     */
    public FrozenProperties setStyleProperty(StyleName styleName, String value) {
        put(styleName, value);
        return this;
    }

    /**
     * Get a styles property.
     * @param styleName the styles name as represented in a {@link Style} class.
     * @return null if the style property is not set.
     */
    public String getStyleProperty(StyleName styleName) {
        return get(styleName);
    }

    public String getWidth() {
        return width;
    }

    public FrozenProperties setWidth(String width) {
        this.width = width;
        return this;
    }
}
