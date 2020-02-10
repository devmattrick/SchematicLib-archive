package cx.matthew.schematiclib.nbt;

import java.util.Arrays;
import java.util.Objects;

public class TagLongArray extends TagArray {

    private final long[] value;

    public TagLongArray(long[] value) {
        Objects.requireNonNull(value);
        this.value = Arrays.copyOf(value, value.length);
    }

    public static TagLongArray from(TagLongArray tag) {
        Objects.requireNonNull(tag);
        return new TagLongArray(tag.value);
    }

    @Override
    public long[] getValue() {
        return Arrays.copyOf(value, value.length);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        sb.append("[L;");
        for (long val : value) {
            if (!first) sb.append(",");

            sb.append(val).append("l");
            first = false;
        }
        sb.append("]");

        return sb.toString();
    }

}
