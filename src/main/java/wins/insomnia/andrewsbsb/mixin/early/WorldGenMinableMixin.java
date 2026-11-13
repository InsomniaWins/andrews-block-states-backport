package wins.insomnia.andrewsbsb.mixin.early;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import wins.insomnia.andrewsbsb.api.BlockStateEventContext;
import wins.insomnia.andrewsbsb.api.event.SetBlockStateEvent;

@Mixin(WorldGenMinable.class)
public class WorldGenMinableMixin {

    @WrapMethod(method = "generate")
    private boolean onGenerate(World world, Random random, int x, int y, int z, Operation<Boolean> original) {
        boolean returnValue = false;

        try (BlockStateEventContext.Scope scope = BlockStateEventContext.push(SetBlockStateEvent.WORLD_GENERATION)) {
            returnValue = original.call(world, random, x, y, z);
        }

        return returnValue;
    }

}
