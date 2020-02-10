package cx.matthew.schematiclib.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TagCompoundBuilder {

    private Map<String, Tag> map;

    public TagCompoundBuilder() {
        this(new HashMap<>());
    }

    public TagCompoundBuilder(Map<String, Tag> map) {
        Objects.requireNonNull(map);
        this.map = map;
    }

    public void set(String key, Tag value) {
        map.put(key, value);
    }

    public TagCompound build() {
        return new TagCompound(map);
    }

}
