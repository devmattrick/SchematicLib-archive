package cx.matthew.schematiclib.nbt;

import java.util.Arrays;
import java.util.Objects;

public class TagByteArray extends TagArray {

    private final byte[] value;

    public TagByteArray(byte[] value) {
        Objects.requireNonNull(value);
        this.value = Arrays.copyOf(value, value.length);
    }

    public static TagByteArray from(TagByteArray tag) {
        Objects.requireNonNull(tag);
        return new TagByteArray(tag.value);
    }
    
    @Override
    public byte[] getValue() {
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
        sb.append("[B;");
        for (byte val : value) {
            if (!first) sb.append(",");

            sb.append(val).append("b");
            first = false;
        }
        sb.append("]");

        return sb.toString();
    }

}
