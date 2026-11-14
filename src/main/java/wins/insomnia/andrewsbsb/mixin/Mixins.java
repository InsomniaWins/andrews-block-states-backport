package wins.insomnia.andrewsbsb.mixin;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    BLOCK(new MixinBuilder("BlockMixin").setPhase(Phase.EARLY)
        .addCommonMixins("BlockMixin")),
    BLOCK_BREAK_EVENT(new MixinBuilder("BlockBreakEventMixin").setPhase(Phase.EARLY)
        .addCommonMixins("BlockBreakEventMixin")),
    CHUNK(new MixinBuilder("ChunkMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ChunkMixin")),
    FORGE_EVENT_FACTORY(new MixinBuilder("ForgeEventFactoryMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ForgeEventFactoryMixin")),
    FORGE_HOOKS(new MixinBuilder("ForgeHooksMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ForgeHooksMixin")),
    HARVEST_DROPS_EVENT(new MixinBuilder("HarvestDropsEventMixin").setPhase(Phase.EARLY)
        .addCommonMixins("HarvestDropsEventMixin")),
    ITEM_BLOCK(new MixinBuilder("ItemBlockMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ItemBlockMixin")),
    ITEM_IN_WORLD_MANAGER_MIXIN(new MixinBuilder("ItemInWorldManagerMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ItemInWorldManagerMixin")),
    WORLD_GEN_MINABLE(new MixinBuilder("WorldGenMinableMixin").setPhase(Phase.EARLY)
        .addCommonMixins("WorldGenMinableMixin")),
    WORLD_GENERATOR(new MixinBuilder("WorldGeneratorMixin").setPhase(Phase.EARLY)
        .addCommonMixins("WorldGeneratorMixin"));

    private final MixinBuilder builder;

    Mixins(MixinBuilder builder) {
        this.builder = builder;
    }

    @Nonnull
    @Override
    public MixinBuilder getBuilder() {
        return builder;
    }
}
