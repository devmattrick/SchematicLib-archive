package cx.matthew.schematiclib.nbt;

import java.util.List;
import java.util.Map;

public enum TagType {

    END        ((byte) 0 , TagEnd.class, null),
    BYTE       ((byte) 1 , TagByte.class, Byte.class),
    SHORT      ((byte) 2 , TagShort.class, Short.class),
    INT        ((byte) 3 , TagInt.class, Integer.class),
    LONG       ((byte) 4 , TagLong.class, Long.class),
    FLOAT      ((byte) 5 , TagFloat.class, Float.class),
    DOUBLE     ((byte) 6 , TagDouble.class, Double.class),
    BYTE_ARRAY ((byte) 7 , TagByteArray.class, byte[].class),
    STRING     ((byte) 8 , TagString.class, String.class),
    LIST       ((byte) 9 , TagList.class, List.class),
    COMPOUND   ((byte) 10, TagCompound.class, Map.class),
    INT_ARRAY  ((byte) 11, TagIntArray.class, int[].class),
    LONG_ARRAY ((byte) 12, TagLongArray.class, long[].class);

    private final byte id;
    private final Class<? extends Tag> type;
    private final Class<?> storageType;

    <T extends Tag> TagType(byte id, Class<T> type, Class<?> storageType) {
        this.id = id;
        this.type = type;
        this.storageType = storageType;
    }

    public static TagType valueOf(byte id) {
        for (TagType value : values()) {
            if (value.id == id) return value;
        }

        return null;
    }

    public static TagType valueOf(int id) {
        return valueOf((byte) id);
    }

    public static <T extends Tag> TagType valueOf(Class<T> type) {
        for (TagType value : values()) {
            if (value.type == type) return value;
        }

        return null;
    }

    public static TagType valueOf(Object stored) {
        for (TagType value : values()) {
            if (value.type == stored.getClass()) return value;
        }

        return null;
    }

    public byte getId() {
        return id;
    }

    public Class<? extends Tag> getType() {
        return type;
    }

}
