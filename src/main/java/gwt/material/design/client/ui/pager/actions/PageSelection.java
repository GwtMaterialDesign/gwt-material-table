/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2018 GwtMaterialDesign
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
package gwt.material.design.client.ui.pager.actions;

import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import gwt.material.design.client.ui.pager.MaterialDataPager;

/**
 * Provides a selection control to {@link MaterialDataPager} pages.
 * Call {@link MaterialDataPager#setPageSelection(PageSelection)} for applying the component.
 * Two available components are available already, see {@link PageNumberBox} and {@link PageListBox}
 *
 * @author kevzlou7979
 */
public interface PageSelection extends HasValue<Integer>, IsWidget {

    /**
     * Will load and initialize all handlers.
     */
    void load();

    /**
     * Will automatically updated the page number based on the current page
     */
    void updatePageNumber(int currentPage);

    /**
     * Clear or reset the page number
     */
    void clearPageNumber();

    /**
     * Will set the current {@link MaterialDataPager} for it's controller.
     */
    void setPager(MaterialDataPager<?> pager);

    /**
     * Will set the component label
     */
    void setLabel(String label);

    /**
     * Will set the total number of pages
     */
    void updateTotalPages(int totalPages);
}
