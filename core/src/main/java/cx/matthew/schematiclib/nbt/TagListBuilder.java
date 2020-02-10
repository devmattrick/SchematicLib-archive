package cx.matthew.schematiclib.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TagListBuilder<T extends Tag> {

    private List<T> tags;
    private Class<T> type;

    public TagListBuilder(Class<T> clazz) {
        this(new ArrayList<>(), clazz);
    }

    public TagListBuilder(List<T> tags, Class<T> clazz) {
        this.tags = tags;
        this.type = Objects.requireNonNull(clazz);
    }

    public static <T extends Tag> TagListBuilder<T> from(TagList<T> tagList) {
        return new TagListBuilder<>(new ArrayList<>(tagList.getValue()), tagList.getType());
    }

    public void add(T tag) {
        this.tags.add(tag);
    }

    public Class<T> getType() {
        return type;
    }

    public TagList<T> build() {
        return new TagList<T>(tags, type);
    }

}
