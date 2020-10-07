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
package gwt.material.design.client.ui.accessibility;

import java.util.Arrays;

public class KeyCode {

    private KeyPrefix[] prefixes;
    private int keyCode;

    public KeyCode(int keyCode, KeyPrefix... prefixes) {
        this.prefixes = prefixes;
        this.keyCode = keyCode;
    }

    public KeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public KeyPrefix[] getPrefixes() {
        return prefixes;
    }

    public void setPrefixes(KeyPrefix[] prefixes) {
        this.prefixes = prefixes;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean hasPrefix(KeyPrefix prefix) {
        return prefix != null && prefixes != null && Arrays.asList(prefixes).contains(prefix);
    }

    public boolean hasAnyPrefix() {
        return prefixes != null && prefixes.length > 0;
    }
}
