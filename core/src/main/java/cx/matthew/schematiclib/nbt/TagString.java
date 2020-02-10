package cx.matthew.schematiclib.nbt;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class TagString extends Tag {

    private static final Charset NBT_CHARSET = StandardCharsets.UTF_8;

    private final String value;

    public TagString(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public TagString(byte[] value) {
        Objects.requireNonNull(value);
        this.value = new String(value, NBT_CHARSET);
    }

    public static TagString from(TagString tag) {
        Objects.requireNonNull(tag);
        return new TagString(tag.value);
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }

}
