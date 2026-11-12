package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import wins.insomnia.andrewsbsb.api.BlockStateEventContext;
import wins.insomnia.andrewsbsb.api.event.SetBlockStateEvent;

@Mixin(WorldGenerator.class)
public class WorldGeneratorMixin {

    @WrapMethod(method = "setBlockAndNotifyAdequately")
    private void andrewsBSB$wrapSetBlockAndNotifyAdequately(World world, int x, int y, int z, Block block, int meta,
        Operation<Void> original) {
        try (BlockStateEventContext.Scope scope = BlockStateEventContext.push(SetBlockStateEvent.WORLD_GENERATION)) {
            original.call(world, x, y, z, block, meta);
        }
    }

}
