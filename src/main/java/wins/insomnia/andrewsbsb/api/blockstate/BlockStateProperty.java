package wins.insomnia.andrewsbsb.api.blockstate;

public abstract class BlockStateProperty<T extends Comparable<T>> {

    private final String name;
    private final Class<T> valueClass;

    public BlockStateProperty(String name, Class<T> typeClass) {
        this.name = name;
        this.valueClass = typeClass;
    }

    public abstract boolean isValid(T value);

    public abstract int getInternalValue(T value);

    public abstract int getValueAmount();

    public abstract T getValue(int internalValue);

    public abstract T getDefaultValue();

    public abstract String serializeValue(T value);

    public abstract T deserializeValue(String serializedValue);

    public String getName() {
        return name;
    }

    public Class<T> getValueClass() {
        return valueClass;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof BlockStateProperty)) {
            return super.equals(obj);
        }

        BlockStateProperty<?> other = (BlockStateProperty<?>) obj;
        if (!other.getValueClass()
            .equals(getValueClass())) {
            return false;
        }

        if (!other.getName()
            .equals(getName())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 31 * valueClass.hashCode() + getName().hashCode();
    }

}
