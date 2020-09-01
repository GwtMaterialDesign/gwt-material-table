package gwt.material.design.client.ui.accessibility;

public class AccessibilityOption {

    private boolean enabled;
    private AccessibilityKeyCodes keys;

    public AccessibilityOption() {
        enabled = true;
        keys = new AccessibilityKeyCodes();
    }

    public AccessibilityOption(boolean enabled, AccessibilityKeyCodes keys) {
        this.enabled = enabled;
        this.keys = keys;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public AccessibilityKeyCodes getKeys() {
        return keys;
    }

    public void setKeys(AccessibilityKeyCodes keys) {
        this.keys = keys;
    }
}
