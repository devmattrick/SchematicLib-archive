package cx.matthew.schematiclib.nbt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class TagList<T extends Tag> extends Tag {

    private final List<T> value;
    private final Class<T> type;

    public TagList(List<T> value, Class<T> type) {
        Objects.requireNonNull(value);
        this.value = Collections.unmodifiableList(value);
        this.type = Objects.requireNonNull(type);
    }

    @SuppressWarnings("unchecked")
    public static <C extends Tag> TagList<C> from(TagList<C> tag) {
        Objects.requireNonNull(tag);
        return new TagList<C>(tag.value, (Class<C>) tag.value.get(0).getClass());
    }

    @Override
    public List<T> getValue() {
        return value;
    }

    public Class<T> getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");
        boolean first = true;
        for (Tag tag : value) {
            if (!first) sb.append(",");

            sb.append(tag.toString());
            first = false;
        }
        sb.append("]");

        return sb.toString();
    }
}
