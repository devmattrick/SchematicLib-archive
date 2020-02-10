package cx.matthew.schematiclib.adapter;

import cx.matthew.schematiclib.nbt.Tag;

public interface INBTAdapter<T> {

    T convert(Tag from);

    Tag convert(T from);

}
