package cx.matthew.schematiclib.nbt;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class NBTStructure {

    private TagCompound root;

    public NBTStructure(TagCompound root) {
        this.root = root;
    }

    // TODO Cache the results of this
    public Optional<Tag> getTag(String pathString) {
        Objects.requireNonNull(pathString);

        String[] paths = pathString.split("\\.");

        TagCompound compound = root;
        for (int i = 0; i < paths.length; i++) {
            Tag value = compound.getValue().get(paths[i]);

            // If we ever end up with a null value, then the path doesn't exist, fail
            if (value == null) return Optional.empty();

            // We've reached the end, return the correct tag
            if (i == paths.length - 1) {
                return Optional.of(value);
            }

            // We're not at the end, but the value is not a TagCompound, fail
            if (!(value instanceof TagCompound)) break;

            // If we're not at the end and the tag is a TagCompound, then we set that as the next tag to iterate through
            compound = (TagCompound) value;
        }

        return Optional.empty();
    }

    public <T extends Tag> Optional<T> getTag(String key, Class<T> clazz) {
        Optional<Tag> optionalTag = getTag(key);
        if (!optionalTag.isPresent()) return Optional.empty();

        Tag tag = optionalTag.get();
        if (!clazz.isInstance(tag)) return Optional.empty();

        return Optional.of(clazz.cast(tag));
    }

    public Optional<TagByte> getByteTag(String key) {
        return getTag(key, TagByte.class);
    }

    public Optional<Byte> getByte(String key) {
        return getByteTag(key).map(TagByte::getValue);
    }

    public Optional<TagShort> getShortTag(String key) {
        return getTag(key, TagShort.class);
    }

    public Optional<Short> getShort(String key) {
        return getShortTag(key).map(TagShort::getValue);
    }

    public Optional<TagInt> getIntTag(String key) {
        return getTag(key, TagInt.class);
    }

    public Optional<Integer> getInt(String key) {
        return getIntTag(key).map(TagInt::getValue);
    }

    public Optional<TagLong> getLongTag(String key) {
        return getTag(key, TagLong.class);
    }

    public Optional<Long> getLong(String key) {
        return getLongTag(key).map(TagLong::getValue);
    }

    public Optional<TagFloat> getFloatTag(String key) {
        return getTag(key, TagFloat.class);
    }

    public Optional<Float> getFloat(String key) {
        return getFloatTag(key).map(TagFloat::getValue);
    }

    public Optional<TagDouble> getDoubleTag(String key) {
        return getTag(key, TagDouble.class);
    }

    public Optional<Double> getDouble(String key) {
        return getDoubleTag(key).map(TagDouble::getValue);
    }

    public Optional<TagByteArray> getByteArrayTag(String key) {
        return getTag(key, TagByteArray.class);
    }

    public Optional<byte[]> getByteArray(String key) {
        return getByteArrayTag(key).map(TagByteArray::getValue);
    }

    public Optional<TagString> getStringTag(String key) {
        return getTag(key, TagString.class);
    }

    public Optional<String> getString(String key) {
        return getStringTag(key).map(TagString::getValue);
    }

    public Optional<TagList> getListTag(String key) {
        return getTag(key, TagList.class);
    }

    public Optional<List<? extends Tag>> getList(String key) {
        return getListTag(key).map(TagList::getValue);
    }

    public Optional<TagCompound> getCompoundTag(String key) {
        return getTag(key, TagCompound.class);
    }

    public Optional<Map<String, Tag>> getCompound(String key) {
        return getCompoundTag(key).map(TagCompound::getValue);
    }

    public Optional<TagIntArray> getIntArrayTag(String key) {
        return getTag(key, TagIntArray.class);
    }

    public Optional<int[]> getIntArray(String key) {
        return getIntArrayTag(key).map(TagIntArray::getValue);
    }

    public Optional<TagLongArray> getLongArrayTag(String key) {
        return getTag(key, TagLongArray.class);
    }

    public Optional<long[]> getLongArray(String key) {
        return getLongArrayTag(key).map(TagLongArray::getValue);
    }

}
