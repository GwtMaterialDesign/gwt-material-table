package gwt.material.design.client.ui.pager;

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

import com.google.gwt.dom.client.Document;
import gwt.material.design.client.base.constants.TableCssName;
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.ui.MaterialColumn;
import gwt.material.design.client.ui.MaterialIcon;
import gwt.material.design.client.ui.MaterialListValueBox;
import gwt.material.design.client.ui.MaterialRow;
import gwt.material.design.client.ui.html.Span;

/**
 * Base class implementation of MaterialDataPager which builds the User Interface of the overall pager
 * @author kevzlou7979
 */
public class MaterialDataPagerBase<T> extends MaterialRow {

    // Number Page Panel
    protected MaterialColumn numPagePanel = new MaterialColumn();
    protected Span pageLabel = new Span("Page");
    protected MaterialListValueBox<Integer> listPages = new MaterialListValueBox<Integer>();

    // Rows per Page Panel
    protected MaterialColumn rowsPerPagePanel = new MaterialColumn();
    protected Span rowsPerPageLabel = new Span("Rows per page");
    protected MaterialListValueBox<Integer> listLimitOptions = new MaterialListValueBox<Integer>();

    // Action Page Panel
    protected MaterialColumn actionPagePanel = new MaterialColumn();
    protected Span actionLabel = new Span();
    protected MaterialIcon iconNext = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
    protected MaterialIcon iconPrev = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);

    public MaterialDataPagerBase() {
        super(Document.get().createDivElement(), TableCssName.DATA_PAGER, TableCssName.ROW);
        buildNumPagePanel();
        buildLimitOptionsPanel();
        buildActionPanel();
    }

    /**
     * Build the number page panel - where user can change the page number into a listbox
     */
    private void buildNumPagePanel() {
        numPagePanel.setInitialClasses(TableCssName.NUM_PAGE_PANEL);
        numPagePanel.setGrid("s12 m4 l3");
        numPagePanel.setOffset("l3");
        numPagePanel.add(listPages);
        numPagePanel.add(pageLabel);
        add(numPagePanel);
    }

    /**
     * Build the rows per page panel - where you can set the range of row count into listbox
     */
    private void buildLimitOptionsPanel() {
        rowsPerPagePanel.setInitialClasses(TableCssName.ROWS_PER_PAGE_PANEL);
        rowsPerPagePanel.setGrid("s12 m4 l3");
        rowsPerPagePanel.add(listLimitOptions);
        rowsPerPagePanel.add(rowsPerPageLabel);
        add(rowsPerPagePanel);
    }

    /**
     * Build the action panel - contains a page detail including the arrow next / previous icons.
     */
    private void buildActionPanel() {
        actionPagePanel.setInitialClasses(TableCssName.ACTION_PAGE_PANEL);
        actionPagePanel.setGrid("s12 m4 l3");
        actionLabel.setText("41 - 640 of 2014");
        actionPagePanel.add(iconNext);
        iconNext.setWaves(WavesType.DEFAULT);
        iconNext.setCircle(true);
        actionPagePanel.add(iconPrev);
        iconPrev.setWaves(WavesType.DEFAULT);
        iconPrev.setCircle(true);
        actionPagePanel.add(actionLabel);
        add(actionPagePanel);
    }
}
