package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class TagDouble extends Tag {

    private final double value;

    public TagDouble(double value) {
        this.value = value;
    }

    public static TagDouble from(TagDouble tag) {
        Objects.requireNonNull(tag);
        return new TagDouble(tag.value);
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return value + "d";
    }

}
