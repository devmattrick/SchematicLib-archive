package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.math.Vector3D;
import org.bukkit.block.Biome;

public class EmptyBiomeVolume extends BiomeVolume implements EmptyVolume<Biome> {

    public static final EmptyBiomeVolume EMPTY_VOLUME = new EmptyBiomeVolume();

    private EmptyBiomeVolume() {
        super(Vector3D.ZERO);
    }

}
