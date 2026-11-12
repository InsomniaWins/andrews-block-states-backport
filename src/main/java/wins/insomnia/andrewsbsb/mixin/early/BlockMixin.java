package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.event.custom.SetBlockStateEvent;
import wins.insomnia.andrewsbsb.util.BlockStateUtil;
import wins.insomnia.andrewsbsb.util.context.BlockStateEventContext;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "onBlockAdded", at = @At("TAIL"))
    public void andrewsBSB$onBlockAddedTail(World world, int x, int y, int z, CallbackInfo ci) {

        if (!world.isRemote) {
            andrewsBSB$emitBlockStateEvent(world, x, y, z);
        }

    }

    @Unique
    private void andrewsBSB$emitBlockStateEvent(World world, int x, int y, int z) {

        if (BlockStateEventContext.get()
            .equals(SetBlockStateEvent.FORGE_SNAPSHOT_REPLAY)) {
            return;
        }

        Block block = (Block) (Object) this;
        int meta = world.getBlockMetadata(x, y, z);

        SetBlockStateEvent event = new SetBlockStateEvent(world, block, meta, x, y, z, BlockStateEventContext.get());
        boolean cancelled = MinecraftForge.EVENT_BUS.post(event);

        if (cancelled) {
            return;
        }

        BlockState resultState = event.blockState;

        try {
            BlockStateUtil.setBlockState(world, x, y, z, resultState);
        } catch (Exception e) {
            AndrewsBSB.LOG.error("Error while setting block state!", e);
        }

    }

}
