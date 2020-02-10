package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.math.Vector3D;

import java.util.Collections;
import java.util.Iterator;

public interface EmptyVolume<T> extends Volume<T> {

    @Override
    default Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @Override
    default T get(Vector3D point) {
        return null;
    }

    @Override
    default void set(Vector3D point, T value) {}

}
