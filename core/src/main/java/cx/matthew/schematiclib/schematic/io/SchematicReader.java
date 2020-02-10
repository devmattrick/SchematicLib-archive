package cx.matthew.schematiclib.schematic.io;

import cx.matthew.schematiclib.schematic.Schematic;

import java.io.IOException;

public interface SchematicReader {

    Schematic read() throws IOException;

}
