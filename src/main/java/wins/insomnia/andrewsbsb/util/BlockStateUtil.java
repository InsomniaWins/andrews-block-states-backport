package wins.insomnia.andrewsbsb.util;

import java.util.Optional;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.api.blockstate.block.IBlockStateHolder;
import wins.insomnia.andrewsbsb.mixinduck.IChunkBlockStateDataHolder;
import wins.insomnia.andrewsbsb.mixinduck.IMixinBlockStateHolder;
import wins.insomnia.andrewsbsb.world.chunk.ChunkBlockStateData;

public class BlockStateUtil {

    public static boolean debugInfoEnabled = false;

    private static String getChunkString(Chunk chunk, int blockX, int blockY, int blockZ) {
        return "Chunk at " + blockX + ", " + blockY + ", " + blockZ;
    }

    public static void setBlockState(World world, int blockX, int blockY, int blockZ, BlockState blockState)
        throws Exception {
        Chunk chunk = world.getChunkFromBlockCoords(blockX, blockZ);
        if (!(chunk instanceof IChunkBlockStateDataHolder)) {
            throw new IllegalStateException(
                getChunkString(chunk, blockX, blockY, blockZ) + " is not an IChunkBlockStateDataHolder!");
        }

        IChunkBlockStateDataHolder dataHolder = (IChunkBlockStateDataHolder) chunk;
        ChunkBlockStateData stateData = dataHolder.andrewsBSB$getChunkBlockStateData();

        if (stateData == null) {
            throw new IllegalStateException(
                getChunkString(chunk, blockX, blockY, blockZ) + " doesnt have ChunkBlockStateData! Data is null!");
        }

        Block block = world.getBlock(blockX, blockY, blockZ);
        if (blockState != null && blockState.owner != block) {
            throw new IllegalArgumentException(
                "Block state of " + blockState
                    + " is not owned by the block \""
                    + block.getLocalizedName()
                    + "\" at "
                    + blockX
                    + ", "
                    + blockY
                    + ", "
                    + blockZ);
        }

        if (blockState == null && (block instanceof IMixinBlockStateHolder || block instanceof IBlockStateHolder)) {
            throw new IllegalArgumentException("Target block has blockstates; cannot set the state to null!");
        }

        stateData.setState(blockX, blockY, blockZ, blockState);
    }

    // is blocking if the chunk is not loaded (i think)
    public static Optional<BlockState> getBlockState(World world, int blockX, int blockY, int blockZ) {
        return getBlockState(world, world.getChunkFromBlockCoords(blockX, blockZ), blockX, blockY, blockZ);
    }

    public static Optional<BlockState> getBlockState(World world, Chunk chunk, int blockX, int blockY, int blockZ) {

        if (!(chunk instanceof IChunkBlockStateDataHolder)) {

            if (debugInfoEnabled) {
                AndrewsBSB.LOG.warn("Chunk is not an IChunkBlockStateDataHolder!");
            }

            return Optional.empty();
        }

        return getBlockState(world, (IChunkBlockStateDataHolder) chunk, blockX, blockY, blockZ);
    }

    public static Optional<BlockState> getBlockState(World world, IChunkBlockStateDataHolder stateDataHolder,
        int blockX, int blockY, int blockZ) {

        if (stateDataHolder == null) {

            if (debugInfoEnabled) {
                AndrewsBSB.LOG.warn("IChunkBlockStateDataHolder is null!");
            }

            return Optional.empty();
        }

        return getBlockState(world, stateDataHolder.andrewsBSB$getChunkBlockStateData(), blockX, blockY, blockZ);
    }

    public static Optional<BlockState> getBlockState(World world, ChunkBlockStateData stateData, int blockX, int blockY,
        int blockZ) {

        if (world == null) {
            if (debugInfoEnabled) {
                AndrewsBSB.LOG.info("World is null!");
            }

            return Optional.empty();
        }

        if (stateData == null) {

            if (debugInfoEnabled) {
                AndrewsBSB.LOG.warn("ChunkBlockStateData is null!");
            }

            return Optional.empty();
        }

        Optional<BlockState> stateOptional = stateData.getState(blockX, blockY, blockZ);

        // if block owner doesnt match the block at x, y, z, replace state with the default state of the real block at
        // x, y, z
        if (stateOptional.isPresent()) {
            BlockState blockState = stateOptional.get();
            Block worldBlock = world.getBlock(blockX, blockY, blockZ);
            if (blockState.owner != worldBlock) {

                stateOptional = getDefaultBlockState(worldBlock);
                if (stateOptional.isPresent()) {
                    stateData.setState(blockX, blockY, blockZ, stateOptional.get());
                } else {
                    stateData.setState(blockX, blockY, blockZ, null);
                }

            }
        }

        return stateOptional;
    }

    public static Optional<BlockState> getDefaultBlockState(Block block) {
        if (block instanceof IMixinBlockStateHolder) {
            IMixinBlockStateHolder stateHolder = (IMixinBlockStateHolder) block;
            return Optional.ofNullable(stateHolder.andrewsBSB$getDefaultBlockState());
        } else if (block instanceof IBlockStateHolder) {
            IBlockStateHolder stateHolder = (IBlockStateHolder) block;
            return Optional.ofNullable(stateHolder.getDefaultBlockState());
        }
        return Optional.empty();
    }

}
