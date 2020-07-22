/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2020 GwtMaterialDesign
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
package gwt.material.design.client;

import com.google.gwt.junit.tools.GWTTestSuite;
import gwt.material.design.client.data.BaseRendererTest;
import gwt.material.design.client.data.infinite.InfiniteDataViewTest;
import gwt.material.design.client.ui.table.MaterialDataTableTest;
import gwt.material.design.client.ui.table.MaterialInfiniteDataTable;
import gwt.material.design.client.ui.table.MaterialInfiniteDataTableTest;
import junit.framework.Test;
import junit.framework.TestSuite;

public class DataTableTestSuite extends GWTTestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test Suite for DataTable");

        // Addins Tests
        suite.addTestSuite(MaterialDataTableTest.class);
        suite.addTestSuite(MaterialInfiniteDataTableTest.class);
        suite.addTestSuite(InfiniteDataViewTest.class);
        suite.addTestSuite(BaseRendererTest.class);
        return suite;
    }
}
