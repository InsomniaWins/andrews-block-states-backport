package wins.insomnia.andrewsbsb.mixinduck;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.api.blockstate.BlockStateContainer;

public interface IMixinBlockStateHolder {

    BlockStateContainer andrewsBSB$getBlockStateContainer();

    BlockState andrewsBSB$getDefaultBlockState();

}
