package cx.matthew.schematiclib.nbt;

public class TagEnd extends Tag {

    public static final TagEnd INSTANCE = new TagEnd();

    private TagEnd() {}

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }

}
