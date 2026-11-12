package wins.insomnia.andrewsbsb.block;

import wins.insomnia.andrewsbsb.world.blockstate.BlockState;
import wins.insomnia.andrewsbsb.world.blockstate.BlockStateContainer;

public interface IBlockStateHolder {

    BlockStateContainer getBlockStateContainer();

    BlockState getDefaultBlockState();

}
