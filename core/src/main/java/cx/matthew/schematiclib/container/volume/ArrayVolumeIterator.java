package cx.matthew.schematiclib.container.volume;

import java.util.Iterator;

/**
 * Iterator for array-backed volumes.
 *
 * @param <T> The type stored in this volume
 */
public class ArrayVolumeIterator<T> implements Iterator<T> {

    private ArrayVolume<T> volume;
    private int index = 0;

    public ArrayVolumeIterator(ArrayVolume<T> volume) {
        this.volume = volume;
    }

    @Override
    public boolean hasNext() {
        return index < volume.getIndexSize();
    }

    @Override
    public T next() {
        return volume.get(index++);
    }

}
