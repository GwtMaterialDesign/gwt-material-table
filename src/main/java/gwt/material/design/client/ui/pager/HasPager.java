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
package gwt.material.design.client.ui.pager;

import gwt.material.design.client.ui.pager.actions.PageListBox;
import gwt.material.design.client.ui.pager.actions.PageSelection;

public interface HasPager {

    /**
     * Navigate to the next page
     */
    void next();

    /**
     * Navigate to the previous page
     */
    void previous();

    /**
     * Navigate to the last page
     */
    void lastPage();

    /**
     * Navigate to the first page
     */
    void firstPage();

    /**
     * Navigate to the given page
     */
    void gotoPage(int page);

    /**
     * Get the current page
     */
    int getCurrentPage();

    /**
     * Set the limit or row count of the navigation
     */
    void setLimit(int limit);

    /**
     * Check if we can call next()
     */
    boolean isNext();

    /**
     * Check if we can still previous()
     */
    boolean isPrevious();

    /**
     * Set the Page selection type on the data pager.
     * There are two available components available: {@link gwt.material.design.client.ui.pager.actions.PageNumberBox}
     * (By Default) and {@link PageListBox}
     */
    void setPageSelection(PageSelection pageSelection);

    PageSelection getPageSelection();
}
