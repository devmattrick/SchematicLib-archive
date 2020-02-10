package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.container.BlockHolder;
import cx.matthew.schematiclib.container.Palette;
import cx.matthew.schematiclib.math.Vector3D;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import java.util.Arrays;
import java.util.Iterator;

/**
 * An array-backed volume of blocks.
 */
public class ArrayBlockVolume extends BlockVolume implements ArrayVolume<BlockHolder> {

    private Palette<BlockHolder> palette;
    private int[] blocks;

    public ArrayBlockVolume(int width, int height, int length) {
        this(width, height, length, new Palette<>(), new int[width * height * length]);
    }

    /**
     * Constructs a block volume with given dimensions
     *
     * @param dimensions The dimensions of this volume
     */
    public ArrayBlockVolume(Vector3D dimensions) {
        this(dimensions, new Palette<>(), new int[dimensions.getX() * dimensions.getY() * dimensions.getZ()]);
    }

    /**
     * Constructs a block volume with given dimensions
     *
     * @param dimensions The dimensions of this volume
     * @param palette The palette of block states to use
     * @param blocks An array of block state ids to use
     */
    public ArrayBlockVolume(Vector3D dimensions, Palette<BlockHolder> palette, int[] blocks) {
        super(dimensions);

        this.palette = palette;
        this.blocks = Arrays.copyOf(blocks, blocks.length);
    }

    /**
     * Constructs a block volume with given dimensions
     *
     * @param width The width of this volume
     * @param height The height of this volume
     * @param length This length of this volume
     * @param palette The palette of block states to use
     * @param blocks An array of block state ids to use
     */
    public ArrayBlockVolume(int width, int height, int length, Palette<BlockHolder> palette, int[] blocks) {
        this(new Vector3D(width, length, height), palette, blocks);
    }

    @Override
    public BlockHolder get(Vector3D position) {
        return get(coordToIndex(position));
    }

    @Override
    public void set(Vector3D point, BlockHolder value) {
        set(coordToIndex(point), value);
    }

    public BlockHolder get(int index) {
        return palette.get(blocks[index]).orElse(BlockHolder.DEFAULT);
    }

    @Override
    public void set(int index, BlockHolder value) {
        blocks[index] = palette.getOrAssign(value);
    }

    @Override
    public Iterator<BlockHolder> iterator() {
        return new ArrayVolumeIterator<>(this);
    }

    public int getIndexSize() {
        return this.blocks.length;
    }

    public Palette<BlockHolder> getPalette() {
        return palette;
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
