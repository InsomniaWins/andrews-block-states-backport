package wins.insomnia.andrewsbsb.api.blockstate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;

public class BlockStateContainer {

    private final Block owner;
    private final List<BlockStateProperty<?>> properties;
    private final Map<String, BlockStateProperty<?>> propertiesByName;

    private final int[] propertyStrides;
    private final int stateAmount;

    public BlockStateContainer(Block owner, BlockStateProperty<?>... properties) {

        if (owner == null) {
            throw new IllegalArgumentException("Block owner cannot be null!");
        }

        this.owner = owner;
        List<BlockStateProperty<?>> propertyList = new ArrayList<>();
        Map<String, BlockStateProperty<?>> propertyMap = new HashMap<>();

        for (BlockStateProperty<?> property : properties) {

            if (property == null) {
                throw new IllegalArgumentException("Block state property cannot be null");
            }

            if (propertyMap.containsKey(property.getName())) {
                throw new IllegalArgumentException("Duplicate property name: " + property.getName());
            }

            propertyList.add(property);
            propertyMap.put(property.getName(), property);
        }

        this.properties = Collections.unmodifiableList(propertyList);
        this.propertiesByName = Collections.unmodifiableMap(propertyMap);

        propertyStrides = new int[properties.length];

        long propertyStride = 1;
        for (int i = 0; i < this.properties.size(); i++) {
            propertyStrides[i] = (int) propertyStride;

            BlockStateProperty<?> property = this.properties.get(i);
            int valueAmount = property.getValueAmount();

            if (valueAmount <= 0) {
                throw new IllegalArgumentException(
                    "Property must have at least one possible value: " + property.getName());
            }

            propertyStride *= valueAmount;
            if (propertyStride > Integer.MAX_VALUE) {
                throw new IllegalStateException(
                    "Too many property combinations have been provided for block: " + owner);
            }
        }

        stateAmount = (int) propertyStride;

    }

    public BlockState getState(int index) {
        if (index < 0 || index >= stateAmount) {
            throw new IllegalArgumentException("Invalid block state index: " + index);
        }

        BlockState.BlockStateBuilder builder = BlockState.newBlockState(owner);
        for (int i = 0; i < properties.size(); i++) {
            BlockStateProperty property = properties.get(i);
            int internalValue = (index / propertyStrides[i]) % property.getValueAmount();

            builder.withProperty(property, property.getValue(internalValue));
        }

        return builder.build();
    }

    public int getStateIndex(BlockState state) {

        if (state == null || state.owner != owner) {
            throw new IllegalArgumentException("BlockState does not belong to this container!");
        }

        int index = 0;
        for (int i = 0; i < properties.size(); i++) {
            BlockStateProperty<?> property = properties.get(i);
            Object value = state.getPropertyValue(property)
                .orElseThrow(
                    () -> new IllegalArgumentException("BlockState is missing property: " + property.getName()));

            int internalValue = getInternalValue(property, value);
            if (internalValue < 0 || internalValue >= property.getValueAmount()) {
                throw new IllegalArgumentException("Invalid value for property: " + property.getName());
            }

            index += internalValue * propertyStrides[i];
        }

        return index;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> int getInternalValue(BlockStateProperty<?> property, Object value) {
        return ((BlockStateProperty<T>) property).getInternalValue((T) value);
    }

    public Block getOwner() {
        return owner;
    }

    public BlockStateProperty<?> getProperty(String name) {
        return propertiesByName.get(name);
    }

    public BlockStateProperty<?> getProperty(int index) {
        return properties.get(index);
    }

    // return directly; already unmodifiable
    public List<BlockStateProperty<?>> getProperties() {
        return properties;
    }

    public int getStateAmount() {
        return stateAmount;
    }

}
