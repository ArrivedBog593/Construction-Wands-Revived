package nadiendev.constructionwand.data;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/** Advancement background type: a plain texture location before 1.21.5, a {@code ClientAsset} from then on. */
public final class AdvancementCompat {

    private AdvancementCompat() {
    }

    //? if >=1.21.9 {
    /*public static Optional<net.minecraft.core.ClientAsset.ResourceTexture> background(ResourceLocation texture) {
        return Optional.of(new net.minecraft.core.ClientAsset.ResourceTexture(texture));
    }
    *///?} elif >=1.21.5 {
    /*public static Optional<net.minecraft.core.ClientAsset> background(ResourceLocation texture) {
        return Optional.of(new net.minecraft.core.ClientAsset(texture));
    }
    *///?} else {
    public static Optional<ResourceLocation> background(ResourceLocation texture) {
        return Optional.of(texture);
    }
    //?}
}
