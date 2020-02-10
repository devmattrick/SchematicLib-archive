package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class TagLong extends Tag {

    private final long value;

    public TagLong(long value) {
        this.value = value;
    }

    public static TagLong from(TagLong tag) {
        Objects.requireNonNull(tag);
        return new TagLong(tag.value);
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(value);
    }

    @Override
    public String toString() {
        return value + "l";
    }

}
