package cx.matthew.schematiclib.container.volume;

/**
 * An array-backed volume.
 *
 * @param <T> The type stored in this volume
 */
public interface ArrayVolume<T> extends Volume<T> {

    /**
     * Gets the object stored at an index
     *
     * @param index The index of this object
     * @return The object stored at index
     */
    T get(int index);

    /**
     * Stores an object at an index
     * @param index The index
     * @param value The object to store at index
     */
    void set(int index, T value);

    /**
     * Gets the size of the index (the total number of objects stored in this volume).
     *
     * @return The size of the index.
     */
    int getIndexSize();

}
