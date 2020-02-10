package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.math.Vector3D;
import org.bukkit.block.Biome;

/**
 * An area of biomes.
 */
public abstract class BiomeVolume implements Volume<Biome> {

    public static final Biome DEFAULT_BIOME = Biome.PLAINS;

    private Vector3D dimensions;

    public BiomeVolume(Vector3D dimensions) {
        this.dimensions = dimensions;
    }

    public Vector3D getDimensions() {
        return dimensions;
    }

}
