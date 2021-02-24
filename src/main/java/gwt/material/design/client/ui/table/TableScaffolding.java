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
package gwt.material.design.client.ui.table;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;

/**
 * Table scaffolding that will construct the Panels for the table foundation.
 *
 * @author Ben Dol
 */
public interface TableScaffolding {

    void build();
    void apply(HasWidgets container);

    Panel getTableBody();
    Panel getTopPanel();
    Panel getInfoPanel();
    Panel getToolPanel();
    Panel getXScrollPanel();
    Panel getFooterPanel();
    Table getTable();
}
