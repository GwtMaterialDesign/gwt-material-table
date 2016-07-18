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
import gwt.material.design.client.constants.IconType;
import gwt.material.design.client.constants.WavesType;
import gwt.material.design.client.data.DataSource;
import gwt.material.design.client.ui.*;
import gwt.material.design.client.ui.html.Span;
import gwt.material.design.client.ui.table.MaterialDataTable;

/**
 * Base class implementation of MaterialDataPager which builds the User Interface of the overall pager
 * @author kevzlou7979
 */
public class MaterialDataPagerBase<T> extends MaterialRow {

    protected final MaterialDataTable<T> table;

    // Number Page Panel
    private MaterialColumn numPagePanel = new MaterialColumn();
    private Span pageLabel = new Span("Page");
    private MaterialListBox listPages = new MaterialListBox();

    // Rows per Page Panel
    private MaterialColumn rowsPerPagePanel = new MaterialColumn();
    private Span rowsPerPageLabel = new Span("Rows per page");
    private MaterialListBox listRowsPerPage = new MaterialListBox();

    // Action Page Panel
    private MaterialColumn actionPagePanel = new MaterialColumn();
    private Span actionLabel = new Span();
    private MaterialIcon iconNext = new MaterialIcon(IconType.KEYBOARD_ARROW_RIGHT);
    private MaterialIcon iconPrev = new MaterialIcon(IconType.KEYBOARD_ARROW_LEFT);

    public MaterialDataPagerBase(MaterialDataTable<T> table, DataSource<T> dataSource) {
        super(Document.get().createDivElement(), "data-pager", "row");
        this.table = table;
        this.table.setDataSource(dataSource);
        buildNumPagePanel();
        buildRowsPerPagePanel();
        buildActionPanel();
    }

    /**
     * Build the number page panel - where user can change the page number into a listbox
     */
    private void buildNumPagePanel() {
        numPagePanel.setInitialClasses("num-page-panel");
        numPagePanel.setGrid("s12 m4 l3");
        numPagePanel.setOffset("l3");
        numPagePanel.add(listPages);
        numPagePanel.add(pageLabel);
        add(numPagePanel);
    }

    /**
     * Build the rows per page panel - where you can set the range of row count into listbox
     */
    private void buildRowsPerPagePanel() {
        rowsPerPagePanel.setInitialClasses("rows-per-page-panel");
        rowsPerPagePanel.setGrid("s12 m4 l3");
        rowsPerPagePanel.add(listRowsPerPage);
        rowsPerPagePanel.add(rowsPerPageLabel);
        add(rowsPerPagePanel);
    }

    /**
     * Build the action panel - contains a page detail including the arrow next / previous icons.
     */
    private void buildActionPanel() {
        actionPagePanel.setInitialClasses("action-page-panel");
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

    /**
     * Get the data table implemented and used by this data pager.
     */
    public MaterialDataTable getTable() {
        return table;
    }

    /**
     * Get the tables data source.
     */
    public DataSource<T> getDataSource() {
        return table.getDataSource();
    }

    /**
     * Get the number page panel.
     */
    public MaterialColumn getNumPagePanel() {
        return numPagePanel;
    }

    /**
     * Get the page label.
     */
    public Span getPageLabel() {
        return pageLabel;
    }

    /**
     * Get the list pages list box.
     */
    public MaterialListBox getListPages() {
        return listPages;
    }

    /**
     * Get rows per page panel.
     */
    public MaterialColumn getRowsPerPagePanel() {
        return rowsPerPagePanel;
    }

    /**
     * Get rows per page label.
     */
    public Span getRowsPerPageLabel() {
        return rowsPerPageLabel;
    }

    /**
     * Get list rows per page listbox.
     */
    public MaterialListBox getListRowsPerPage() {
        return listRowsPerPage;
    }

    /**
     * Get the action page panel.
     */
    public MaterialColumn getActionPagePanel() {
        return actionPagePanel;
    }

    /**
     * Get the action label.
     */
    public Span getActionLabel() {
        return actionLabel;
    }

    /**
     * Get the icon next.
     */
    public MaterialIcon getIconNext() {
        return iconNext;
    }

    /**
     * Get the icon previous.
     */
    public MaterialIcon getIconPrev() {
        return iconPrev;
    }
}
