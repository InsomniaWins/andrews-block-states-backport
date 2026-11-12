package wins.insomnia.andrewsbsb.world.chunk;

import java.util.Optional;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.chunk.Chunk;

import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.util.BlockStateUtil;
import wins.insomnia.andrewsbsb.world.blockstate.BlockStatePalette;

public class ChunkBlockStateData {

    public static final String KEY_DATA = "AndrewsBSB_BlockStateData";
    public static final String KEY_SECTION = "AndrewsBSB_Section";

    public static final int SECTION_HEIGHT = 16;

    private final BlockStatePalette[] statePalettes;
    private final int worldHeight;

    public ChunkBlockStateData(Chunk chunk) {
        worldHeight = chunk.worldObj.getActualHeight();
        statePalettes = new BlockStatePalette[(worldHeight + SECTION_HEIGHT - 1) / SECTION_HEIGHT];
    }

    public void setState(int x, int y, int z, BlockState state) {
        BlockStatePalette palette = getOrMakePalette(y);
        palette.setFromBlockPos(x, y, z, state);

        // check state == null since thats what removes palette entry
        if (state == null && palette.isEmpty()) {
            setPaletteAt(getPaletteIndex(y), null);
        }

    }

    public Optional<BlockState> getState(int x, int y, int z) {
        BlockStatePalette palette = getOrMakePalette(y);
        if (palette == null) {

            if (BlockStateUtil.debugInfoEnabled) {
                AndrewsBSB.LOG.warn("BlockState palette is null at {},{},{}", x, y, z);
            }

            return Optional.empty();
        }

        return Optional.ofNullable(palette.getFromBlockPos(x, y, z));
    }

    public BlockStatePalette getOrMakePalette(int blockY) {
        return getOrMakePaletteFromIndex(getPaletteIndex(blockY));
    }

    public BlockStatePalette getOrMakePaletteFromIndex(int index) {
        BlockStatePalette palette = getPaletteFromIndex(index);

        if (palette == null) {
            palette = new BlockStatePalette();
            setPaletteAt(index, palette);
        }

        return palette;
    }

    public void setPalette(int blockY, BlockStatePalette palette) {
        int index = getPaletteIndex(blockY);
        setPaletteAt(index, palette);
    }

    public void setPaletteAt(int index, BlockStatePalette palette) {
        statePalettes[index] = palette;
    }

    public int getPaletteIndex(int blockY) {

        if (blockY >= worldHeight || blockY < 0) {
            throw new IndexOutOfBoundsException("blockY is out of bounds for the world height!");
        }

        return blockY >> 4;
    }

    public BlockStatePalette getPaletteFromIndex(int paletteIndex) {
        return statePalettes[paletteIndex];
    }

    public BlockStatePalette getPalette(int blockY) {
        return getPaletteFromIndex(getPaletteIndex(blockY));
    }

    public void readFromNBT(NBTTagCompound compound) {

        if (!compound.hasKey(KEY_DATA, 9)) {
            return;
        }

        NBTTagList dataList = compound.getTagList(KEY_DATA, 10);

        for (int i = 0; i < dataList.tagCount(); i++) {
            NBTTagCompound paletteCompound = dataList.getCompoundTagAt(i);

            int sectionIndex = paletteCompound.getByte(KEY_SECTION);

            if (sectionIndex < 0 || sectionIndex >= statePalettes.length) {
                continue;
            }

            BlockStatePalette palette = new BlockStatePalette();

            palette.readFromNBT(paletteCompound);

            statePalettes[sectionIndex] = palette;
        }
    }

    public void writeToNBT(NBTTagCompound compound) {

        NBTTagList dataList = new NBTTagList();

        for (int i = 0; i < statePalettes.length; i++) {

            BlockStatePalette palette = statePalettes[i];

            if (palette == null) {
                continue;
            }

            NBTTagCompound paletteCompound = new NBTTagCompound();

            paletteCompound.setByte(KEY_SECTION, (byte) i);

            palette.writeToNBT(paletteCompound);

            dataList.appendTag(paletteCompound);
        }

        compound.setTag(KEY_DATA, dataList);
    }

}
