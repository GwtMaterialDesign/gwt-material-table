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
