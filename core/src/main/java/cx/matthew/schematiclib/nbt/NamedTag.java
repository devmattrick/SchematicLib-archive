package cx.matthew.schematiclib.nbt;

import java.util.Objects;

public class NamedTag {

    private String name;
    private Tag tag;

    public NamedTag(String name, Tag tag) {
        this.name = Objects.requireNonNull(name);
        this.tag = Objects.requireNonNull(tag);
    }

    public static NamedTag from(NamedTag tag) {
        Objects.requireNonNull(tag);
        return new NamedTag(tag.name, tag.tag);
    }

    public String getName() {
        return name;
    }

    public Tag getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "{" + name + ":" + tag.toString() + "}";
    }

}
