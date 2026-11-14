package wins.insomnia.andrewsbsb.world.blockstate;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.api.blockstate.BlockStateContainer;
import wins.insomnia.andrewsbsb.util.BlockStateUtil;

public class BlockStatePalette {

    public static final String KEY_EMPTY = "AndrewsBSB_Empty";
    public static final String KEY_STATES = "AndrewsBSB_States";
    public static final String KEY_INDICES = "AndrewsBSB_Indices";

    private final List<BlockState> palette = new ArrayList<>();
    private final Map<BlockState, Short> paletteLookup = new HashMap<>();

    private final short[] indices = new short[4096];

    public BlockStatePalette() {
        palette.add(null);
    }

    public BlockState getFromBlockPos(int blockX, int blockY, int blockZ) {
        return get(xFromBlockX(blockX), yFromBlockY(blockY), zFromBlockZ(blockZ));
    }

    public BlockState get(int x, int y, int z) {

        if (BlockStateUtil.debugInfoEnabled && palette.get(indices[getIndex(x, y, z)]) == null) {
            AndrewsBSB.LOG.warn("BlockState at {},{},{} is null", x, y, z);
        }

        return palette.get(indices[getIndex(x, y, z)]);
    }

    public void setFromBlockPos(int blockX, int blockY, int blockZ, BlockState state) {

        if (state != null && BlockStateUtil.debugInfoEnabled) {
            AndrewsBSB.LOG.info("Setting {}, {}, {} to {}", blockX, blockY, blockZ, state);
        }

        set(xFromBlockX(blockX), yFromBlockY(blockY), zFromBlockZ(blockZ), state);
    }

    public void set(int x, int y, int z, BlockState state) {
        int indicesIndex = getIndex(x, y, z);
        if (state == null) {
            indices[indicesIndex] = 0;
            return;
        }

        Short paletteIndex = paletteLookup.get(state);
        if (paletteIndex == null) {
            paletteIndex = (short) palette.size();

            palette.add(state);
            paletteLookup.put(state, paletteIndex);
        }

        indices[indicesIndex] = paletteIndex;
    }

    private static int getIndex(int x, int y, int z) {

        if (x < 0 || x >= 16 || y < 0 || y >= 16 || z < 0 || z >= 16) {
            throw new IllegalArgumentException("Cannot get palette index of x,y,z outside of palette bounds!");
        }

        return (y << 8) | (z << 4) | x;
    }

    public List<BlockState> getPalette() {
        return Collections.unmodifiableList(palette);
    }

    public short[] getIndices() {
        return indices.clone();
    }

    public static int xFromBlockX(int blockX) {
        return blockX & 15;
    }

    public static int yFromBlockY(int blockY) {
        return blockY & 15;
    }

    public static int zFromBlockZ(int blockZ) {
        return blockZ & 15;
    }

    public boolean isEmpty() {
        for (int index : indices) {
            if (index != 0) {
                return false;
            }
        }
        return true;
    }

    // returns amount of entries removed
    // also kind of expensive, so only run occasionally
    public int cleanJunkData() {

        short[] remap = new short[palette.size()];
        Arrays.fill(remap, (short) -1);

        List<BlockState> compactedPalette = new ArrayList<>();
        Map<BlockState, Short> compactedLookup = new HashMap<>();

        compactedPalette.add(null);
        remap[0] = 0;

        for (int i = 0; i < indices.length; i++) {
            int oldIndex = indices[i];

            if (oldIndex == 0) {
                continue;
            }

            if (oldIndex < 0 || oldIndex >= palette.size()) {
                throw new IllegalStateException("Invalid palette index: " + oldIndex);
            }

            short newIndex = remap[oldIndex];

            if (newIndex == -1) {
                BlockState state = palette.get(oldIndex);

                if (state == null) {
                    indices[i] = 0;
                    continue;
                }

                Short existingIndex = compactedLookup.get(state);

                if (existingIndex != null) {
                    newIndex = existingIndex;
                } else {
                    newIndex = (short) compactedPalette.size();
                    compactedPalette.add(state);
                    compactedLookup.put(state, newIndex);
                }

                remap[oldIndex] = newIndex;
            }

            indices[i] = newIndex;
        }

        int removed = palette.size() - compactedPalette.size();

        palette.clear();
        palette.addAll(compactedPalette);

        paletteLookup.clear();
        paletteLookup.putAll(compactedLookup);

        return removed;
    }

    public void readFromNBT(NBTTagCompound compound) {

        palette.clear();
        paletteLookup.clear();

        NBTTagList states = compound.getTagList(KEY_STATES, 10);
        for (short i = 0; i < states.tagCount(); i++) {

            NBTTagCompound stateCompound = states.getCompoundTagAt(i);
            BlockState state = null;

            if (!stateCompound.getBoolean(KEY_EMPTY)) {
                state = BlockState.readFromNBT(stateCompound);
            }

            palette.add(state);

            if (state != null) {
                paletteLookup.put(state, i);
            }

        }

        byte[] savedIndices = compound.getByteArray(KEY_INDICES);
        if (savedIndices.length != indices.length * 2) {
            throw new IllegalStateException("Invalid indices array length: " + savedIndices.length);
        }

        ByteBuffer buffer = ByteBuffer.wrap(savedIndices);
        for (int i = 0; i < indices.length; i++) {
            indices[i] = buffer.getShort();
        }

    }

    /**
     * serialize {@link BlockState} instead of saving just the state indices since indices could change if a new
     * property
     * is added to a {@link BlockStateContainer}
     */
    public void writeToNBT(NBTTagCompound compound) {

        NBTTagList states = new NBTTagList();

        for (BlockState state : palette) {
            NBTTagCompound stateCompound = null;
            if (state == null || state.getCachedNBT() == null) {
                stateCompound = new NBTTagCompound();
                stateCompound.setBoolean(KEY_EMPTY, true);
            } else {
                stateCompound = state.getCachedNBT();
            }

            states.appendTag(stateCompound);
        }

        compound.setTag(KEY_STATES, states);

        byte[] byteIndices = new byte[indices.length * 2];
        ByteBuffer.wrap(byteIndices)
            .asShortBuffer()
            .put(indices);

        compound.setByteArray(KEY_INDICES, byteIndices);

    }

}
