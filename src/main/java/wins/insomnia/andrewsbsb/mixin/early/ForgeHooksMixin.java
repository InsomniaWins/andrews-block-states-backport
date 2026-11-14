package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.world.BlockEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import wins.insomnia.andrewsbsb.api.BlockStateEventContext;
import wins.insomnia.andrewsbsb.api.event.SetBlockStateEvent;
import wins.insomnia.andrewsbsb.mixinduck.IBlockStateHoldingMixin;
import wins.insomnia.andrewsbsb.util.BlockStateUtil;

@Mixin(value = ForgeHooks.class, remap = false)
public class ForgeHooksMixin {

    @Redirect(
        method = "onPlaceItemIntoWorld",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBlockAdded(Lnet/minecraft/world/World;III)V"))
    private static void andrewsBSB$onBlockAdded(Block block, World world, int x, int y, int z) {
        try (BlockStateEventContext.Scope scope = BlockStateEventContext
            .push(SetBlockStateEvent.FORGE_SNAPSHOT_REPLAY)) {
            block.onBlockAdded(world, x, y, z);
        }
    }

    @ModifyVariable(
        method = "onBlockBreakEvent",
        at = @At(
            value = "STORE",
            target = "Lnet/minecraftforge/event/world/BlockEvent$BreakEvent;<init>(IIIILnet/minecraft/block/Block;ILnet/minecraft/entity/player/EntityPlayer;)V"))
    private static BlockEvent.BreakEvent andrewsBSB$injectBreakEventState(BlockEvent.BreakEvent event, World world,
        WorldSettings.GameType gameType, EntityPlayerMP entityPlayer, int x, int y, int z) {
        if (event instanceof IBlockStateHoldingMixin) {
            var blockState = BlockStateUtil.getBlockState(world, x, y, z)
                .orElse(null);
            ((IBlockStateHoldingMixin) event).andrewsBSB$setBlockState(blockState);
        }
        return event;
    }

}
