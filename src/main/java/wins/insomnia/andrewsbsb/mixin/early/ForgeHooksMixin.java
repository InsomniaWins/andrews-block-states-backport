package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import wins.insomnia.andrewsbsb.api.BlockStateEventContext;
import wins.insomnia.andrewsbsb.api.event.SetBlockStateEvent;

@Mixin(ForgeHooks.class)
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

}
