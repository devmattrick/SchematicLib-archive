package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.container.Palette;
import cx.matthew.schematiclib.math.Vector3D;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayBiomeVolume extends BiomeVolume implements ArrayVolume<Biome> {

    private Palette<Biome> palette;
    private int[] biomes;

    public ArrayBiomeVolume(Vector3D dimensions, Palette<Biome> palette, int[] biomes) {
        super(dimensions);
        this.palette = palette;
        this.biomes = Arrays.copyOf(biomes, biomes.length);
    }

    public ArrayBiomeVolume(int width, int height, int length, Palette<Biome> palette, int[] biomes) {
        this(new Vector3D(width, height, length), palette, biomes);
    }

    @Override
    public Biome get(Vector3D point) {
        return get(coordToIndex(point));
    }

    @Override
    public void set(Vector3D point, Biome value) {
        set(coordToIndex(point), value);
    }

    @Override
    public Biome get(int index) {
        return palette.get(biomes[index]).orElse(DEFAULT_BIOME);
    }

    @Override
    public void set(int index, Biome value) {
        biomes[index] = palette.getOrAssign(value);
    }

    @Override
    public Iterator<Biome> iterator() {
        return new ArrayVolumeIterator<>(this);
    }

    @Override
    public int getIndexSize() {
        return this.biomes.length;
    }

    private int coordToIndex(Vector3D position) {
        return coordToIndex(position.getX(), position.getY(), position.getZ());
    }

    private int coordToIndex(int x, int y, int z) {
        int width = this.getDimensions().getX();
        int length = this.getDimensions().getZ();

        return x + z * width + y * width * length;
    }

}
