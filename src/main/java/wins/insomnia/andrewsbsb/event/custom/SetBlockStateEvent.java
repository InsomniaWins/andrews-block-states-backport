package wins.insomnia.andrewsbsb.event.custom;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import cpw.mods.fml.common.eventhandler.Event;
import wins.insomnia.andrewsbsb.AndrewsBSB;
import wins.insomnia.andrewsbsb.block.IBlockStateHolder;
import wins.insomnia.andrewsbsb.mixinduck.IMixinBlockStateHolder;
import wins.insomnia.andrewsbsb.world.blockstate.BlockState;

public class SetBlockStateEvent extends Event {

    public static final Context UNKNOWN = new Context(AndrewsBSB.MODID, "unknown");
    public static final Context WORLD_GENERATION = new Context(AndrewsBSB.MODID, "world_generation");
    public static final Context PLAYER_PLACED = new Context(AndrewsBSB.MODID, "player_placed");
    public static final Context FORGE_SNAPSHOT_REPLAY = new Context(AndrewsBSB.MODID, "forge_snapshot_replay");

    public final World world;
    public final Block block;
    public final int meta;
    public final int blockX;
    public final int blockY;
    public final int blockZ;
    public final Context context;

    public final BlockState defaultBlockState;
    public BlockState blockState = null; // set this to the blockstate you would like to write to the world

    public SetBlockStateEvent(World world, Block block, int meta, int blockX, int blockY, int blockZ, Context context) {
        this.world = world;
        this.block = block;
        this.meta = meta;
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.context = context;

        if (block instanceof IMixinBlockStateHolder) {
            IMixinBlockStateHolder stateHolder = (IMixinBlockStateHolder) block;
            defaultBlockState = stateHolder.andrewsBSB$getDefaultBlockState();
        } else if (block instanceof IBlockStateHolder) {
            IBlockStateHolder stateHolder = (IBlockStateHolder) block;
            defaultBlockState = stateHolder.getDefaultBlockState();
        } else {
            defaultBlockState = null;
        }

        if (defaultBlockState != null) {
            blockState = defaultBlockState;
        }
    }

    @Override
    public boolean isCancelable() {
        return true;
    }

    public boolean isContextUnknown() {
        return context == UNKNOWN || context.equals(UNKNOWN);
    }

    public static class Context {

        public final String modId;
        public final String contextId;

        public Context(String modId, String contextId) {
            this.modId = modId;
            this.contextId = contextId;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Context)) {
                return super.equals(obj);
            }

            Context other = (Context) obj;
            return other.modId.equals(modId) && other.contextId.equals(contextId);
        }

        @Override
        public String toString() {
            return "Context{" + modId + ":" + contextId + "}";
        }
    }

}
