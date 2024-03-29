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
package gwt.material.design.client.data.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class CategoryClosedEvent extends GwtEvent<CategoryClosedHandler> {

    public static final Type<CategoryClosedHandler> TYPE = new Type<>();

    public static void fire(HasHandlers source, String id, String name) {
        source.fireEvent(new CategoryClosedEvent(id, name));
    }

    private final String name;
    private final String id;

    public CategoryClosedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public Type<CategoryClosedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(CategoryClosedHandler handler) {
        handler.onCategoryClosed(this);
    }
}
