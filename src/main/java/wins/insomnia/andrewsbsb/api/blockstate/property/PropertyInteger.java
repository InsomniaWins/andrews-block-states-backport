package wins.insomnia.andrewsbsb.api.blockstate.property;

import wins.insomnia.andrewsbsb.api.blockstate.BlockStateProperty;

public class PropertyInteger extends BlockStateProperty<Integer> {

    private final int min;
    private final int max;
    private final int defaultValue;

    public PropertyInteger(String name, int minValue, int maxValue) {
        this(name, minValue, maxValue, minValue);
    }

    public PropertyInteger(String name, int minValue, int maxValue, int defaultValue) {
        super(name, Integer.class);

        if (minValue > maxValue) {
            throw new IllegalArgumentException("Maximum value cannot be less than minimum value of integer property!");
        }

        this.min = minValue;
        this.max = maxValue;

        if (!isInBounds(defaultValue)) {
            throw new IllegalArgumentException("Default value of int must be in bounds of minValue and maxValue!");
        }

        this.defaultValue = defaultValue;
    }

    @Override
    public boolean isValid(Integer value) {
        return isInBounds(value);
    }

    @Override
    public int getInternalValue(Integer value) {
        if (isInBounds(value)) {
            return value - min;
        }
        return -1;
    }

    @Override
    public int getValueAmount() {
        return max - min + 1;
    }

    @Override
    public Integer getValue(int internalValue) {

        if (internalValue < 0 || internalValue >= getValueAmount()) {
            throw new IllegalArgumentException("Invalid integer value index: " + internalValue);
        }

        return min + internalValue;
    }

    @Override
    public Integer getDefaultValue() {
        return defaultValue;
    }

    @Override
    public String serializeValue(Integer value) {
        return Integer.toString(value);
    }

    @Override
    public Integer deserializeValue(String serializedValue) {
        return Integer.parseInt(serializedValue);
    }

    public boolean isInBounds(int value) {
        if (value < min) return false;
        if (value > max) return false;
        return true;
    }

}
