package wins.insomnia.andrewsbsb.mixin.early;

import java.util.Optional;

import net.minecraftforge.event.world.BlockEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.mixinduck.IBlockStateHoldingMixin;

@Mixin(value = BlockEvent.HarvestDropsEvent.class, remap = false)
public class HarvestDropsEventMixin implements IBlockStateHoldingMixin {

    @Unique
    public BlockState andrewsBSB$blockState = null;

    @Override
    public Optional<BlockState> andrewsBSB$getBlockState() {
        return Optional.ofNullable(andrewsBSB$blockState);
    }

    @Override
    public void andrewsBSB$setBlockState(BlockState blockState) {
        andrewsBSB$blockState = blockState;
    }
}
