package cx.matthew.schematiclib.nbt.io;

import java.io.IOException;

public class InvalidNBTException extends IOException {

    public InvalidNBTException(String message) {
        super(message);
    }

}
