package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.container.BlockHolder;
import cx.matthew.schematiclib.math.Vector3D;

/**
 * A volume with given dimensions of blocks.
 */
public abstract class BlockVolume implements Volume<BlockHolder> {

    private final Vector3D dimensions;

    /**
     * Constructs a block volume with given dimensions
     *
     * @param dimensions The dimensions of this volume
     */
    public BlockVolume(Vector3D dimensions) {
        this.dimensions = dimensions;
    }

    /**
     * Constructs a block volume with given dimensions
     *
     * @param width The width of this volume
     * @param height The height of this volume
     * @param length This length of this volume
     */
    public BlockVolume(int width, int height, int length) {
        this(new Vector3D(width, height, length));
    }

    /**
     * Gets the dimensions of this volume
     *
     * @return The dimensions of this volume
     */
    public Vector3D getDimensions() {
        return dimensions;
    }

}
