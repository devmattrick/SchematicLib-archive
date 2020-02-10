package cx.matthew.schematiclib.nbt;

import java.util.Arrays;
import java.util.Objects;

public class TagIntArray extends TagArray {

    private final int[] value;

    public TagIntArray(int[] value) {
        Objects.requireNonNull(value);
        this.value = Arrays.copyOf(value, value.length);
    }

    public static TagIntArray from(TagIntArray tag) {
        Objects.requireNonNull(tag);
        return new TagIntArray(tag.value);
    }

    @Override
    public int[] getValue() {
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
        sb.append("[I;");
        for (int val : value) {
            if (!first) sb.append(",");

            sb.append(val);
            first = false;
        }
        sb.append("]");

        return sb.toString();
    }

}
