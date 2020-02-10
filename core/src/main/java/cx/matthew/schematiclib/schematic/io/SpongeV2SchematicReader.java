package cx.matthew.schematiclib.schematic.io;

import cx.matthew.schematiclib.container.BlockHolder;
import cx.matthew.schematiclib.container.Palette;
import cx.matthew.schematiclib.container.volume.ArrayBlockVolume;
import cx.matthew.schematiclib.container.volume.BiomeVolume;
import cx.matthew.schematiclib.container.volume.BlockVolume;
import cx.matthew.schematiclib.container.volume.LegacyArrayBiomeVolume;
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

        int width = ReaderUtil.requireOptional(structure.getShort("Width"), "Width") & 0xFFFF;
        int height = ReaderUtil.requireOptional(structure.getShort("Height"), "Height") & 0xFFFF;
        int length = ReaderUtil.requireOptional(structure.getShort("Length"), "Length") & 0xFFFF;

        // Read block and block entity data
        Palette<BlockData> palette = readBlockDataPalette(structure);
        Map<Vector3D, TagCompound> blockEntities = readBlockEntities(structure);

        BlockVolume blocks = new ArrayBlockVolume(width, height, length);

        byte[] blockData = ReaderUtil.requireOptional(structure.getByteArray("BlockData"), "BlockData");
        int index = 0;
        int i = 0;
        int value = 0;
        int varint_length = 0;
        while (i < blockData.length) {
            value = 0;
            varint_length = 0;

            while (true) {
                value |= (blockData[i] & 127) << (varint_length++ * 7);
                if (varint_length > 5) {
                    throw new RuntimeException("VarInt too big (probably corrupted data)");
                }
                if ((blockData[i] & 128) != 128) {
                    i++;
                    break;
                }
                i++;
            }

            int y = index / (width * length);
            int z = (index % (width * length)) / width;
            int x = (index % (width * length)) % width;
            BlockData state = palette.get(value).get();

            System.out.println(blockEntities.get(new Vector3D(x, y, z)));

            blocks.set(x, y, z, new BlockHolder(state, blockEntities.get(new Vector3D(x, y, z))));

            index++;
        }

        System.out.println();

        Map<Vector3D, TagCompound> entities = readEntities(structure);

        // TODO read biome data

        return new Schematic(blocks, entities, null);
    }

    private Palette<BlockData> readBlockDataPalette(NBTStructure structure) throws SchematicReaderException {
        Map<String, Tag> tagBlockPalette = ReaderUtil.requireOptional(structure.getCompound("Palette"), "Palette");

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

        List<? extends Tag> blockEntitiesTag = ReaderUtil.requireOptional(structure.getList("TileEntities"), "BlockEntities");
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

        blockEntities.forEach((key, value) -> System.out.println(key + " : " + value));

        return blockEntities;
    }

    private Map<Vector3D, TagCompound> readEntities(NBTStructure structure) throws SchematicReaderException {
        Map<Vector3D, TagCompound> entities = new HashMap<>();

        List<? extends Tag> entitiesTag = ReaderUtil.requireOptional(structure.getList("Entities"), "Entities");
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

    private Palette<Biome> readBiomePalette(NBTStructure structure) throws SchematicReaderException {
        Map<String, Tag> tagBiomePalette = ReaderUtil.requireOptional(structure.getCompound("BiomePalette"), "BiomePalette");

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
