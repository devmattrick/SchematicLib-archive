package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.math.Vector3D;

/**
 * A spacial container of objects.
 *
 * @param <T> The type of object to be held
 */
public interface Volume<T> extends Iterable<T> {

    Vector3D getDimensions();

    T get(Vector3D point);

    default T get(int x, int y, int z) {
        return get(new Vector3D(x, y, z));
    }

    void set(Vector3D point, T value);

    default void set(int x, int y, int z, T value) {
        set(new Vector3D(x, y, z), value);
    }

}
