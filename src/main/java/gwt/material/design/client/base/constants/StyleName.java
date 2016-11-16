/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2016 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package gwt.material.design.client.base.constants;

public enum StyleName {
    Z_INDEX("zIndex"),
    WIDTH("width"),
    VISIBILITY("visibility"),
    TOP("top"),
    TEXT_DECORATION("textDecoration"),
    RIGHT("right"),
    POSITION("position"),
    PADDING_TOP("paddingTop"),
    PADDING_RIGHT("paddingRight"),
    PADDING_LEFT("paddingLeft"),
    PADDING_BOTTOM("paddingBottom"),
    PADDING("padding"),
    OVERFLOW("overflow"),
    OVERFLOW_X("overflowX"),
    OVERFLOW_Y("overflowY"),
    OPACITY("opacity"),
    MARGIN_TOP("marginTop"),
    MARGIN_RIGHT("marginRight"),
    MARGIN_LEFT("marginLeft"),
    MARGIN_BOTTOM("marginBottom"),
    MARGIN("margin"),
    LIST_STYLE_TYPE("listStyleType"),
    LEFT("left"),
    HEIGHT("height"),
    FONT_WEIGHT("fontWeight"),
    FONT_STYLE("fontStyle"),
    FONT_SIZE("fontSize"),
    DISPLAY("display"),
    CURSOR("cursor"),
    COLOR("color"),
    CLEAR("clear"),
    BOTTOM("bottom"),
    BORDER_WIDTH("borderWidth"),
    BORDER_STYLE("borderStyle"),
    BORDER_COLOR("borderColor"),
    BACKGROUND_IMAGE("backgroundImage"),
    BACKGROUND_COLOR("backgroundColor"),
    VERTICAL_ALIGN("verticalAlign"),
    TABLE_LAYOUT("tableLayout"),
    TEXT_ALIGN("textAlign"),
    TEXT_INDENT("textIndent"),
    TEXT_JUSTIFY("textJustify"),
    TEXT_OVERFLOW("textOverflow"),
    TEXT_TRANSFORM("textTransform"),
    OUTLINE_WIDTH("outlineWidth"),
    OUTLINE_STYLE("outlineStyle"),
    OUTLINE_COLOR("outlineColor"),
    LINE_HEIGHT("lineHeight"),
    WHITE_SPACE("whiteSpace");

    String styleName;
    StyleName(String styleName) {
        this.styleName = styleName;
    }

    public String styleName() {
        return styleName;
    }
}
