package gwt.material.design.client.data.factory;

public enum CategoryState {

    ENABLED("enabled"),
    DISABLED("disabled"),
    HIDDEN("hidden");

    private String name;

    CategoryState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
