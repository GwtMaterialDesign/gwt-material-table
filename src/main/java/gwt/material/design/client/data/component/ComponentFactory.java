package gwt.material.design.client.data.component;

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


import gwt.material.design.client.data.AbstractDataView;
import gwt.material.design.client.data.component.Component;

/**
 * Component factory is used to generate {@link Component}'s.
 * This is referenced each time a new row is added within {@link AbstractDataView}.
 *
 * The objective of the ComponentFactory is to define custom data components
 * based on the row being added.
 * @param <T>
 * @author Ben Dol
 */
public interface ComponentFactory<T extends Component, M> {

    /**
     * Generate a {@link Component}.
     * @param model model to define the component.
     * @return a fully formed {@link Component}.
     */
    T generate(M model);
}
