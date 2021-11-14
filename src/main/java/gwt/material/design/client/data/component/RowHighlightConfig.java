/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2021 GwtMaterialDesign
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
package gwt.material.design.client.data.component;

import gwt.material.design.client.ui.animate.Transition;

public class RowHighlightConfig {

    private int offsetTop;
    private int duration;
    private int delay;
    private Transition transition;

    public RowHighlightConfig() {
    }

    public RowHighlightConfig(int offsetTop, int duration, int delay, Transition transition) {
        this.offsetTop = offsetTop;
        this.duration = duration;
        this.delay = delay;
        this.transition = transition;
    }

    public int getOffsetTop() {
        return offsetTop;
    }

    public void setOffsetTop(int offsetTop) {
        this.offsetTop = offsetTop;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }
}
