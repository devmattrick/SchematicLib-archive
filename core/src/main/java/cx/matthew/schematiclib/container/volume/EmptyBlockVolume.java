package cx.matthew.schematiclib.container.volume;

import cx.matthew.schematiclib.container.BlockHolder;
import cx.matthew.schematiclib.math.Vector3D;
import org.bukkit.block.data.BlockData;

public class EmptyBlockVolume extends BlockVolume implements EmptyVolume<BlockHolder> {

    public static final EmptyBlockVolume EMPTY_VOLUME = new EmptyBlockVolume();

    private EmptyBlockVolume() {
        super(Vector3D.ZERO);
    }

}
