package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class TagInt extends Tag {

    private final int value;

    public TagInt(int value) {
        this.value = value;
    }

    public static TagInt from(TagInt tag) {
        Objects.requireNonNull(tag);
        return new TagInt(tag.value);
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
