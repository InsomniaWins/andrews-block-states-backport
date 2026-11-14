package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.mixinduck.IItemInWorldManagerMixin;
import wins.insomnia.andrewsbsb.util.BlockStateUtil;

@Mixin(ItemInWorldManager.class)
public abstract class ItemInWorldManagerMixin implements IItemInWorldManagerMixin {

    @Shadow
    protected abstract boolean removeBlock(int p_73079_1_, int p_73079_2_, int p_73079_3_);

    @Shadow
    protected abstract boolean removeBlock(int p_73079_1_, int p_73079_2_, int p_73079_3_, boolean canHarvest);

    @Shadow
    public World theWorld;

    @Unique
    private BlockState andrewsBSB$blockStateBeforeBlockRemoval = null;

    @Redirect(
        method = "tryHarvestBlock",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/ItemInWorldManager;removeBlock(III)Z"))
    private boolean andrewsBSB$setBlockStateBeforeRemovingBlockA(ItemInWorldManager instance, int p_73079_1_,
        int p_73079_2_, int p_73079_3_) {
        andrewsBSB$blockStateBeforeBlockRemoval = BlockStateUtil
            .getBlockState(theWorld, p_73079_1_, p_73079_2_, p_73079_3_)
            .orElse(null);
        return removeBlock(p_73079_1_, p_73079_2_, p_73079_3_);
    }

    @Redirect(
        method = "tryHarvestBlock",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/management/ItemInWorldManager;removeBlock(IIIZ)Z"))
    private boolean andrewsBSB$setBlockStateBeforeRemovingBlockB(ItemInWorldManager instance, int p_73079_1_,
        int p_73079_2_, int p_73079_3_, boolean canHarvest) {
        andrewsBSB$blockStateBeforeBlockRemoval = BlockStateUtil
            .getBlockState(theWorld, p_73079_1_, p_73079_2_, p_73079_3_)
            .orElse(null);
        return removeBlock(p_73079_1_, p_73079_2_, p_73079_3_, canHarvest);
    }

    @Override
    public BlockState andrewsBSB$getBlockStateBeforeBlockRemoval() {
        return andrewsBSB$blockStateBeforeBlockRemoval;
    }

    @Override
    public void andrewsBSB$clearBlockStateBeforeBlockRemoval() {
        andrewsBSB$blockStateBeforeBlockRemoval = null;
    }
}
