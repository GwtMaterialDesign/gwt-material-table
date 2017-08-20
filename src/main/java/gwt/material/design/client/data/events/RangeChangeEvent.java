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
import com.google.gwt.view.client.Range;

public class RangeChangeEvent extends GwtEvent<RangeChangeHandler> {

    public static final Type<RangeChangeHandler> TYPE = new Type<>();

    private final Range range;

    public RangeChangeEvent(Range range) {
        this.range = range;
    }

    public static void fire(HasHandlers source, Range range) {
        source.fireEvent(new RangeChangeEvent(range));
    }

    public Range getRange() {
        return range;
    }

    @Override
    public Type<RangeChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(RangeChangeHandler handler) {
        handler.onRangeChange(this);
    }
}
