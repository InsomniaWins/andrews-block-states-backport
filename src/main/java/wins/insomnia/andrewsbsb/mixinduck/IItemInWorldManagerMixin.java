package wins.insomnia.andrewsbsb.mixinduck;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;

public interface IItemInWorldManagerMixin {

    BlockState andrewsBSB$getBlockStateBeforeBlockRemoval();

    void andrewsBSB$clearBlockStateBeforeBlockRemoval();

}
