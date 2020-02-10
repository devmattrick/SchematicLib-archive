package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.container.Palette;
import cx.matthew.schematiclib.math.Vector2D;
import cx.matthew.schematiclib.math.Vector3D;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.Iterator;

public class LegacyArrayBiomeVolume extends BiomeVolume implements ArrayVolume<Biome> {

    private Palette<Biome> palette;
    private int[] biomes;

    public LegacyArrayBiomeVolume(Vector2D dimensions, Palette<Biome> palette, int[] biomes) {
        super(new Vector3D(dimensions.getX(), 256, dimensions.getY()));
        this.palette = palette;
        this.biomes = Arrays.copyOf(biomes, biomes.length);
    }

    public LegacyArrayBiomeVolume(int width, int length, Palette<Biome> palette, int[] biomes) {
        this(new Vector2D(width, length), palette, biomes);
    }

    @Override
    public Biome get(Vector3D point) {
        return get(vec3ToVec2(point));
    }

    @Override
    public void set(Vector3D point, Biome value) {
        set(vec3ToVec2(point), value);
    }

    public Biome get(Vector2D point) {
        return get(coordToIndex(point));
    }

    public void set(Vector2D point, Biome value) {
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

    private int coordToIndex(Vector2D position) {
        return coordToIndex(position.getX(), position.getY());
    }

    private int coordToIndex(int x, int z) {
        int width = this.getDimensions().getX();

        return x + z * width;
    }

    private static Vector2D vec3ToVec2(Vector3D vec3d) {
        return new Vector2D(vec3d.getX(), vec3d.getZ());
    }

}
