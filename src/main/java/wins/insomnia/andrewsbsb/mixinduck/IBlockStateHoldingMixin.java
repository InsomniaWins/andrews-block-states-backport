package wins.insomnia.andrewsbsb.mixinduck;

import java.util.Optional;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;

public interface IBlockStateHoldingMixin {

    Optional<BlockState> andrewsBSB$getBlockState();

    void andrewsBSB$setBlockState(BlockState blockState);

}
