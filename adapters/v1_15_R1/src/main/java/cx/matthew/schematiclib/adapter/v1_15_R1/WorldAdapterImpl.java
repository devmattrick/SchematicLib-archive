package cx.matthew.schematiclib.adapter.v1_15_R1;

public class WorldAdapterImpl {

    private NBTAdapter nbtAdapter = new NBTAdapter();

    /*
    public void setBlock(org.bukkit.World world, Point point, BlockContainer container, boolean update) {
        setBlock(world, point.getX(), point.getY(), point.getZ(), container, update);
    }

    public void setBlock(org.bukkit.World world, int x, int y, int z, BlockContainer container, boolean update) {
        World nmsWorld = ((CraftWorld) world).getHandle();
        Chunk nmsChunk = nmsWorld.getChunkAt(x >> 4, z >> 4);

        BlockPosition blockPos = new BlockPosition(x, y, z);
        ChunkSection[] sections = nmsChunk.getSections();
        ChunkSection section = sections[y >> 4];

        IBlockData blockData = ((CraftBlockData) Bukkit.createBlockData(container.getBlockState())).getState();
        NBTTagCompound nmsTag = (NBTTagCompound) nbtAdapter.convert(container.getNbtData());

        IBlockData existingBlockData;
        if (section == null) {
            sections[y >> 4] = section = new ChunkSection(y >> 4 << 4);
            existingBlockData = Blocks.AIR.getBlockData();
        } else {
            existingBlockData = section.getType(x & 15, y & 15, z & 15);
        }

        nmsChunk.removeTileEntity(blockPos);

        if (nmsTag != null) {
            nmsWorld.setTypeAndData(blockPos, blockData, 0);

            TileEntity tileEntity = nmsWorld.getTileEntity(blockPos);
            if (tileEntity != null) {
                nmsTag.set("x", NBTTagInt.a(x));
                nmsTag.set("y", NBTTagInt.a(y));
                nmsTag.set("z", NBTTagInt.a(z));
                tileEntity.load(nmsTag);
            }
        } else {
            if (existingBlockData == blockData) return;
            section.getBlocks().setBlock(x & 15, y & 15, z & 15, blockData);
        }

        if (update) {
            nmsWorld.getMinecraftWorld().notify(blockPos, existingBlockData, blockData, 0);
        }
    }
     */

}
