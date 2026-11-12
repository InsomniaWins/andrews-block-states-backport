package wins.insomnia.andrewsbsb.block;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.api.blockstate.BlockStateContainer;

public interface IBlockStateHolder {

    BlockStateContainer getBlockStateContainer();

    BlockState getDefaultBlockState();

}
