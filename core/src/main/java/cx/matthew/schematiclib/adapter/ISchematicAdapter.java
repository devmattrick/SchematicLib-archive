package cx.matthew.schematiclib.adapter;

import cx.matthew.schematiclib.schematic.Schematic;
import org.bukkit.Location;

public interface ISchematicAdapter {

    void paste(Location location, Schematic schematic);

}
