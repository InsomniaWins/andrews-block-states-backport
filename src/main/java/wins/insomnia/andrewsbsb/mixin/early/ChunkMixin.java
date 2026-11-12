package wins.insomnia.andrewsbsb.mixin.early;

import net.minecraft.world.chunk.Chunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import wins.insomnia.andrewsbsb.mixinduck.IChunkBlockStateDataHolder;
import wins.insomnia.andrewsbsb.world.chunk.ChunkBlockStateData;

@Mixin(Chunk.class)
public class ChunkMixin implements IChunkBlockStateDataHolder {

    @Unique
    private ChunkBlockStateData andrewsBSB$blockStateData = null;

    @Override
    public ChunkBlockStateData andrewsBSB$getChunkBlockStateData() {
        if (andrewsBSB$blockStateData == null) {
            andrewsBSB$blockStateData = new ChunkBlockStateData((Chunk) (Object) this);
        }

        return andrewsBSB$blockStateData;
    }
}
