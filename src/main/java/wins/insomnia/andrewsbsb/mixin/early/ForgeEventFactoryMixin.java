package wins.insomnia.andrewsbsb.mixin.early;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import wins.insomnia.andrewsbsb.api.blockstate.BlockState;
import wins.insomnia.andrewsbsb.mixinduck.IBlockStateHoldingMixin;
import wins.insomnia.andrewsbsb.mixinduck.IItemInWorldManagerMixin;

@Mixin(value = ForgeEventFactory.class, remap = false)
public class ForgeEventFactoryMixin {

    @ModifyVariable(method = "fireBlockHarvesting", at = @At(value = "STORE", ordinal = 0))
    private static BlockEvent.HarvestDropsEvent andrewsBSB$modifyFireBlockHarvestingEvent(
        BlockEvent.HarvestDropsEvent event, ArrayList<ItemStack> drops, World world, Block block, int x, int y, int z,
        int meta, int fortune, float dropChance, boolean silkTouch, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            if (((EntityPlayerMP) player).theItemInWorldManager instanceof IItemInWorldManagerMixin) {
                IItemInWorldManagerMixin worldManager = (IItemInWorldManagerMixin) ((EntityPlayerMP) player).theItemInWorldManager;

                BlockState blockState = worldManager.andrewsBSB$getBlockStateBeforeBlockRemoval();

                if (blockState != null) {
                    if (event instanceof IBlockStateHoldingMixin) {
                        ((IBlockStateHoldingMixin) event).andrewsBSB$setBlockState(blockState);
                        worldManager.andrewsBSB$clearBlockStateBeforeBlockRemoval();
                    }
                }
            }
        }
        return event;
    }

}
