package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class TagByte extends Tag {

    private final byte value;

    public TagByte(byte value) {
        this.value = value;
    }

    public static TagByte from(TagByte tag) {
        Objects.requireNonNull(tag);
        return new TagByte(tag.value);
    }

    @Override
    public Byte getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Byte.hashCode(value);
    }

    @Override
    public String toString() {
        return value + "b";
    }

}
