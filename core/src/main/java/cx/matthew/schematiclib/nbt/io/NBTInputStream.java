package cx.matthew.schematiclib.nbt.io;

import cx.matthew.schematiclib.nbt.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class NBTInputStream extends DataInputStream {

    private static final Charset NBT_CHARSET = StandardCharsets.UTF_8;

    /**
     * Creates a NBTInputStream that uses the specified
     * underlying InputStream.
     *
     * @param in the specified input stream
     */
    public NBTInputStream(InputStream in) {
        super(conditionallyUngzip(in));
    }

    public NamedTag readTags() throws IOException {
        return readNamedTag();
    }

    private NamedTag readNamedTag() throws IOException {
        byte id = readByte();
        if (id == -1) return null;

        TagType type = TagType.valueOf(id);
        if (type == null) throw new InvalidNBTException("Invalid tag id: " + id);

        String name = type != TagType.END ? readString() : "";
        return new NamedTag(name, readTag(type));
    }

    private Tag readTag(TagType type) throws IOException {
        Objects.requireNonNull(type);

        switch (type) {
            case END:        return TagEnd.INSTANCE;
            case BYTE:       return new TagByte(readByte());
            case SHORT:      return new TagShort(readShort());
            case INT:        return new TagInt(readInt());
            case LONG:       return new TagLong(readLong());
            case FLOAT:      return new TagFloat(readFloat());
            case DOUBLE:     return new TagDouble(readDouble());
            case BYTE_ARRAY: return readTagByteArray();
            case STRING:     return readTagString();
            case LIST:       return readTagList();
            case COMPOUND:   return readTagCompound();
            case INT_ARRAY:  return readTagIntArray();
            case LONG_ARRAY: return readTagLongArray();
        }

        throw new InvalidNBTException("Invalid tag type");
    }

    private TagByteArray readTagByteArray() throws IOException {
        int length = readInt();
        byte[] data = new byte[length];
        readFully(data);
        return new TagByteArray(data);
    }

    private TagString readTagString() throws IOException {
        return new TagString(readString());
    }

    private TagList<?> readTagList() throws IOException {
        byte tagId = readByte();
        TagType type = TagType.valueOf(tagId);
        int length = readInt();

        if (type == null) throw new InvalidNBTException("Invalid tag id in list: " + tagId);
        if (type == TagType.END && length > 0) throw new InvalidNBTException("Nonempty list of END tags");

        List<Tag> items = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Tag item = readTag(type);
            items.add(item);
        }

        return new TagList(items, type.getType());
    }

    private TagCompound readTagCompound() throws IOException {
        Map<String, Tag> tags = new LinkedHashMap<>();

        while(true) {
            NamedTag namedTag = readNamedTag();
            if (namedTag == null) throw new InvalidNBTException("Unexpected end of NBT");

            Tag tag = namedTag.getTag();

            if (tag instanceof TagEnd) break;;

            tags.put(namedTag.getName(), tag);
        }

        return new TagCompound(tags);
    }

    private TagIntArray readTagIntArray() throws IOException {
        int length = readInt();
        int[] data = new int[length];

        for (int i = 0; i < length; i++) data[i] = readInt();

        return new TagIntArray(data);
    }

    private TagLongArray readTagLongArray() throws IOException {
        int length = readInt();
        long[] data = new long[length];

        for (int i = 0; i < length; i++) data[i] = readLong();

        return new TagLongArray(data);
    }

    private String readString() throws IOException {
        int length = readUnsignedShort();
        byte[] data = new byte[length];
        readFully(data);
        return new String(data, NBT_CHARSET);
    }

    private static InputStream conditionallyUngzip(InputStream in) {
        PushbackInputStream pb = new PushbackInputStream(in, 2 );
        try {
            int signature = (pb.read() & 0xFF) + (pb.read() << 8);
            pb.unread(signature >> 8);
            pb.unread(signature & 0xFF);

            if (signature == GZIPInputStream.GZIP_MAGIC) return new GZIPInputStream(pb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pb;
    }

}
