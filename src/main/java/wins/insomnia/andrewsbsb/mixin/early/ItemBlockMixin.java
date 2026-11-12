package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;

import wins.insomnia.andrewsbsb.event.custom.SetBlockStateEvent;
import wins.insomnia.andrewsbsb.util.context.BlockStateEventContext;

@Mixin(ItemBlock.class)
public class ItemBlockMixin {

    @WrapMethod(method = "onItemUse")
    private boolean andrewsBSB$wrapOnItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z,
        int side, float hitX, float hitY, float hitZ, Operation<Boolean> original) {
        if (world.isRemote) {
            return original.call(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }

        boolean returnValue = false;
        try (BlockStateEventContext.Scope scope = BlockStateEventContext.push(SetBlockStateEvent.PLAYER_PLACED)) {
            returnValue = original.call(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return returnValue;
    }

}
