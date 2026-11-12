package wins.insomnia.andrewsbsb.mixin;

import javax.annotation.Nonnull;

import com.gtnewhorizon.gtnhmixins.builders.IMixins;
import com.gtnewhorizon.gtnhmixins.builders.MixinBuilder;

public enum Mixins implements IMixins {

    BLOCK(new MixinBuilder("BlockMixin").setPhase(Phase.EARLY)
        .addCommonMixins("BlockMixin")),
    CHUNK(new MixinBuilder("ChunkMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ChunkMixin")),
    FORGE_HOOKS(new MixinBuilder("ForgeHooksMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ForgeHooksMixin")),
    ITEM_BLOCK(new MixinBuilder("ItemBlockMixin").setPhase(Phase.EARLY)
        .addCommonMixins("ItemBlockMixin")),
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
