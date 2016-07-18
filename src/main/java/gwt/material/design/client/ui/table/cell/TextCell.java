package gwt.material.design.client.ui.table.cell;

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


import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

/**
 * A {@link com.google.gwt.cell.client.Cell} used to render text.
 *
 * @author Ben Dol
 */
public class TextCell extends AbstractSafeHtmlCell<String> {

    /**
     * Constructs a TextCell that uses a {@link SimpleSafeHtmlRenderer} to render
     * its text.
     */
    public TextCell() {
        super(SimpleSafeHtmlRenderer.getInstance());
    }

    /**
     * Constructs a TextCell that uses the provided {@link SafeHtmlRenderer} to
     * render its text.
     * 
     * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
     */
    public TextCell(SafeHtmlRenderer<String> renderer) {
        super(renderer);
    }

    @Override
    protected void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
        if (data != null) {
            sb.append(data);
        }
    }
}
