package wins.insomnia.andrewsbsb.api.blockstate.property;

import wins.insomnia.andrewsbsb.api.blockstate.BlockStateProperty;

public class PropertyBoolean extends BlockStateProperty<Boolean> {

    public static final int FALSE_INDEX = 0;
    public static final int TRUE_INDEX = 1;

    private final boolean defaultValue;

    public PropertyBoolean(String name) {
        this(name, false);
    }

    public PropertyBoolean(String name, boolean defaultValue) {
        super(name, Boolean.class);
        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isValid(Boolean value) {
        return value != null;
    }

    @Override
    public int getInternalValue(Boolean value) {
        return value ? TRUE_INDEX : FALSE_INDEX;
    }

    @Override
    public int getValueAmount() {
        return 2;
    }

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String serializeValue(Boolean value) {
        return value.toString();
    }

    @Override
    public Boolean deserializeValue(String serializedValue) {
        return !serializedValue.equals("false");
    }

    @Override
    public Boolean getValue(int internalValue) {
        return internalValue != 0;
    }

}
