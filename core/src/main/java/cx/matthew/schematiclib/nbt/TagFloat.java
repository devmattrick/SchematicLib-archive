package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class TagFloat extends Tag {

    private final float value;

    public TagFloat(float value) {
        this.value = value;
    }

    public static TagFloat from(TagFloat tag) {
        Objects.requireNonNull(tag);
        return new TagFloat(tag.value);
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Float.hashCode(value);
    }

    @Override
    public String toString() {
        return value + "f";
    }

}
