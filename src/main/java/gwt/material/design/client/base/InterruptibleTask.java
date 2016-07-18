package gwt.material.design.client.base;

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

import com.google.gwt.user.client.Timer;

/**
 * A <code>Timer</code> that is canceled if a new request is made.
 *
 * @author Ben Dol
 */
public abstract class InterruptibleTask {

    private Timer timer;

    /**
     * Creates a new delayed task.
     */
    public InterruptibleTask() {
        timer = new Timer() {
            public void run() {
                onExecute();
            }
        };
    }

    /**
     * Cancels the task.
     */
    public void cancel() {
        timer.cancel();
    }

    /**
     * Cancels any running timers and starts a new one.
     *
     * @param delay the delay in ms
     */
    public void delay(int delay) {
        timer.cancel();
        if (delay > 0) {
            timer.schedule(delay);
        } else {
            timer.run();
        }
    }

    /**
     * Called when the task should execute.
     */
    public abstract void onExecute();
}
