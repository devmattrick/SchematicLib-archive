package cx.matthew.schematiclib.schematic;

import cx.matthew.schematiclib.container.volume.BiomeVolume;
import cx.matthew.schematiclib.container.volume.BlockVolume;
import cx.matthew.schematiclib.container.volume.EmptyBiomeVolume;
import cx.matthew.schematiclib.math.Vector3D;
import cx.matthew.schematiclib.nbt.TagCompound;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Schematic {

    private BlockVolume blocks;
    private Map<Vector3D, TagCompound> entities;
    private BiomeVolume biomes;

    public Schematic(BlockVolume blocks, Map<Vector3D, TagCompound> entities, BiomeVolume biomes) {
        this.blocks = Objects.requireNonNull(blocks);
        this.entities = Collections.unmodifiableMap(entities != null ? entities : Collections.emptyMap());
        this.biomes = biomes != null ? biomes : EmptyBiomeVolume.EMPTY_VOLUME;
    }

    public BlockVolume getBlocks() {
        return blocks;
    }

    public Map<Vector3D, TagCompound> getEntities() {
        return entities;
    }

    public BiomeVolume getBiomes() {
        return biomes;
    }

}
