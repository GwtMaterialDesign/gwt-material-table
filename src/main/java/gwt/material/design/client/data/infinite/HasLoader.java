package gwt.material.design.client.data.infinite;

public interface HasLoader {

    int getLoaderDelay();

    void setLoaderDelay(int loaderDelay);

    int getLoaderBuffer();

    /**
     * The amount of data that will buffer your start index and view size.
     * This can be useful in removing loading delays. Defaults to 10.
     */
    void setLoaderBuffer(int loaderBuffer);

    /**
     * Is the loader loading.
     */
    boolean isLoading();
}
