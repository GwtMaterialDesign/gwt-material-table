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
package gwt.material.design.client;

import com.google.gwt.junit.client.GWTTestCase;
import gwt.material.design.client.resources.MaterialResources;
import gwt.material.design.client.resources.WithJQueryResources;
import org.junit.Test;

import static gwt.material.design.jquery.client.api.JQuery.$;

public class MaterialTableTestComponent extends GWTTestCase {

    @Override
    public String getModuleName() {
        return "gwt.material.design.GwtMaterialTable";
    }

    @Override
    protected void gwtSetUp() throws Exception {
        super.gwtSetUp();
        setup();
    }

    public void setup() {
        // Test JQuery
        MaterialDesign.injectJs(WithJQueryResources.INSTANCE.jQuery());
        assertTrue(MaterialDesign.isjQueryLoaded());
        // Test Materialize
        MaterialDesign.injectJs(MaterialResources.INSTANCE.materializeJs());
        assertTrue(MaterialDesign.isMaterializeLoaded());
        // gwt-material-jquery Test
        assertNotNull($("body"));
    }

    @Test
    public void testStandardTable() {
        new StandardTableTest().init();
    }

    @Test
    public void testInfiniteTable() {
        new InfiniteTableTest().init();
    }
}
