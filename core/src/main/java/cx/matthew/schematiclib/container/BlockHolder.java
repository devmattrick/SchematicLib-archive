package cx.matthew.schematiclib.container;

import cx.matthew.schematiclib.nbt.TagCompound;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Objects;
import java.util.Optional;

public class BlockHolder {

    public static final BlockHolder DEFAULT = new BlockHolder(Material.AIR.createBlockData());

    private BlockData state;
    private TagCompound entity;

    public BlockHolder(BlockData state) {
        this(state, null);
    }

    public BlockHolder(BlockData state, TagCompound entity) {
        this.state = state;
        this.entity = entity;
    }

    public BlockData getState() {
        return state;
    }

    public Optional<TagCompound> getEntity() {
        return Optional.ofNullable(entity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockHolder that = (BlockHolder) o;
        return state.matches(that.state) &&
                Objects.equals(entity, that.entity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, entity);
    }

}
