package wins.insomnia.andrewsbsb.event.handler;

import java.util.Optional;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import wins.insomnia.andrewsbsb.event.EventHandler;
import wins.insomnia.andrewsbsb.event.custom.SetBlockStateEvent;
import wins.insomnia.andrewsbsb.mixinduck.IChunkBlockStateDataHolder;
import wins.insomnia.andrewsbsb.world.chunk.ChunkBlockStateData;

public class BlockStateHandler extends EventHandler {

    public BlockStateHandler() {
        super("block_state");
    }

    @SubscribeEvent
    public void onSetBlockStateEvent(SetBlockStateEvent event) {}

    @SubscribeEvent
    public void onSaveChunkData(ChunkDataEvent.Save event) {
        saveBlockStateData(event);
    }

    @SubscribeEvent
    public void onLoadChunkData(ChunkDataEvent.Load event) {
        loadBlockStateData(event);
    }

    private Optional<ChunkBlockStateData> getBlockStateData(Chunk chunk) {
        if (!(chunk instanceof IChunkBlockStateDataHolder)) {
            return Optional.empty();
        }

        IChunkBlockStateDataHolder stateHolder = (IChunkBlockStateDataHolder) chunk;
        return Optional.ofNullable(stateHolder.andrewsBSB$getChunkBlockStateData());
    }

    private void saveBlockStateData(ChunkDataEvent.Save event) {
        ChunkBlockStateData stateData = getBlockStateData(event.getChunk()).orElse(null);
        if (stateData == null) {
            return;
        }

        stateData.writeToNBT(event.getData());
    }

    private void loadBlockStateData(ChunkDataEvent.Load event) {
        ChunkBlockStateData stateData = getBlockStateData(event.getChunk()).orElse(null);
        if (stateData == null) {
            return;
        }

        stateData.readFromNBT(event.getData());
    }

}
