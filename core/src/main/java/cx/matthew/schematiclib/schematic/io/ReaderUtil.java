package cx.matthew.schematiclib.schematic.io;

import java.util.Optional;

public class ReaderUtil {

    public static <T> T requireOptional(Optional<T> optional, String name) throws SchematicReaderException {
        if (!optional.isPresent()) throw new SchematicReaderException("Missing required data in schematic: " + name);

        return optional.get();
    }

}
