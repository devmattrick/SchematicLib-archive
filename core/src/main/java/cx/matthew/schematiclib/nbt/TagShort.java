package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class TagShort extends Tag {

    private final short value;

    public TagShort(short value) {
        this.value = value;
    }

    public static TagShort from(TagShort tag) {
        Objects.requireNonNull(tag);
        return new TagShort(tag.value);
    }

    @Override
    public Short getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Short.hashCode(value);
    }

    @Override
    public String toString() {
        return value + "s";
    }

}
