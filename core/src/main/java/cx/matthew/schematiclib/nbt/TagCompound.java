package cx.matthew.schematiclib.nbt;

import java.util.*;

public class TagCompound extends Tag {

    private Map<String, Tag> value;

    public TagCompound() {
        this.value = new HashMap<>();
    }

    public TagCompound(Map<String, Tag> value) {
        Objects.requireNonNull(value);
        this.value = new HashMap<>(value);
    }

    public static TagCompound from(TagCompound tag) {
        Objects.requireNonNull(tag);
        return new TagCompound(tag.value);
    }

    @Override
    public Map<String, Tag> getValue() {
        return Collections.unmodifiableMap(value);
    }

    public Set<Map.Entry<String, Tag>> entrySet() {
        return value.entrySet();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        sb.append("{");
        for (Map.Entry<String, Tag> entry : value.entrySet()) {
            if (!first) sb.append(",");

            sb.append(entry.getKey()).append(":").append(entry.getValue().toString());
            first = false;
        }
        sb.append("}");

        return sb.toString();
    }

}
