package cx.matthew.schematiclib.nbt;

public abstract class Tag {

    public abstract Object getValue();

    @Override
    public abstract int hashCode();

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != this.getClass()) return false;
        return this.hashCode() == object.hashCode();
    }

    @Override
    public abstract String toString();

}
