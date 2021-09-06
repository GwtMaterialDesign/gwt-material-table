/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
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
package gwt.material.design.client.data;

import com.google.gwt.dom.client.Style;
import gwt.material.design.client.base.HasDensity;

public interface ViewSettings extends HasDensity {

    /**
     * True if we are using sticky table header.
     */
    boolean isUseStickyHeader();

    /**
     * Enable the use of sticky table header bar.
     */
    void setUseStickyHeader(boolean stickyHeader);

    /**
     * True if we are using sticky table footer.
     */
    boolean isUseStickyFooter();

    /**
     * Enable the use of sticky table footer.
     */
    void setUseStickyFooter(boolean stickyFooter);

    /**
     * Is the data view using categories where possible.
     */
    boolean isUseCategories();

    /**
     * Allow the data view to generate categories,
     * or use added categories for row data.
     */
    void setUseCategories(boolean useCategories);

    /**
     * Is the data view using loading overlay mask.
     */
    boolean isUseLoadOverlay();

    /**
     * Use the loading overlay mask.
     */
    void setUseLoadOverlay(boolean useLoadOverlay);

    /**
     * Is the data view using row expansion.
     */
    boolean isUseRowExpansion();

    /**
     * Use row expansion functionality, giving the user
     * a way to expand rows for extra information.
     */
    void setUseRowExpansion(boolean useRowExpansion);

    /**
     * Get the long press duration requirement.
     */
    int getLongPressDuration();

    /**
     * Set the long press duration requirement.
     */
    void setLongPressDuration(int longPressDuration);

    /**
     * Set the table body height.
     */
    void setHeight(String height);

    /**
     * Get the table body height.
     */
    String getHeight();

    /**
     * Set the table layout.
     */
    void setTableLayout(Style.TableLayout layout);
}
