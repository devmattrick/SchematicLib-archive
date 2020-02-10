package cx.matthew.schematiclib.adapter.v1_15_R1;

import cx.matthew.schematiclib.adapter.ISchematicAdapter;
import cx.matthew.schematiclib.container.BlockHolder;
import cx.matthew.schematiclib.container.volume.BiomeVolume;
import cx.matthew.schematiclib.container.volume.BlockVolume;
import cx.matthew.schematiclib.math.Vector3D;
import cx.matthew.schematiclib.nbt.TagCompound;
import cx.matthew.schematiclib.schematic.Schematic;
import net.minecraft.server.v1_15_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_15_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

import java.util.Objects;
import java.util.Optional;

public class SchematicAdapter implements ISchematicAdapter {

    private static final NBTAdapter nbtAdapter = new NBTAdapter();

    // TODO Improve the efficiency of this, mostly just a PoC for now
    @Override
    public void paste(Location location, Schematic schematic) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(schematic);

        BlockVolume blocks = schematic.getBlocks();
        BiomeVolume biomes = schematic.getBiomes();
        Vector3D dimensions = blocks.getDimensions();
        for (int x = 0; x < dimensions.getX(); x++) {
            for (int y = 0; y < dimensions.getY(); y++) {
                for (int z = 0; z < dimensions.getZ(); z++) {
                    Location offsetLocation = location.clone().add(x, y, z);
                    setBlock(offsetLocation, blocks.get(x, y, z));
                    Objects.requireNonNull(location.getWorld()).setBiome(offsetLocation.getBlockX(), offsetLocation.getBlockY(), offsetLocation.getBlockZ(), biomes.get(x, y, z));
                }
            }
        }
    }

    public void setBlock(Location location, BlockHolder blockHolder) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        Chunk chunk = ((CraftChunk) location.getChunk()).getHandle();
        World world = chunk.getWorld();
        IBlockData blockState = ((CraftBlockData) blockHolder.getState()).getState();

        BlockPosition position = new BlockPosition(x, y, z);

        Optional<TagCompound> tileEntityData = blockHolder.getEntity();
        if (tileEntityData.isPresent()) {
            world.setTypeAndData(position, blockState, 0);

            TileEntity tileEntity = world.getTileEntity(position);
            if (tileEntity != null) {
                NBTTagCompound nmsTag = (NBTTagCompound) nbtAdapter.convert(tileEntityData.get());
                nmsTag.set("x", NBTTagInt.a(x));
                nmsTag.set("y", NBTTagInt.a(y));
                nmsTag.set("z", NBTTagInt.a(z));
                tileEntity.load(nmsTag);
            }
        } else {
            ChunkSection[] sections = chunk.getSections();
            int yShr4 = y >> 4;
            ChunkSection section = sections[yShr4];

            if (section == null) {
                sections[yShr4] = new ChunkSection(yShr4 << 4);
            }

            chunk.setType(position, blockState, false);
        }
    }

}
