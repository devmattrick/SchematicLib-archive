package cx.matthew.schematiclib.container;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.BitSet;
import java.util.Optional;

/**
 * A mapping of an integer to an object. This format is primarily used for BlockStates and Entities in schematics.
 *
 * @param <T> The type to be mapped
 */
public class Palette<T> {

    // Much of this implementation was inspired by Sponge's implementation:
    // https://github.com/SpongePowered/SpongeCommon/blob/aa2c8c53b4f9f40297e6a4ee281bee4f4ce7707b/src/main/java/org/spongepowered/common/world/schematic/BimapPalette.java

    private final BiMap<Integer, T> palette;
    private final BiMap<T, Integer> paletteInverse;
    private final BitSet allocation = new BitSet();

    public Palette() {
        palette = HashBiMap.create();
        paletteInverse = palette.inverse();
    }

    /**
     * Get the stored value for the given id
     *
     * @param id The id to get
     * @return An Optional conditionally containing the value stored in the given id
     */
    public Optional<T> get(int id) {
        return Optional.ofNullable(palette.get(id));
    }

    /**
     * Get the stored id for the given value
     *
     * @param value The value whose id to get
     * @return An optional conditionally containing the id for the given value
     */
    public Optional<Integer> get(T value) {
        return Optional.ofNullable(paletteInverse.get(value));
    }

    /**
     * Get the stored id for the given value, creating and returning a new id if missing
     *
     * @param value The value whose id to get
     * @return The existing or created id for the given value
     */
    public int getOrAssign(T value) {
        Optional<Integer> id = get(value);

        if (!id.isPresent()) {
            int next = allocation.nextClearBit(0);
            assign(next, value);

            return next;
        }

        return id.get();
    }

    public void assign(int id, T value) {
        allocation.set(id);
        palette.put(id, value);
    }

    public void remove(int id) {
        allocation.clear(id);
        palette.remove(id);
    }

    public void remove(T value) {
        Optional<Integer> id = get(value);
        if (!id.isPresent()) return;

        remove(id.get());
    }

    public int size() {
        return palette.size();
    }

}
