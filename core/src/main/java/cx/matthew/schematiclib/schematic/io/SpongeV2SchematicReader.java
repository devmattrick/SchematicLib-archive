package cx.matthew.schematiclib.schematic.io;

import cx.matthew.schematiclib.container.BlockHolder;
import cx.matthew.schematiclib.container.Palette;
import cx.matthew.schematiclib.container.volume.ArrayBiomeVolume;
import cx.matthew.schematiclib.container.volume.ArrayBlockVolume;
import cx.matthew.schematiclib.container.volume.BiomeVolume;
import cx.matthew.schematiclib.container.volume.BlockVolume;
import cx.matthew.schematiclib.math.Vector3D;
import cx.matthew.schematiclib.nbt.*;
import cx.matthew.schematiclib.nbt.io.NBTInputStream;
import cx.matthew.schematiclib.schematic.Schematic;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.block.data.BlockData;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SpongeV2SchematicReader implements SchematicReader {

    private NBTInputStream in;

    public SpongeV2SchematicReader(File file) throws FileNotFoundException {
        this(new FileInputStream(file));
    }

    public SpongeV2SchematicReader(InputStream in) {
        this.in = new NBTInputStream(in);
    }

    @Override
    public Schematic read() throws IOException {
        NamedTag namedTag = this.in.readTags();
        Tag tag = namedTag.getTag();

        if (!(tag instanceof TagCompound)) throw new SchematicReaderException("Root tag of schematic must be a compound tag.");

        NBTStructure structure = new NBTStructure((TagCompound) tag);

        int version = ReaderUtil.require(structure.getInt("Version"), "Version");
        if (version != 2) {
            throw new SchematicReaderException("Unsupported Sponge Schematic version: " + version);
        }

        // We only support 1.13 (1519) or above since we expect there to be "flattened" ids
        // The Sponge Schematic format specifies the tag as (inconsistent) "Data Version", but WorldEdit uses "DataVersion"
        int dataVersion = structure.getInt("Data Version")
                .orElse(structure.getInt("DataVersion")
                .orElseThrow(() -> new SchematicReaderException("Missing Data Version (or DataVersion) tag.")));
        if (dataVersion < 1519) {
            throw new SchematicReaderException("Only data version 1519 or above is currently supported. Provided: " + dataVersion);
        }

        int width = ReaderUtil.require(structure.getUnsignedShort("Width"), "Width");
        int height = ReaderUtil.require(structure.getUnsignedShort("Height"), "Height");
        int length = ReaderUtil.require(structure.getUnsignedShort("Length"), "Length");

        BlockVolume blocks = readBlockVolume(structure, width, height, length);
        Map<Vector3D, TagCompound> entities = readEntities(structure);
        BiomeVolume biomes = readBiomeVolume(structure, width, length);

        return new Schematic(blocks, entities, biomes);
    }

    private BlockVolume readBlockVolume(NBTStructure structure, int width, int height, int length) throws SchematicReaderException {
        Palette<BlockData> palette = readBlockDataPalette(structure);
        Map<Vector3D, TagCompound> blockEntities = readBlockEntities(structure);

        BlockVolume blocks = new ArrayBlockVolume(width, height, length);

        Integer[] data = ReaderUtil.require(structure.getVarintArray("BlockData"), "BlockData");
        for (int i = 0; i < data.length; i++) {
            int y = i / (width * length);
            int z = (i % (width * length)) / width;
            int x = (i % (width * length)) % width;
            BlockData state = palette.get(data[i]).get();

           blocks.set(x, y, z, new BlockHolder(state, blockEntities.get(new Vector3D(x, y, z))));
        }

        return blocks;
    }

    private Palette<BlockData> readBlockDataPalette(NBTStructure structure) throws SchematicReaderException {
        Map<String, Tag> tagBlockPalette = ReaderUtil.require(structure.getCompound("Palette"), "Palette");

        Palette<BlockData> blockPalette = new Palette<>();
        for (Map.Entry<String, Tag> entry : tagBlockPalette.entrySet()) {
            Tag entryTag = entry.getValue();
            if (!(entryTag instanceof TagInt)) {
                Bukkit.getLogger().warning("Expected an IntTag for block palette, got " + entryTag.toString());
                continue;
            }

            blockPalette.assign(((TagInt) entryTag).getValue(), Bukkit.createBlockData(entry.getKey()));
        }

        return blockPalette;
    }

    private Map<Vector3D, TagCompound> readBlockEntities(NBTStructure structure) throws SchematicReaderException {
        Map<Vector3D, TagCompound> blockEntities = new HashMap<>();

        List<? extends Tag> blockEntitiesTag = ReaderUtil.require(structure.getList("TileEntities"), "BlockEntities");
        for (Tag listTag : blockEntitiesTag) {
            if (!(listTag instanceof TagCompound)) {
                Bukkit.getLogger().warning("Expected a CompoundTag for block entity, got " + listTag.toString());
                continue;
            }
            TagCompound compound = (TagCompound) listTag;

            Vector3D point = null;
            TagCompoundBuilder builder = new TagCompoundBuilder();
            for (Map.Entry<String, Tag> entry : compound.entrySet()) {
                if (entry.getKey().equals("Pos")) {
                    Tag value = entry.getValue();

                    if (!(value instanceof TagIntArray)) break;;

                    int[] pos = ((TagIntArray) value).getValue();

                    if (pos.length != 3) break;

                    point = new Vector3D(pos[0], pos[1], pos[2]);
                    continue;
                }

                if (entry.getKey().equals("Id")) continue;

                builder.set(entry.getKey(), entry.getValue());
            }

            if (point == null) {
                Bukkit.getLogger().warning("Missing or incomplete position data for tile entity.");
                continue;
            }

            blockEntities.put(point, builder.build());
        }

        return blockEntities;
    }

    private Map<Vector3D, TagCompound> readEntities(NBTStructure structure) throws SchematicReaderException {
        Map<Vector3D, TagCompound> entities = new HashMap<>();

        List<? extends Tag> entitiesTag = ReaderUtil.require(structure.getList("Entities"), "Entities");
        for (Tag listTag : entitiesTag) {
            if (!(listTag instanceof TagCompound)) {
                Bukkit.getLogger().warning("Expected a CompoundTag for entity, got " + listTag.toString());
                continue;
            }
            TagCompound compound = (TagCompound) listTag;

            Vector3D point = null;
            TagCompoundBuilder builder = new TagCompoundBuilder();
            for (Map.Entry<String, Tag> entry : compound.entrySet()) {
                if (entry.getKey().equals("Pos")) {
                    Tag value = entry.getValue();

                    if (!(value instanceof TagIntArray)) break;;

                    int[] pos = ((TagIntArray) value).getValue();

                    if (pos.length != 3) break;

                    point = new Vector3D(pos[0], pos[1], pos[2]);
                }

                builder.set(entry.getKey(), entry.getValue());
            }

            if (point == null) {
                Bukkit.getLogger().warning("Missing or incomplete position data for entity.");
                continue;
            }

            entities.put(point, builder.build());
        }

        return entities;
    }

    private BiomeVolume readBiomeVolume(NBTStructure structure, int width, int length) throws SchematicReaderException {
        Palette<Biome> palette = readBiomePalette(structure);
        if (palette == null) {
            return null;
        }

        BiomeVolume biomes = new ArrayBiomeVolume(width, 1, length);

        Integer[] data = ReaderUtil.require(structure.getVarintArray("BiomeData"), "BiomeData");
        for (int i = 0; i < data.length; i++) {
            int z = i / width;
            int x = i % width;
            Biome biome = palette.get(data[i]).get();

            biomes.set(x, 0, z, biome);
        }

        return biomes;
    }

    private Palette<Biome> readBiomePalette(NBTStructure structure) throws SchematicReaderException {
        Optional<Map<String, Tag>> optionalTagBiomePalette = structure.getCompound("BiomePalette");;
        if (!optionalTagBiomePalette.isPresent()) {
            return null;
        }

        Map<String, Tag> tagBiomePalette = optionalTagBiomePalette.get();

        Palette<Biome> biomePalette = new Palette<>();
        for (Map.Entry<String, Tag> entry : tagBiomePalette.entrySet()) {
            Tag entryTag = entry.getValue();
            if (!(entryTag instanceof TagInt)) {
                Bukkit.getLogger().warning("Expected an IntTag for biome palette, got " + entryTag.toString());
                continue;
            }

            biomePalette.assign(((TagInt) entryTag).getValue(), Biome.valueOf(entry.getKey()));
        }

        return biomePalette;
    }

}
