package nadiendev.constructionwand.data;

import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/** Advancement background type: a plain texture location before 1.21.5, a {@code ClientAsset} from then on. */
public final class AdvancementCompat {

    private AdvancementCompat() {
    }

    public static Optional<ResourceLocation> background(ResourceLocation texture) {
        return Optional.of(texture);
    }
}
