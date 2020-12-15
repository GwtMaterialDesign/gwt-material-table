package gwt.material.design.client.data.factory;

public enum CategoryState {

    ENABLE("enable"),
    DISABLE("disable"),
    HIDDEN("hidden");

    private String name;

    CategoryState(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
