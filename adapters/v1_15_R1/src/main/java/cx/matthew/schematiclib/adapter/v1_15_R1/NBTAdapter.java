package cx.matthew.schematiclib.adapter.v1_15_R1;

import cx.matthew.schematiclib.adapter.INBTAdapter;
import cx.matthew.schematiclib.nbt.Tag;
import cx.matthew.schematiclib.nbt.*;
import net.minecraft.server.v1_15_R1.*;

import java.util.*;

public class NBTAdapter implements INBTAdapter<NBTBase> {
    /**
     * Converts NBT data from the intermediate format used by SchematicLib into Minecraft's own NBT format.
     * @param from The tag to convert
     * @return The converted Minecraft NBT tag
     */
    public NBTBase convert(Tag from) {
        if (from == null) return null;

        if (from instanceof TagEnd) return NBTTagEnd.b;

        if (from instanceof TagByte) return NBTTagByte.a(((TagByte) from).getValue());

        if (from instanceof TagShort) return NBTTagShort.a(((TagShort) from).getValue());

        if (from instanceof TagInt) return NBTTagInt.a(((TagInt) from).getValue());

        if (from instanceof TagLong) return NBTTagLong.a(((TagLong) from).getValue());

        if (from instanceof TagFloat) return NBTTagFloat.a(((TagFloat) from).getValue());

        if (from instanceof TagDouble) return NBTTagDouble.a(((TagDouble) from).getValue());

        if (from instanceof TagByteArray) return new NBTTagByteArray(((TagByteArray) from).getValue());

        if (from instanceof TagString) return NBTTagString.a(((TagString) from).getValue());

        if (from instanceof TagList) {
            NBTTagList to = new NBTTagList();
            for (Tag tag : ((TagList<?>) from).getValue()) {
                to.add(convert(tag));
            }
            return to;
        }

        if (from instanceof TagCompound) {
            NBTTagCompound to = new NBTTagCompound();
            for (Map.Entry<String, Tag> entry : ((TagCompound) from).entrySet()) {
                to.set(entry.getKey(), convert(entry.getValue()));
            }
            return to;
        }

        if (from instanceof TagIntArray) return new NBTTagIntArray(((TagIntArray) from).getValue());

        if (from instanceof TagLongArray) return new NBTTagLongArray(((TagLongArray) from).getValue());

        throw new IllegalArgumentException("Unable to convert from " + from.getClass().getCanonicalName());
    }

    /**
     * Converts NBT data from Minecraft's own NBT format to cx.matthew.schematiclib.SchematicLib's intermediate NBT format.
     * @param from The tag to convert
     * @return The converted cx.matthew.schematiclib.SchematicLib NBT tag
     */
    public Tag convert(NBTBase from) {
        if (from == null) return null;

        if (from instanceof NBTTagEnd) return TagEnd.INSTANCE;

        if (from instanceof NBTTagByte) return new TagByte(((NBTTagByte) from).asByte());

        if (from instanceof NBTTagShort) return new TagShort(((NBTTagShort) from).asShort());

        if (from instanceof NBTTagInt) return new TagInt(((NBTTagInt) from).asInt());

        if (from instanceof NBTTagLong) return new TagLong(((NBTTagLong) from).asLong());

        if (from instanceof NBTTagFloat) return new TagFloat(((NBTTagFloat) from).asFloat());

        if (from instanceof NBTTagDouble) return new TagDouble(((NBTTagDouble) from).asDouble());

        if (from instanceof NBTTagByteArray) return new TagByteArray(((NBTTagByteArray) from).getBytes());

        if (from instanceof NBTTagString) return new TagString(from.asString());

        if (from instanceof NBTTagList) {
            NBTTagList fromList = (NBTTagList) from;
            TagListBuilder<Tag> builder = new TagListBuilder<>(Tag.class);

            for (NBTBase fromBase : fromList) {
                builder.add(convert(fromBase));
            }

            return builder.build();
        }

        if (from instanceof NBTTagCompound) {
            NBTTagCompound fromCompound = ((NBTTagCompound) from);
            Map<String, Tag> values = new HashMap<>();
            Set<String> fromKeys = fromCompound.getKeys();

            for (String fromKey : fromKeys) {
                NBTBase base = fromCompound.get(fromKey);
                values.put(fromKey, convert(base));
            }

            return new TagCompound(values);
        }

        if (from instanceof NBTTagIntArray) return new TagIntArray(((NBTTagIntArray) from).getInts());

        if (from instanceof NBTTagLongArray) return new TagLongArray(((NBTTagLongArray) from).getLongs());

        throw new IllegalArgumentException("Unable to convert from " + from.getClass().getCanonicalName());
    }
}
