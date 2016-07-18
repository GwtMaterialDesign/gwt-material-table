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


import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

/**
 * Client bundle for data pager component
 * @author kevzlou7979
 */
public interface MaterialDataPagerClientBundle extends ClientBundle {

    MaterialDataPagerClientBundle INSTANCE = GWT.create(MaterialDataPagerClientBundle.class);

    @ClientBundle.Source("resources/css/data-pager.css")
    TextResource dataPagerDebugCss();

    @ClientBundle.Source("resources/css/data-pager.min.css")
    TextResource dataPagerCss();
}
