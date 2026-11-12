package wins.insomnia.andrewsbsb.api;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.util.BlockStateUtil;

public class AndrewsBSBAPI {

    public static void setBlockState(World world, int blockX, int blockY, int blockZ, @Nullable BlockState blockState)
        throws Exception {
        BlockStateUtil.setBlockState(world, blockX, blockY, blockZ, blockState);
    }

    public static Optional<BlockState> getBlockState(World world, int blockX, int blockY, int blockZ) {
        return getBlockState(world, world.getChunkFromBlockCoords(blockX, blockZ), blockX, blockY, blockZ);
    }

    public static Optional<BlockState> getBlockState(World world, Chunk chunk, int blockX, int blockY, int blockZ) {
        return BlockStateUtil.getBlockState(world, chunk, blockX, blockY, blockZ);
    }

    public static Optional<BlockState> getDefaultBlockState(Block block) {
        return BlockStateUtil.getDefaultBlockState(block);
    }

}
