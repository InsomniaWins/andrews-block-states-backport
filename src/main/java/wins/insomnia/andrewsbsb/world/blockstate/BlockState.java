package wins.insomnia.andrewsbsb.world.blockstate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;

import wins.insomnia.andrewsbsb.block.IBlockStateHolder;
import wins.insomnia.andrewsbsb.mixinduck.IMixinBlockStateHolder;

public class BlockState {

    public static final String KEY_BLOCK_OWNER = "Block";
    public static final String KEY_PROPERTIES = "Properties";

    public final Block owner;
    private Map<BlockStateProperty<?>, Comparable<?>> properties = new HashMap<>();
    private boolean finalized = false;

    private BlockState(Block owner) {
        this.owner = owner;
    }

    public static BlockStateBuilder newBlockState(Block owner) {
        return new BlockStateBuilder(owner);
    }

    public static BlockStateBuilder newBlockState(BlockState copyFrom) {
        return new BlockStateBuilder(copyFrom);
    }

    @SuppressWarnings("unchecked")
    public <T extends Comparable<T>> Optional<T> getPropertyValue(BlockStateProperty<T> property) {
        return Optional.ofNullable((T) properties.get(property));
    }

    private <T extends Comparable<T>> void addProperty(BlockStateProperty<T> property, T value) {
        if (isFinalized()) {
            throw new FinalizedBlockStateException(this);
        }
        properties.put(property, value);
    }

    public boolean isFinalized() {
        return finalized;
    }

    private void finalizeBlockState() {
        properties = Collections.unmodifiableMap(new HashMap<>(properties));
        finalized = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        if (!(obj instanceof BlockState)) {
            return super.equals(obj);
        }

        BlockState other = (BlockState) obj;

        if (other.owner != owner) {
            return false;
        }

        return properties.equals(other.properties);
    }

    @Override
    public int hashCode() {
        return 31 * System.identityHashCode(owner) + properties.hashCode();
    }

    public static class FinalizedBlockStateException extends RuntimeException {

        public final BlockState blockState;

        public FinalizedBlockStateException(BlockState blockState) {
            this.blockState = blockState;
        }

    }

    public static class BlockStateBuilder {

        private final BlockState blockState;

        private BlockStateBuilder(BlockState copyFrom) {
            blockState = new BlockState(copyFrom.owner);
            blockState.properties.putAll(copyFrom.properties);
        }

        private BlockStateBuilder(Block owner) {
            blockState = new BlockState(owner);
        }

        public <T extends Comparable<T>> BlockStateBuilder withProperty(BlockStateProperty<T> property, T value) {

            if (property == null) {
                throw new IllegalArgumentException("Tried adding property of null to block state: " + this);
            }

            if (!property.isValid(value)) {
                throw new IllegalArgumentException("Invalid value for block state property: " + property.getName());
            }

            blockState.addProperty(property, value);
            return this;
        }

        public BlockState build() {
            blockState.finalizeBlockState();
            return blockState;
        }

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{");

        Iterator<Map.Entry<BlockStateProperty<?>, Comparable<?>>> iterator = properties.entrySet()
            .iterator();
        while (iterator.hasNext()) {
            Map.Entry<BlockStateProperty<?>, Comparable<?>> propertyEntry = iterator.next();
            stringBuilder.append(
                propertyEntry.getKey()
                    .getName() + ": "
                    + propertyEntry.getValue());

            if (iterator.hasNext()) {
                stringBuilder.append(", ");
            }
        }

        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public <T extends Comparable<T>> void writeToNBT(NBTTagCompound compound) {

        String blockName = Block.blockRegistry.getNameForObject(owner);

        if (blockName == null) {
            throw new IllegalStateException("Block has no registry name: " + owner);
        }

        compound.setString(KEY_BLOCK_OWNER, blockName);

        NBTTagCompound propertiesCompound = new NBTTagCompound();

        for (Map.Entry<BlockStateProperty<?>, Comparable<?>> propertyEntry : properties.entrySet()) {
            BlockStateProperty<T> property = (BlockStateProperty<T>) propertyEntry.getKey();
            T value = (T) propertyEntry.getValue();

            propertiesCompound.setString(property.getName(), property.serializeValue(value));

        }

        compound.setTag(KEY_PROPERTIES, propertiesCompound);

    }

    public static <T extends Comparable<T>> BlockState readFromNBT(NBTTagCompound compound) {

        String blockName = compound.getString(KEY_BLOCK_OWNER);
        Object blockObject = Block.blockRegistry.getObject(blockName);

        if (!(blockObject instanceof Block)) {
            throw new IllegalStateException("Unknown block with id of: " + blockName);
        }

        Block owner = (Block) blockObject;
        BlockStateContainer stateContainer = null;

        if (owner instanceof IMixinBlockStateHolder) {
            stateContainer = ((IMixinBlockStateHolder) owner).andrewsBSB$getBlockStateContainer();
        } else if (owner instanceof IBlockStateHolder) {
            stateContainer = ((IBlockStateHolder) owner).getBlockStateContainer();
        }

        if (stateContainer == null) {
            throw new IllegalStateException("Block \"" + blockName + "\" has no properties for block states!");
        }

        BlockStateBuilder builder = BlockState.newBlockState(owner);

        NBTTagCompound propertiesCompound = compound.getCompoundTag(KEY_PROPERTIES);

        for (BlockStateProperty<?> property : stateContainer.getProperties()) {
            if (!propertiesCompound.hasKey(property.getName())) {
                builder.withProperty((BlockStateProperty<T>) property, (T) property.getDefaultValue());
                continue;
            }

            String serializedValue = propertiesCompound.getString(property.getName());
            Comparable<?> deserializedValue = property.deserializeValue(serializedValue);

            builder.withProperty((BlockStateProperty<T>) property, (T) deserializedValue);
        }

        return builder.build();
    }

}
