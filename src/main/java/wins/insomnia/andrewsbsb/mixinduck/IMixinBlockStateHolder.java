package wins.insomnia.andrewsbsb.mixinduck;

import wins.insomnia.andrewsbsb.world.blockstate.BlockState;
import wins.insomnia.andrewsbsb.world.blockstate.BlockStateContainer;

public interface IMixinBlockStateHolder {

    BlockStateContainer andrewsBSB$getBlockStateContainer();

    BlockState andrewsBSB$getDefaultBlockState();

}
